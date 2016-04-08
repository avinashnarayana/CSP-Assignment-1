package com.example.assignment1;

import java.io.IOException;

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
public class AnagramServlet extends HttpServlet {
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
		
			pm = PMF.get().getPersistenceManager();
			
		
		// attach a few things to the request such that we can access them in the jsp 
		req.setAttribute("user", u); 
		req.setAttribute("login_url", login_url); 
		req.setAttribute("logout_url", logout_url);
		
		
		//check whether anything was input by user
		String search = req.getParameter("check_anagram");
		boolean validWord = true;
		if(search==null||search.equals("")){
			req.setAttribute("errMsg", "Input string is empty");
			validWord = false;
		}
		// get access to a request dispatcher and forward onto the root.jsp file
		int[] primes = {2,3,5,7,11,
				13,17,19,23,29,
				31,37,41,43,47,
				53,59,61,67,71,
				73,79,83,89,97,101};
		// Associating each letter to a prime number, a unique integer can be generated
		// for a particular letter combination
		long uniqueID = 1;
		try{// surrounding with try catch to deal with index out of bounds exception
			// when a white space of other character apart from alphabet is entered
		for(int i=0;i<search.length();i++){
			int ascii=search.charAt(i);
			if(ascii>96)ascii-=96;
			else ascii-=64;
			uniqueID*=primes[ascii-1];
		}}catch(Exception e){req.setAttribute("errMsg", "Input word is invalid");validWord=false;}
		if(validWord){
			Key word_key = KeyFactory.createKey("WordList", uniqueID);
			try { 
				WordList resultList = pm.getObjectById(WordList.class, word_key);
				
				String displayMsg = "Anagrams found for the input word are: ";
				for(int j=0;j<resultList.wordcount();j++)displayMsg+="<br/>"+resultList.getWord(j);
				if(!resultList.contains(search)){
					resultList.addWord(search);
					displayMsg+="<br/>Added the word '"+search+"' to the dictionary as well";
				}
				req.setAttribute("displayMsg", displayMsg);
			} catch(Exception e) {
				WordList w = new WordList(word_key);
				w.addWord(search);
				pm.makePersistent(w);
				req.setAttribute("errMsg", "No anagrams found");
				req.setAttribute("displayMsg", search+" was added to dictionary");
			}
		}
		pm.close(); 
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp"); 
		rd.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
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
		pm.close();*/ 
		resp.sendRedirect("/");
	}
}
