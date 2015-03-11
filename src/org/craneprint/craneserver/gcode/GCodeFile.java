package org.craneprint.craneserver.gcode;

import java.io.File;

import javax.servlet.ServletContext;

import org.craneprint.craneserver.db.DBManager;

public class GCodeFile {
	// An object to represent uploaded gcode files
	private File file = null;
	private String name = "";
	private String user = "";
	private String notes = "";
	private DBManager db;
	private long printId;
	private boolean deleted = false;
	
	public GCodeFile(DBManager dbManager, File f, String n, String ns, String u, long id){
		db = dbManager;
		file = f;
		name = n;
		user = u;
		printId = id;
		notes = ns;
	}
	
	public GCodeFile(DBManager dbManager, File f, String n, String ns, String u){
		db = dbManager;
		file = f;
		name = n;
		notes = ns;
		user = u;
	}
	
	public long getId(){
		return printId;
	}
	public boolean deleteFile(){
		if(!db.isFileNeeded(this)){
			deleted = file.delete();
			File meta = new File(file.getPath() + ".meta");
			if(meta.exists())
				deleted = meta.delete();
			return deleted;
		} else {
			deleted = false;
			return false;
		}
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

	public void setId(long id) {
		printId = id;		
	}
}
