package edu.nku.csc540.hashtag.stream;

import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;

import edu.nku.csc540.hashtag.utilities.ConfigurationUtils;
import edu.nku.csc540.hashtag.utilities.CredentialUtils;

public class KinesisConsumerThread implements Runnable {

	@Override
	public void run() {
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);

        AmazonKinesis kinesisClient = new AmazonKinesisClient();
        kinesisClient.setRegion(usEast1);
        String streamName = "hashTagAppKinesis";
        AWSCredentialsProvider credentialsProvider = null;
		try {
			credentialsProvider = CredentialUtils.getCredentialsProvider();
		} catch (Exception e) {
			e.printStackTrace();
		}
        String workerId = String.valueOf(UUID.randomUUID());
        KinesisClientLibConfiguration kclConfig =
                new KinesisClientLibConfiguration("hashtagPopularityApp", streamName, credentialsProvider, workerId)
            .withRegionName(usEast1.getName())
            .withCommonClientConfig(ConfigurationUtils.getClientConfigWithUserAgent());

        IRecordProcessorFactory recordProcessorFactory = new PopularityDatapointProcessorFactory();

        // Create the KCL worker with the stock trade record processor factory
        Worker worker = new Worker(recordProcessorFactory, kclConfig);

        int exitCode = 0;
        try {
            worker.run();
        } catch (Throwable t) {
            System.out.println("Caught throwable while processing data.");
            t.printStackTrace();
            exitCode = 1;
        }
        System.exit(exitCode);
	}
}
