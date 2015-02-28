package org.craneprint.craneserver.users;

import java.util.ArrayList;

import org.craneprint.craneserver.db.DBManager;

import com.vaadin.server.VaadinServlet;

public class Permissions {
	private ArrayList<String> perms = new ArrayList<String>();
	DBManager db;
	
	public Permissions(String username){
		db = (DBManager)VaadinServlet.getCurrent().getServletContext().getAttribute("org.craneprint.craneserver.db.dbManager");
		perms = db.getPermsForUser(username);
	}
	
	public boolean hasPermission(String perm){
		if(perms.contains(perm))
			return true;
		else
			return false;
	}
}
