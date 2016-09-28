package edu.nku.csc540.hashtag.stream;

import java.util.List;

import edu.nku.csc540.hashtag.model.Snapshot;
import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class HashtagCountGenerator {
	private TwitterStream stream;
	private StatusListener listener; 
	
	public HashtagCountGenerator(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("EImVEPB9KivgJPt8TOHk2Uf4P")
		  .setOAuthConsumerSecret("cGkF3NiMsat1JFBvx0s8sGjMXH34fKJwi19JfS1X2bk1HfgEE0")
		  .setOAuthAccessToken("16338219-sHcieKVq3R5i4F3mqL0nIlaRqsjssvkOUEmDKtGKg")
		  .setOAuthAccessTokenSecret("BfftWD4JPcxJX9DvhqtQD0z1VL5U3vWybtwPiej7YKsWQ");
		TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
		stream = tf.getInstance();
	};
	
	public void readTwitterFeed(Snapshot snapshot) {
		
		int interval = snapshot.getInterval();
		List<String> hashtags = snapshot.getHashtags();
		
		FilterQuery qry = new FilterQuery();
		String[] keywords = hashtags.toArray(new String[0]);
		
		qry.track(keywords);
		
		listener = new TweetStreamListener(interval, hashtags, snapshot.getId());
		stream.addListener(listener);
		stream.filter(qry);
	}
}

