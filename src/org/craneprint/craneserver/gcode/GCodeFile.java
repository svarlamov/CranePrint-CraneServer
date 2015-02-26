package org.craneprint.craneserver.gcode;

import java.io.File;

public class GCodeFile {
	// An object to represent uploaded gcode files
	private File file = null;
	private String name = "";
	private String user = "";
	private String notes = "";
	private long printId;
	private boolean deleted = false;
	
	public GCodeFile(File f, String n, String ns, String u, long id){
		file = f;
		name = n;
		user = u;
		printId = id;
		notes = ns;
	}
	
	public GCodeFile(File f, String n, String ns, String u){
		file = f;
		name = n;
		notes = ns;
		user = u;
	}
	
	public long getId(){
		return printId;
	}
	public boolean deleteFile(){
		boolean success = true;
		success = file.delete();
		File meta = new File(file.getPath() + ".meta");
		if(meta.exists())
			success = meta.delete();
		deleted = success;
		return success;
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
	
	public boolean isDeleted(){
		return deleted;
	}
	
	public static long calculateFilamentUsage(GCodeFile f){
		//TODO: Actually calculate instead of simply returning a test value
		return 900;
	}
}
