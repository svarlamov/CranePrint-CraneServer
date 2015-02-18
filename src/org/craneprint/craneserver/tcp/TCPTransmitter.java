package org.craneprint.craneserver.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.craneprint.craneserver.gcode.GCodeFile;
import org.craneprint.craneserver.ui.HandShake;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class TCPTransmitter {
	private final String ip;
	//private final int printerId;
	private final int chickPort;
	private ServerSocket welcomeSocket;
	
	public TCPTransmitter(String i, int chp){
		// TODO: Actually Set these values
		//printerId = id;
		ip = i;
		chickPort = chp;
	}
	
	public HandShake sendHandShake() throws IOException, ParseException{
		JSONObject obj = new JSONObject();
	    obj.put("type", RequestType.HAND_SHAKE_CODE);
	    obj.put("password", "password");
	    String resp = sendCommand(obj.toJSONString());
	    return new HandShake(resp);
	}
	
	public boolean sendFile(GCodeFile gcf) throws IOException, ParseException{
		JSONObject obj = new JSONObject();
	    obj.put("type", RequestType.FILE_CODE);
	    obj.put("file", readFile(gcf.getFile()));
	    obj.put("notes", gcf.getNotes());
	    obj.put("id", gcf.getId());
	    obj.put("name", gcf.getName());
	    // TODO: Dynamically Add the following credentials
	    obj.put("user", "testUser");
	    obj.put("password", "password");
	    String resp = sendCommand(obj.toJSONString());
	    return true;
	}
	
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
	}
	
	public String sendCommand(String toSend) throws IOException{
		String resp;
		System.out.println("CHICK PORT: " + chickPort);
		Socket clientSocket = new Socket(ip, chickPort);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes(toSend + "\n");
		resp = inFromServer.readLine();
		System.out.println("FROM AGENT: " + resp);
		clientSocket.close();
		return resp;
	}
	
}
