package edu.nku.csc540.hashtag.testing;

import java.time.LocalDateTime;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class HashtagAppDataPointDBTest {
	private static AmazonDynamoDBClient dynamoDB;
	static String snapshot_id = "0ca06edf-f3ab-4d5d-b154-76376db5ae00";
	static LocalDateTime lastQuery;
	
	public static void main(String[] args) throws InterruptedException {
		init();
		
//		DataPointRepository repo = new DataPointRepository(dynamoDB);
//		List<PopularityDatapoint> datapoints;
//		while(true){
//			lastQuery = LocalDateTime.now();
//			while(repo.getLatest(snapshot_id, lastQuery).isEmpty()){
//				Thread.sleep(3000);
//			}
//			datapoints = repo.getLatest(snapshot_id, lastQuery);
//			datapoints.forEach(value -> System.out.println(value.toString()));
//		}
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

}
