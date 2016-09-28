package edu.nku.csc540.hashtag.testing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import edu.nku.csc540.hashtag.model.Favorites;
import edu.nku.csc540.hashtag.model.Snapshot;
import edu.nku.csc540.hashtag.model.User;
import edu.nku.csc540.hashtag.repository.FavoritesRepository;
import edu.nku.csc540.hashtag.repository.SnapshotRepository;
import edu.nku.csc540.hashtag.repository.UserRepository;
import edu.nku.csc540.hashtag.stream.HashtagCountGenerator;

public class HashtagAppTest {

	private static AmazonDynamoDBClient dynamoDB;

	public static void main(String[] args) {
		init();
//		testDB();
		testStream();	
	}

	private static void testStream() {
		HashtagCountGenerator hashtagGen = new HashtagCountGenerator();
		SnapshotRepository snapshotRepo = new SnapshotRepository(dynamoDB);
		UserRepository userRepo = new UserRepository(dynamoDB);
		Optional<User> me = userRepo.find("beckmatthew", "password");
		System.out.println(me.get().toString());
		Snapshot snapshot = snapshotRepo.searchByOwner(me.get().getId()).get(0);
		hashtagGen.readTwitterFeed(snapshot);
	}

	private static void init() {
		AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/Users/icart/.aws/credentials), and is in valid format.",
                    e);
        }
        dynamoDB = new AmazonDynamoDBClient(credentials);
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        dynamoDB.setRegion(usEast1);	
	}
	
	private static void testDB(){
		UserRepository userRepo = new UserRepository(dynamoDB);
		FavoritesRepository favsRepo = new FavoritesRepository(dynamoDB);
		SnapshotRepository snapshotRepo = new SnapshotRepository(dynamoDB);
		
//		User user = new User();
//		user.setEmail("mattdenisbeck@gmail.com");
//		user.setfName("Luke");
//		user.setlName("Skywalker");
//		user.setPassword("password");
//		user.setUsername("luke");
//		
//		userRepo.save(user);
//		System.out.println("User saved to dynamodb");
//		
		Optional<User> me = userRepo.find("beckmatthew", "password");
		System.out.println(me.get().toString());
		
		
		List<String> hashtags = new ArrayList<String>();
		hashtags.add("test1");
		hashtags.add("test2");
		hashtags.add("test3");
		Snapshot snapshot = new Snapshot();
		snapshot.setHashtags(hashtags);
		snapshot.setInterval(5);
		snapshot.setOwner(me.get().getId());
		snapshotRepo.save(snapshot);
		
		Favorites favs = favsRepo.find(me.get().getId());
		if(null != favs){ System.out.println(favs.toString()); }
		else{ 
			favs = new Favorites();
			favs.setOwner(me.get().getId());
		}
		List<Snapshot> mySnapshots = snapshotRepo.searchByOwner(me.get().getId());
		List<String> mySnapshot_ids = new ArrayList<String>();
		mySnapshots.forEach(snap -> mySnapshot_ids.add(snap.getId()));
		favs.setSnapshot_ids(mySnapshot_ids);
		favsRepo.save(favs);
		System.out.println("saved favs");
		Favorites favsDB = favsRepo.find(me.get().getId());
		System.out.println(favsDB.toString());
	}

}
