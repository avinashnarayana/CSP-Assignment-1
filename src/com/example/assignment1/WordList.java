// WordList.java 
// 
// Simple demonstration of a JDO annotated class that will store a user key and 
// store a list of words in it
package com.example.assignment1;
//imports 
import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable; 
import javax.jdo.annotations.Persistent; 
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
// class definition 
@PersistenceCapable 
public class WordList { 
	// the identifier for the object as we will be using the user key for this 
	// we need to use a key object here 
	@PrimaryKey 
	@Persistent 
	private Key id;
	// the list of words
	@Persistent 
	private ArrayList<String> words;
	// anything below this line is for our convenience and will not be used by 
	// JDO
	// setter and getter for the id 
	public WordList(Key id){
		this.id=id; words = new ArrayList<>();
	}
	public int wordcount(){return words.size();}
	public String getWord(final int index){return words.get(index);}
	public void addWord(final String word){
		if(!words.contains(word))words.add(word);
	}
	public boolean contains(String word){return words.contains(word);}
}
