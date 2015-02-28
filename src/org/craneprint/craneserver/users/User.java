package org.craneprint.craneserver.users;

public class User {
	private String username;
	private Permissions permissions = null;
	private boolean loggedIn = false;
	
	public User(String uname, boolean isIn){
		username = uname;
		loggedIn = isIn;
	}
	
	public void initPerms(){
		permissions = new Permissions(username);
	}
	
	public boolean hasPermission(String perm){
		if(permissions.hasPermission(perm))
			return true;
		else
			return false;
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
