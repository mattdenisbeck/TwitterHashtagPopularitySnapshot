package edu.nku.csc540.hashtag.servlet;

import java.io.IOException;
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
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import edu.nku.csc540.hashtag.listener.DBSetupListener;
import edu.nku.csc540.hashtag.model.Favorites;
import edu.nku.csc540.hashtag.model.User;
import edu.nku.csc540.hashtag.repository.FavoritesRepository;
import edu.nku.csc540.hashtag.repository.UserRepository;

@SuppressWarnings("serial")
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AmazonDynamoDBClient dynamoDB = new AmazonDynamoDBClient();
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        dynamoDB.setRegion(usEast1);
        UserRepository userRepo = new UserRepository(dynamoDB);
        //UserRepository repository = (UserRepository) this.getServletContext().getAttribute(DBSetupListener.USER_REPOSITORY_KEY);
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Optional<User> user = userRepo.find(username, password);
        HttpSession httpSession = request.getSession();
        
        //check if the user was trying to go to a login protected page, set redirect destination accordingly
        String dest = "snapshot";
        if(null != httpSession.getAttribute("dest")){
        	dest = (String) httpSession.getAttribute("dest");
        }
        if (user.isPresent()) {
        	httpSession.setAttribute("user", user.get());
            response.sendRedirect(dest);
        }
        else {
        	response.sendRedirect("register");
        }
    }
}