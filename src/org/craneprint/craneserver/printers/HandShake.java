package org.craneprint.craneserver.printers;

import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HandShake {
	private int status = PrinterStatus.NO_DATA_CODE;
	private String notes = "";
	private HashMap<String, String> tools = new HashMap<String, String>();
	
	public HandShake(String toParse) throws ParseException{
		JSONParser parser = new JSONParser();
		JSONObject jo = (JSONObject) parser.parse(toParse);
		
		// Get "tools" structure
		JSONObject toolsStruct = (JSONObject) jo.get("tools");
		Set toolKeys = toolsStruct.keySet();
		for(int i = 0; i < toolKeys.size(); i++){
			tools.put((String)toolKeys.toArray()[i], (String)toolsStruct.get(toolKeys.toArray()[i]));
		}
		// Get Status
		status = (int)(long)jo.get("status");
		// Get Notes
		notes = (String)jo.get("notes");
	}
	
	public int getStatus(){
		return status;
	}
	
	public String getNotes(){
		return notes;
	}
	
	public HashMap<String, String> getTools(){
		return tools;
	}

}
