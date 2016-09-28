package edu.nku.csc540.hashtag.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

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
import edu.nku.csc540.hashtag.stream.HashtagProcessingThread;
import edu.nku.csc540.hashtag.stream.KinesisConsumerThread;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@WebServlet("/snapshot")
public class SnapshotServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AmazonDynamoDBClient dynamoDB = new AmazonDynamoDBClient();
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        dynamoDB.setRegion(usEast1);
        FavoritesRepository favsRepo = new FavoritesRepository(dynamoDB);
        SnapshotRepository snapshotRepo = new SnapshotRepository(dynamoDB);
       
    	HttpSession session = request.getSession();
    	User user = (User) session.getAttribute("user");
    	Favorites favorites = favsRepo.find(user.getId());
    	List<Snapshot> favs = new ArrayList<Snapshot>();
    	favorites.getSnapshot_ids().forEach(id -> favs.add(snapshotRepo.find(id)));
    	session.setAttribute("favorites", favs);
    	
    	//start up consumer thread... speeds up loading time to start this ahead of time
    	Thread consumer = new Thread(new KinesisConsumerThread());
		consumer.start();
        
    	RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/snapshot.jsp");
        dispatcher.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        AmazonDynamoDBClient dynamoDB = new AmazonDynamoDBClient();
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        dynamoDB.setRegion(usEast1);
        
        HttpSession session = request.getSession();
    	User user = (User) session.getAttribute("user");
        FavoritesRepository favsRepo = new FavoritesRepository(dynamoDB);
        SnapshotRepository snapshotRepo = new SnapshotRepository(dynamoDB);
        Snapshot snapshot;
        Favorites favs = favsRepo.find(user.getId());
        
        String button_val = request.getParameter("submit");
        
        if(button_val.equals("save") || button_val.equals("view")){
        	List<String> hashtags = new ArrayList<String>();
            for(int i = 0; i < 5; ++i){
            	if(null != request.getParameter("hashtag" + i)){
            		hashtags.add(request.getParameter("hashtag" + i));
            	}
            }
            int interval = Integer.parseInt(request.getParameter("interval"));
            
            
            if(button_val.equals("save")){
            	snapshot = new Snapshot(hashtags, interval, user.getId());
                snapshotRepo.save(snapshot);
            	List<Snapshot> my_snaps = snapshotRepo.searchByOwner(user.getId());
            	List<String> my_snap_ids = new ArrayList<String>();
            	my_snaps.forEach(snap -> my_snap_ids.add(snap.getId()));
            	favs.setSnapshot_ids(my_snap_ids);
                favsRepo.save(favs);
                response.sendRedirect("snapshot");
            }
            if(button_val.equals("view")){
            	snapshot = new Snapshot(hashtags, interval, user.getId(), "temp");
                snapshotRepo.save(snapshot);
                snapshot = snapshotRepo.find("temp");
            	session.setAttribute("snapshot", snapshot);
            	
            	//start up producer/consumer threads
            	Thread producer = new Thread(new HashtagProcessingThread(snapshot));
            	producer.start();
            	//Thread consumer = new Thread(new KinesisConsumerThread());
        		//consumer.start();
            	
            	//pause to let data load
        		try {
    				Thread.sleep(10000);
    			} catch (InterruptedException e) {e.printStackTrace();}
        		
                response.sendRedirect("graph");
            }
        }
        
        else {
        	String snapshot_id = favs.getSnapshot_ids().get(Integer.parseInt(button_val));
        	snapshot = snapshotRepo.find(snapshot_id);
        	session.setAttribute("snapshot", snapshot);
        	
        	//start up producer/consumer threads
        	Thread producer = new Thread(new HashtagProcessingThread(snapshot));
        	producer.start();
        	//Thread consumer = new Thread(new KinesisConsumerThread());
    		//consumer.start();
    		
    		//pause to let data load
    		try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {e.printStackTrace();}
    		
        	response.sendRedirect("graph");
        }
        
    }
}
