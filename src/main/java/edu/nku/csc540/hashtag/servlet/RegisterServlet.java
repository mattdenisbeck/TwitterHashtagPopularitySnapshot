package edu.nku.csc540.hashtag.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import edu.nku.csc540.hashtag.listener.DBSetupListener;
import edu.nku.csc540.hashtag.model.Favorites;
import edu.nku.csc540.hashtag.model.User;
import edu.nku.csc540.hashtag.repository.FavoritesRepository;
import edu.nku.csc540.hashtag.repository.UserRepository;

@SuppressWarnings("serial")
@WebServlet("/register")
public class RegisterServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/register.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        AmazonDynamoDBClient dynamoDB = new AmazonDynamoDBClient();
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        dynamoDB.setRegion(usEast1);
        UserRepository userRepo = new UserRepository(dynamoDB);
        FavoritesRepository favsRepo = new FavoritesRepository(dynamoDB);

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String emailAddress = request.getParameter("emailAddress");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        User newUser = new User();
		newUser.setEmail(emailAddress);
		newUser.setfName(firstName);
		newUser.setlName(lastName);
		newUser.setPassword(password);
		newUser.setUsername(username);
        userRepo.save(newUser);
        Optional<User> user = userRepo.find(username, password);
        
        Favorites favs = new Favorites(new ArrayList<String>(), user.get().getId());
        favsRepo.save(favs);
        response.sendRedirect("login");
    }
}