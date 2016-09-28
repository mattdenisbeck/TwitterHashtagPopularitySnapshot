package edu.nku.csc540.hashtag.stream;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.nku.csc540.hashtag.model.PopularityDatapoint;
import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TweetStreamListener implements StatusListener {
	private LocalDateTime currentTimestamp; 
	private Map<String, Integer> currentCounts;
	private int interval;
	private List<String> hashtags;
	private String snapshot_id;
	private Map<String,Integer> emptyCounts = new HashMap<String,Integer>(); //for reseting counts at each interval
	private DataPointWriter writer;
	
	public TweetStreamListener(int interval, List<String> hashtags, String snapshot_id){
		currentTimestamp = LocalDateTime.now();
		currentCounts = new HashMap<String,Integer>();
		hashtags.forEach(hashtag -> currentCounts.put(hashtag, 0));
		emptyCounts.putAll(currentCounts);
		this.interval = interval;
		this.hashtags = hashtags;
		this.snapshot_id = snapshot_id;
		writer = new DataPointWriter();
	}
	
	@Override
	public void onStatus(Status status) {
		//convert Status 'createdAt' to LocalDateTime
		LocalDateTime tweetTime = getTime(status);

		//count hashtags in status
		countHashtags(status);
		
		//check if interval elapsed
		if(tweetTime.compareTo(currentTimestamp.plusSeconds(interval)) > 0) {
			currentTimestamp = tweetTime;
			System.out.println("interval elapsed");
			
			//construct datapoint from currentCounts
			PopularityDatapoint dataPoint = new PopularityDatapoint(currentTimestamp, currentCounts);
			dataPoint.setSnapshotID(snapshot_id);
			System.out.println(dataPoint.toString());
			//write datapoint to kinesis
			writer.sendToKinesis(dataPoint);
			
			//clear counts
			currentCounts.putAll(emptyCounts);
		}
		System.out.println(status.getCreatedAt() +": " + status.getText());  
	}
	
	private LocalDateTime getTime(Status status){
		Date ts = status.getCreatedAt();
		Instant instant = Instant.ofEpochMilli(ts.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}
	
	private void countHashtags(Status status){
		HashtagEntity[] hashtagEntities = status.getHashtagEntities();
		for(HashtagEntity ht: hashtagEntities){
			if (hashtags.contains(ht.getText())){
				int score = currentCounts.get(ht.getText());
				score++;
				currentCounts.put(ht.getText(), score);
			}
		}
	}

	@Override
	public void onException(Exception e) {
		System.out.println("Exception occured:" + e.getMessage());
		e.printStackTrace();
	}

	@Override
	public void onTrackLimitationNotice(int n) {
		System.out.println("Track limitation notice for " + n);
	}

	@Override
	public void onStallWarning(StallWarning arg0) {
		System.out.println("Stall warning");
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		System.out.println("Scrub geo with:" + arg0 + ":" + arg1);
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		System.out.println("Status deletion notice");
	}
}
