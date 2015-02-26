package org.craneprint.craneserver.users;

public class Authenticator {
	private String username;
	private boolean loggedIn = false;
	
	public Authenticator(){
		super();
	}
	
	public boolean login(String u, String p){
		System.out.println("user: " + u + "has been authenticated with password: " + p);
		loggedIn = true;
		username = u;
		return true;
	}
	
	public boolean logout(){
		return true;
	}
	
	public String getUsername(){
		return username;
	}
	
	public boolean isLoggedIn(){
		return loggedIn;
	}
}
