package org.craneprint.craneserver.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import org.craneprint.craneserver.gcode.GCodeFile;
import org.craneprint.craneserver.printers.HandShake;
import org.craneprint.craneserver.printers.PrinterStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class TCPTransmitter {
	private final String ip;
	//private final int printerId;
	private final int chickPort;
	private ServerSocket welcomeSocket;
	private HandShake lastHandShake = null;
	private long lastHandShakeT = -1;
	
	public TCPTransmitter(String i, int chp){
		// TODO: Actually Set these values
		//printerId = id;
		ip = i;
		chickPort = chp;
	}
	
	private HandShake sendHandShake() throws IOException, ParseException {
		JSONObject obj = new JSONObject();
	    obj.put("type", RequestType.HAND_SHAKE_CODE);
	    obj.put("password", "password");
	    String resp = sendCommand(obj.toJSONString());
	    return new HandShake(resp);
	}
	
	public HandShake getHandShake() throws IOException, ParseException {
		if (lastHandShake != null && new Date().getTime() - lastHandShakeT < 10000 && (lastHandShake.getStatus() == PrinterStatus.FAILED_TO_CONNECT_CODE || lastHandShake.getStatus() == PrinterStatus.UNKNOWN_ERROR_CODE || lastHandShake.getStatus() == PrinterStatus.NO_DATA_CODE || lastHandShake.getStatus() == PrinterStatus.FAILED_TO_AUTHENTICATE_CODE)){
			lastHandShake = sendHandShake();
			lastHandShakeT = new Date().getTime();
			return lastHandShake;
		}
	    else if(lastHandShake != null && new Date().getTime() - lastHandShakeT < 180000)
			return lastHandShake;
		else {
			lastHandShake = sendHandShake();
			lastHandShakeT = new Date().getTime();
			return lastHandShake;
		}
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
	    if(resp.length() < 1)
	    	return false;
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
	
	public String sendCommand(String toSend) throws IOException {
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
