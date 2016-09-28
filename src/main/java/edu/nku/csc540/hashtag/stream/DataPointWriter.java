package edu.nku.csc540.hashtag.stream;

import java.nio.ByteBuffer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.model.PutRecordRequest;

import edu.nku.csc540.hashtag.model.PopularityDatapoint;

public class DataPointWriter {
	
	private AWSCredentials credentials;
	private Region usEast1;
	private AmazonKinesis kinesisClient;
	private String streamName;
	
	public DataPointWriter(){
        usEast1 = Region.getRegion(Regions.US_EAST_1);

        kinesisClient = new AmazonKinesisClient();
        kinesisClient.setRegion(usEast1);
        streamName = "hashTagAppKinesis";
	}

	public void sendToKinesis(PopularityDatapoint dataPoint) {
		byte[] bytes = dataPoint.toJsonAsBytes();
        // The bytes could be null if there is an issue with the JSON serialization by the Jackson JSON library.
        if (bytes == null) {
            System.out.println("Could not get JSON bytes for stock trade");
            return;
        }

        PutRecordRequest putRecord = new PutRecordRequest();
        putRecord.setStreamName(streamName);
        putRecord.setPartitionKey(dataPoint.toString());
        putRecord.setData(ByteBuffer.wrap(bytes));

        try {
            kinesisClient.putRecord(putRecord);
            System.out.println("Putting record...");
        } catch (AmazonClientException ex) {
            ex.printStackTrace();
        }
		
	}

}
