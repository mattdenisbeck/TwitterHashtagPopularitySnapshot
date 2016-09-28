package edu.nku.csc540.hashtag.model;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "hashtagAppFavsTable")
public class Favorites {
	private List<String> snapshot_ids;
	private String owner;
	
	public Favorites(List<String> snapshot_ids, String owner){
		this.setSnapshot_ids(snapshot_ids);
		this.owner = owner;
	}
	
	public Favorites(){}
	
	@DynamoDBHashKey
	public String getOwner() { return owner; }
	public void setOwner(String owner) { this.owner = owner;}
	
	@DynamoDBAttribute(attributeName="Snapshot_ids")
	public List<String> getSnapshot_ids() { return snapshot_ids; }
	public void setSnapshot_ids(List<String> snapshot_ids) { this.snapshot_ids = snapshot_ids; }
	
	@Override
	public String toString(){
		String str = this.owner + ": ";
		for(String snapshot_id : this.snapshot_ids){
			str += snapshot_id;
			str += " ";
		}
		return str;
	}

	public void addSnapshot(String snapshot_id) {
		this.snapshot_ids.add(snapshot_id);
	}
}
