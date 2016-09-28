package edu.nku.csc540.hashtag.repository;

import java.util.List;
import java.util.Optional;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import edu.nku.csc540.hashtag.model.User;

public class UserRepository {
	
	AmazonDynamoDBClient dynamodb;
	DynamoDBMapper mapper;
	
	public UserRepository(AmazonDynamoDBClient dynamodb){
		this.dynamodb = dynamodb;
		this.mapper = new DynamoDBMapper(dynamodb);
	}
	
	public void save(User user){
		mapper.save(user);
	}
	
	public Optional<User> find(String username, String password){
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		scanExpression.addFilterCondition("Username", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(username)));
		scanExpression.addFilterCondition("Password", 
                new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(password)));
      
		List<User> result = mapper.scan(User.class, scanExpression);
		
		return Optional.ofNullable(result.get(0));
	}

}
