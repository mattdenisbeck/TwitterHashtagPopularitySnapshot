package edu.nku.csc540.hashtag.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import edu.nku.csc540.hashtag.model.Favorites;

public class FavoritesRepository {
	
	AmazonDynamoDBClient dynamodb;
	DynamoDBMapper mapper;
	
	public FavoritesRepository(AmazonDynamoDBClient dynamodb){
		this.dynamodb = dynamodb;
		this.mapper = new DynamoDBMapper(dynamodb);
	}
	
	public void save(Favorites favs){
		mapper.save(favs);
	}
	
	public Favorites find(String owner){
		Favorites favs = mapper.load(Favorites.class, owner);
		return favs;
	}

}
