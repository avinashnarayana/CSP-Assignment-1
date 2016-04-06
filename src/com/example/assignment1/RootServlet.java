package com.example.assignment1;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class RootServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		resp.setContentType("text/plain");
		// we need to get access to the google user service 
		UserService us = UserServiceFactory.getUserService(); 
		User u = us.getCurrentUser(); 
		String login_url = us.createLoginURL("/"); 
		String logout_url = us.createLogoutURL("/");
		// persistence manager and a user we declare them like this because 
		// each time we close we need a new persistence manager for the following 
		// query 
		PersistenceManager pm = null; 
		// get access to the user. if they do not exist in the datastore then 
		// store a default version of them. of course we have to check that a user has 
		// logged in first 
		if(u != null) { 
			Key user_key = KeyFactory.createKey("User", u.getUserId()); 
			pm = PMF.get().getPersistenceManager();
			pm.close(); 
		}
		
		// attach a few things to the request such that we can access them in the jsp 
		req.setAttribute("user", u); 
		req.setAttribute("login_url", login_url); 
		req.setAttribute("logout_url", logout_url);
		// Uncomment the next line to check if the errMsg is working on the root page as expected
		// req.setAttribute("errMsg", "Hey the error message box seems to work");
		// get access to a request dispatcher and forward onto the root.jsp file 
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp"); 
		rd.forward(req, resp);
	}
	/*
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// get the new timezone from the form 
		int timezone = 0; 
		try{ 
			timezone = Integer.parseInt(req.getParameter("new_timezone")); 
		} catch(NumberFormatException e) { 
			// failed to update so just redirect to / 
			resp.sendRedirect("/"); 
		}
		
		// get access to the user service to get our user 
		UserService us = UserServiceFactory.getUserService(); 
		User u = us.getCurrentUser();
		// update the timezone in the datastore and then redirect to / 
		PersistenceManager pm = null; 
		ClockUser settings; 
		Key user_key = KeyFactory.createKey("ClockUser", u.getUserId()); 
		try { 
			pm = PMF.get().getPersistenceManager(); 
			settings = pm.getObjectById(ClockUser.class, user_key); 
			settings.setOffset(timezone); pm.makePersistent(settings); 
		} catch (Exception e) { 
			// will only fail if the datastore goes down as this is 
			// already in the datastore 
		}
		pm.close(); 
		resp.sendRedirect("/");
	}*/
}
