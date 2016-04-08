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
		RootServlet.userLogin(req,resp);
		
		//check whether anything was input by user
		String search = req.getParameter("check_anagram");
		boolean validWord = true;
		if(search==null||search.equals("")){
			req.setAttribute("errMsg", "Input string is empty");
			validWord = false;
		}
		
		// Associating each letter to a prime number, a unique integer can be generated
		// for a particular letter combination by multiplying all the primes together.
		// We calculate that unique ID for the search string here
		int[] primes = {2,3,5,7,11,
				13,17,19,23,29,
				31,37,41,43,47,
				53,59,61,67,71,
				73,79,83,89,97,101};
		
		long uniqueID = 1;
		
		try{// surrounding with try catch to deal with index out of bounds exception
			// when a non-alphabet character is entered
			for(int i=0;i<search.length();i++){
				int ascii=search.charAt(i);
				// converting the ascii of the character into an index of 0-25
				if(ascii>96)ascii-=97; //for lower-case
				else ascii-=65; // for upper-cased letters
				uniqueID*=primes[ascii];
		}}catch(IndexOutOfBoundsException e){
			req.setAttribute("errMsg", "Input word is invalid");
			validWord=false;
		}
		
		// Process it if it is a valid word
		if(validWord){
			Key word_key = KeyFactory.createKey("WordList", uniqueID);
			PersistenceManager pm = PMF.get().getPersistenceManager();
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
			pm.close(); 
		}
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp"); 
		rd.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RootServlet.userLogin(req,resp);
		//check whether anything was input by user
		String search = req.getParameter("submit_word");
		boolean validWord = true;
		if(search==null||search.equals("")){
			req.setAttribute("errMsg", "Input string is empty");
			validWord = false;
		}
		
		// Associating each letter to a prime number, a unique integer can be generated
		// for a particular letter combination by multiplying all the primes together.
		// We calculate that unique ID for the search string here
		int[] primes = {2,3,5,7,11,
				13,17,19,23,29,
				31,37,41,43,47,
				53,59,61,67,71,
				73,79,83,89,97,101};
		
		long uniqueID = 1;
		try{// surrounding with try catch to deal with index out of bounds exception
			// when a non-alphabet character is entered
			for(int i=0;i<search.length();i++){
				int ascii=search.charAt(i);
				// converting the ascii of the character into an index of 0-25
				if(ascii>96)ascii-=97; //for lower-case
				else ascii-=65; // for upper-cased letters
				uniqueID*=primes[ascii];
		}}catch(IndexOutOfBoundsException e){
			req.setAttribute("errMsg", "Input word is invalid");
			validWord=false;
		}
		
		// Process it if it is a valid word
		if(validWord){
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Key word_key = KeyFactory.createKey("WordList", uniqueID);
			try { 
				WordList resultList = pm.getObjectById(WordList.class, word_key);
				
				if(!resultList.contains(search)){
					resultList.addWord(search);
					req.setAttribute("displayMsg", search+" was added to dictionary");
				}
				else
					req.setAttribute("displayMsg", search+" already in dictionary");
			} catch(Exception e) {
				WordList w = new WordList(word_key);
				w.addWord(search);
				pm.makePersistent(w);
				req.setAttribute("displayMsg", search+" was added to dictionary");
			}
			pm.close();
		}
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp"); 
		rd.forward(req, resp);
	}
}
