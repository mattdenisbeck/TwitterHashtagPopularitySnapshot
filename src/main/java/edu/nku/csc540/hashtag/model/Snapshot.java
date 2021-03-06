package edu.nku.csc540.hashtag.model;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "hashtagAppSnapshotsTable")
public class Snapshot {
	private List<String> hashtags = new ArrayList<String>();
	private int interval;
	private String id;
	private String owner;
	
	public Snapshot(List<String> hashtags, int interval, String owner) {
		this.hashtags = hashtags;
		this.interval = interval;
		this.owner = owner;
	}
	public Snapshot(List<String> hashtags, int interval, String owner, String id) {
		this.hashtags = hashtags;
		this.interval = interval;
		this.owner = owner;
		this.id = id;
	}
	
	public Snapshot() {}
	
	@DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	@DynamoDBAttribute(attributeName="Hashtags")
	public List<String> getHashtags() { return hashtags; }
	public void setHashtags(List<String> hashtags) { this.hashtags = hashtags; }
	
	@DynamoDBAttribute(attributeName="Interval")
	public int getInterval() { return interval; }
	public void setInterval(int interval) { this.interval = interval; }
	
	@DynamoDBAttribute(attributeName="Owner")
	public String getOwner() { return owner; }
	public void setOwner(String owner) { this.owner = owner; }
	
	public void addHashtag(String hashtag) {
		hashtags.add(hashtag);
	}
	
	public String toString(){
		String str = "ID: " + this.getId() + "Hashtags[";
		for(String hashtag : this.hashtags){
			str += hashtag + ",";
		}
		str += "] Interval: " + this.interval + " seconds.";
		return str;
	}

	
}
