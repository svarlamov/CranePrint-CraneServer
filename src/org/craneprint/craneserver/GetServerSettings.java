package org.craneprint.craneserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public class GetServerSettings {
	// TODO: Some static methods to retrieve certain parameters, such as the printer ip, type, etc.
	// Path to the settings.json file
	//private static final String filePath = File.pathSeparator + "CraneServerConfig.json";
	private static final String filePath = "C:\\Users\\ckpcAdmin\\workspace\\CranePrint CraneServer\\CraneServerConfig.json";
	
	public static String getString(String name) {
		JSONObject jsonObject = parseFile();
		// get a String from the JSON object
		String s = (String) jsonObject.get(name);
		return s;
	}
	
	public static int getInt(String name){
		JSONObject jsonObject = parseFile();
		// get a number from the JSON object
		int i =  (int) jsonObject.get(name);
		return i;
	}
	
	public static long getLong(String name){
		JSONObject jsonObject = parseFile();
		// get a number from the JSON object
		long l =  (long) jsonObject.get(name);
		return l;
	}
	
	public static JSONArray getArray(String name){
		JSONObject jsonObject = parseFile();
		// get an array from the JSON object
		JSONArray array= (JSONArray) jsonObject.get(name);
		return array;
	}
	
	public static JSONObject getStructure(String name){
		JSONObject jsonObject = parseFile();
		// Get a structure from the json file
		JSONObject structure = (JSONObject) jsonObject.get(name);
		return structure;
	}
	
	private static JSONObject parseFile(){
		JSONObject jsonObject = null;
		try {
			// read the json file
			FileReader reader = new FileReader(filePath);

			JSONParser jsonParser = new JSONParser();
			jsonObject = (JSONObject) jsonParser.parse(reader);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			new Notification("Error Opening Config File<br/>",
                    ex.getMessage(),
                    Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
		} catch (IOException ex) {
			ex.printStackTrace();
			new Notification("Error Opening Config File<br/>",
                    ex.getMessage(),
                    Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
		} catch (ParseException ex) {
			ex.printStackTrace();
			new Notification("Error Opening Config File<br/>",
                    ex.getMessage(),
                    Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			new Notification("Error Opening Config File<br/>",
                    ex.getMessage(),
                    Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
		} finally{
			return jsonObject;
		}
	}
}
