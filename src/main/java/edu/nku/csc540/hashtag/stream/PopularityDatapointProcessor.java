package edu.nku.csc540.hashtag.stream;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ThrottlingException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownReason;
import com.amazonaws.services.kinesis.model.Record;

import edu.nku.csc540.hashtag.model.PopularityDatapoint;
import edu.nku.csc540.hashtag.repository.DataPointRepository;

public class PopularityDatapointProcessor implements IRecordProcessor {

    private String kinesisShardId;
    private DataPointRepository datapointRepo;
    private AmazonDynamoDBClient dynamoDB;

    @Override
    public void initialize(String shardId) {
        dynamoDB = new AmazonDynamoDBClient();
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        dynamoDB.setRegion(usEast1);
        this.kinesisShardId = shardId;
        this.datapointRepo = new DataPointRepository(dynamoDB);
    }

    @Override
    public void processRecords(List<Record> records, IRecordProcessorCheckpointer checkpointer) {
        for (Record record : records) {
            // process record
            processRecord(record);
        }
    }

    private void processRecord(Record record) {
        PopularityDatapoint dataPoint = PopularityDatapoint.fromJsonAsBytes(record.getData().array());
        if (dataPoint == null) {
            System.out.println("Skipping record. Unable to parse record into PopularityDataPoint. Partition Key: " + record.getPartitionKey());
            return;
        }
        System.out.println(dataPoint.toString());
        datapointRepo.save(dataPoint);
    }

    @Override
    public void shutdown(IRecordProcessorCheckpointer checkpointer, ShutdownReason reason) {
    	System.out.println("Shutting down record processor for shard: " + kinesisShardId);
        // Important to checkpoint after reaching end of shard, so we can start processing data from child shards.
        if (reason == ShutdownReason.TERMINATE) {
            checkpoint(checkpointer);
        }
    }

    private void checkpoint(IRecordProcessorCheckpointer checkpointer) {
    	System.out.println("Checkpointing shard " + kinesisShardId);
        try {
            checkpointer.checkpoint();
        } catch (ShutdownException se) {
            // Ignore checkpoint if the processor instance has been shutdown (fail over).
        	System.out.println("Caught shutdown exception, skipping checkpoint.");
        	se.printStackTrace();
        } catch (ThrottlingException e) {
            // Skip checkpoint when throttled. In practice, consider a backoff and retry policy.
        	System.out.println("Caught throttling exception, skipping checkpoint.");
        	e.printStackTrace();
        } catch (InvalidStateException e) {
            // This indicates an issue with the DynamoDB table (check for table, provisioned IOPS).
        	System.out.println("Cannot save checkpoint to the DynamoDB table used by the Amazon Kinesis Client Library.");
        	e.printStackTrace();
        }
    }

}
