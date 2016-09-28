package edu.nku.csc540.hashtag.testing;

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

import edu.nku.csc540.hashtag.stream.PopularityDatapointProcessorFactory;
import edu.nku.csc540.hashtag.utilities.ConfigurationUtils;
import edu.nku.csc540.hashtag.utilities.CredentialUtils;

public class HashtagAppConsumerTest {

	public static void main(String[] args) {
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
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);

        AmazonKinesis kinesisClient = new AmazonKinesisClient(credentials);
        kinesisClient.setRegion(usEast1);
        String streamName = "hashTagAppKinesis";
        AWSCredentialsProvider credentialsProvider = null;
		try {
			credentialsProvider = CredentialUtils.getCredentialsProvider();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
