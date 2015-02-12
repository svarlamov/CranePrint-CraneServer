package org.craneprint.craneserver.tcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.craneprint.craneserver.GCodeFile;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class SendFile {
	public static boolean sendFile(GCodeFile gcf) throws IOException, ParseException{
		JSONObject obj = new JSONObject();
	    obj.put("type", RequestType.FILE_CODE);
	    obj.put("file", readFile(gcf.getFile()));
	    obj.put("notes", gcf.getNotes());
	    obj.put("id", gcf.getId());
	    obj.put("name", gcf.getName());
	    // TODO: Dynamically Add the following credentials
	    obj.put("user", "testUser");
	    obj.put("password", "password");
	    String resp = TCPThread.sendCommand(obj.toJSONString());
	    return true;
	}
	private static String readFile(File file) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String line = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    String ls = System.getProperty("line.separator");
	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }
	    return stringBuilder.toString();
	}
}
