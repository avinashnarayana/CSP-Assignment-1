package com.example.assignment1;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

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
			pm = PMF.get().getPersistenceManager();
			pm.close(); 
		}
		// attach a few things to the request such that we can access them in the jsp 
		req.setAttribute("user", u); 
		req.setAttribute("login_url", login_url); 
		req.setAttribute("logout_url", logout_url);
		// get access to a request dispatcher and forward onto the root.jsp file 
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp"); 
		rd.forward(req, resp);
	}
}
