package org.craneprint.craneserver.gcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CraneCodeExtractor {
	private File file;
	
	public CraneCodeExtractor(File f) {
		file = f;
	}
	
	public GCodeFile getMyGCodeFile() throws IOException {
		GCodeFile gcf;
		JSONObject j = readMetaFile();
		gcf = new GCodeFile(file, (String)j.get("name"), (String)j.get("notes"), (String)j.get("user"));
		return gcf;
	}
	
	private JSONObject readMetaFile() throws IOException {
		JSONObject jsonObject = null;
		try {
			// read the json file
			FileReader reader = new FileReader(file.getPath() + ".meta");

			JSONParser jsonParser = new JSONParser();
			jsonObject = (JSONObject) jsonParser.parse(reader);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
		return jsonObject;
	}
	/*
	 * Not really rqd...
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
