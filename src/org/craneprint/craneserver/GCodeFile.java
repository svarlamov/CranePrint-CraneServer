package org.craneprint.craneserver;

import java.io.File;

public class GCodeFile {
	// An object to represent uploaded gcode files
	private File file = null;
	private String name = "";
	private String user = "";
	private String notes = "";
	private long printId;
	private boolean complete = false;
	
	public GCodeFile(File f, String n, String ns, String u){
		file = f;
		name = n;
		user = u;
		notes = ns;
	}
	
	public GCodeFile(File f, String n, String ns, String u, long id){
		file = f;
		name = n;
		user = u;
		printId = id;
		notes = ns;
	}
	
	// By using this method we put a lot of trust in them setting up the notes before it is sent 
	public GCodeFile(File f, String n, String u){
		file = f;
		name = n;
		user = u;
	}
	
	public long getId(){
		return printId;
	}
	public boolean deleteFile(){
		return file.delete();
	}
	
	public void setNotes(String ns){
		notes = ns;
	}
	
	public String getNotes(){
		return notes;
	}
	
	public File getFile(){
		return file;
	}
	
	public String getName(){
		return name;
	}
	
	public String getUser(){
		return user;
	}
	
	public static long calculateFilamentUsage(GCodeFile f){
		//TODO: Actually calculate instead of simply returning a test value
		return 900;
	}
}
