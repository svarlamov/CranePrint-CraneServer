package org.craneprint.craneserver.gcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;

public class CraneCodePacker {
	private GCodeFile gcf;
	private JSONObject obj;
	
	public CraneCodePacker(GCodeFile g) {
		gcf = g;
		obj = new JSONObject();
	    obj.put("notes", gcf.getNotes());
	    obj.put("user", gcf.getUser());
	    obj.put("name", gcf.getName());
	}
	
	public void pack() throws IOException {
		writeFile(obj);
	}
	
	private void writeFile(JSONObject j) throws IOException{
		File f = new File(gcf.getFile().getPath() + ".meta");
		f.createNewFile();
		FileWriter file = new FileWriter(gcf.getFile().getPath() + ".meta");
        try {
            file.write(j.toJSONString());
 
        } catch (IOException e) {
            e.printStackTrace();
 
        } finally {
            file.flush();
            file.close();
        }
	}
	/*
	 * Not Really Reqd Here
	private String readFile(File file) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String line = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    String ls = System.getProperty("line.separator");
	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }
	    return stringBuilder.toString();
	}*/
}
