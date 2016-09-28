package edu.nku.csc540.hashtag.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import edu.nku.csc540.hashtag.model.PopularityDatapoint;
import edu.nku.csc540.hashtag.model.User;

public class DataPointRepository {
	
	AmazonDynamoDBClient dynamodb;
	DynamoDBMapper mapper;
	
	public DataPointRepository(AmazonDynamoDBClient dynamodb){
		this.dynamodb = dynamodb;
		this.mapper = new DynamoDBMapper(dynamodb);
	}
	
	public void save(PopularityDatapoint datapoint){
		mapper.save(datapoint);
	}
	
	public PopularityDatapoint find(String id){
		PopularityDatapoint favs = mapper.load(PopularityDatapoint.class, id);
		return favs;
	}

	public List<PopularityDatapoint> getLatest(String snapshot_id, LocalDateTime lastQuery) {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		scanExpression.addFilterCondition("Snapshot_id", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(snapshot_id)));
		scanExpression.addFilterCondition("Timestamp", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.GT)
                    .withAttributeValueList(new AttributeValue().withS(lastQuery.toString())));
		
		if(null != mapper.scan(PopularityDatapoint.class, scanExpression)){
			return mapper.scan(PopularityDatapoint.class, scanExpression);
		}
		else {
			return null;		
		}
	}
}
