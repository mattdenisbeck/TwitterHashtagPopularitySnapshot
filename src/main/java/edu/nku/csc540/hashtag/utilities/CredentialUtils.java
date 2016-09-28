package edu.nku.csc540.hashtag.utilities;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;


public class CredentialUtils {

    public static AWSCredentialsProvider getCredentialsProvider() throws Exception {
       
        AWSCredentialsProvider credentialsProvider = null;
        try {
        	credentialsProvider = new DefaultAWSCredentialsProviderChain();
            //credentialsProvider = new ProfileCredentialsProvider("default");
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        return credentialsProvider;
    }

}
