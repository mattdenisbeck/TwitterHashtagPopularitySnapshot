package edu.nku.csc540.hashtag.repository;

import java.util.List;
import java.util.Optional;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import edu.nku.csc540.hashtag.model.Snapshot;
import edu.nku.csc540.hashtag.model.User;

public class SnapshotRepository {
	
	AmazonDynamoDBClient dynamodb;
	DynamoDBMapper mapper;
	
	public SnapshotRepository(AmazonDynamoDBClient dynamodb){
		this.dynamodb = dynamodb;
		this.mapper = new DynamoDBMapper(dynamodb);
	}
	
	public void save(Snapshot snapshot){
		mapper.save(snapshot);
	}
	
	public Snapshot find(String id){
		Snapshot snapshot = mapper.load(Snapshot.class, id);
		return snapshot;
	}
	
	public List<Snapshot> searchByOwner(String owner){
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		scanExpression.addFilterCondition("Owner", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(owner)));
		scanExpression.addFilterCondition("id", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.NE)
                    .withAttributeValueList(new AttributeValue().withS("temp")));
      
		List<Snapshot> result = mapper.scan(Snapshot.class, scanExpression);
		
		return result;
	}

}
