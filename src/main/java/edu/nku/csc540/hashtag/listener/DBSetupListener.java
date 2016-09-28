package edu.nku.csc540.hashtag.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import edu.nku.csc540.hashtag.repository.FavoritesRepository;
import edu.nku.csc540.hashtag.repository.UserRepository;

public class DBSetupListener implements ServletContextListener {
	
	public static final String USER_REPOSITORY_KEY = "userRepo";
	public static final String FAVS_REPOSITORY_KEY = "favsRepo";
	public static final String DYNAMO_DB_KEY = "dynamoDB";
	static AmazonDynamoDBClient dynamoDB;
	

	@Override
	public void contextInitialized(ServletContextEvent sce) {
        dynamoDB = new AmazonDynamoDBClient();
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        dynamoDB.setRegion(usEast1);
        
        sce.getServletContext().setAttribute(USER_REPOSITORY_KEY, new UserRepository(DBSetupListener.dynamoDB));
        sce.getServletContext().setAttribute(FAVS_REPOSITORY_KEY, new FavoritesRepository(DBSetupListener.dynamoDB));
        sce.getServletContext().setAttribute(DYNAMO_DB_KEY, DBSetupListener.dynamoDB);

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
