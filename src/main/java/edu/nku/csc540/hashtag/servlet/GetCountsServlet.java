package edu.nku.csc540.hashtag.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.TwitterStreamFactory;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.nku.csc540.hashtag.model.DatapointToJsonAdapter;
import edu.nku.csc540.hashtag.model.PopularityDatapoint;
import edu.nku.csc540.hashtag.model.Snapshot;
import edu.nku.csc540.hashtag.repository.DataPointRepository;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/hashtag-snapshot/GetCounts", "/GetCounts"})
public class GetCountsServlet extends HttpServlet{
	private transient ObjectMapper JSON = new ObjectMapper();
	LocalDateTime lastQuery;
	List<PopularityDatapoint> datapoints;
	DataPointRepository datapointRepo;
	
	@Override
	public void init(){
		lastQuery = LocalDateTime.now().minusSeconds(20); //account for missed datapoints
		
        AmazonDynamoDBClient dynamoDB = new AmazonDynamoDBClient();
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        dynamoDB.setRegion(usEast1);
		datapointRepo = new DataPointRepository(dynamoDB);
	}
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Snapshot snapshot = (Snapshot) req.getSession().getAttribute("snapshot");
		String snapshot_id = snapshot.getId();
		
		while(null == datapointRepo.getLatest(snapshot_id, lastQuery) || datapointRepo.getLatest(snapshot_id, lastQuery).isEmpty()){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		datapoints = datapointRepo.getLatest(snapshot.getId(), lastQuery);
		List<DatapointToJsonAdapter> data = new ArrayList<DatapointToJsonAdapter>();
		
		datapoints.forEach(e -> {
			data.add(new DatapointToJsonAdapter(e.getTimestamp(), e.getHashtagCounts()));
		});
		lastQuery = LocalDateTime.now();
		
		// Return the counts as JSON
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        JSON.writeValue(resp.getWriter(), data);
	}
}
