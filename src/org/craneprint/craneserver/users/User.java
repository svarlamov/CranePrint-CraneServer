package org.craneprint.craneserver.users;

public class User {
	//private static final long serialVersionUID = 1475947405579420406L;
	private String username;
	private boolean loggedIn = false;
	
	public User(String uname){
		username = uname;
	}
	
	public User(String uname, boolean isIn){
		username = uname;
		loggedIn = isIn;
	}
	
	public void setLoggedIn(){
		loggedIn = true;
	}
	
	public void setLoggedOut(){
		loggedIn = false;
	}
	
	public boolean isLoggedIn(){
		return loggedIn;
	}
	
	public String getUsername(){
		return username;
	}
}
