package edu.nku.csc540.hashtag.stream;

import edu.nku.csc540.hashtag.model.Snapshot;

public class HashtagProcessingThread implements Runnable {
	private Snapshot snapshot;
	
	public HashtagProcessingThread(Snapshot snapshot){
		this.snapshot = snapshot;
	}

	@Override
	public void run() {
		HashtagCountGenerator hashtagGen = new HashtagCountGenerator();
		hashtagGen.readTwitterFeed(snapshot);
	}

}
