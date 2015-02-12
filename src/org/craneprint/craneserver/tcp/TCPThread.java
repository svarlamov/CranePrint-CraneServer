package org.craneprint.craneserver.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.craneprint.craneserver.Craneprint_craneserverUI;
import org.craneprint.craneserver.db.QueueManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.vaadin.ui.UI;

public class TCPThread implements Runnable {
	private final static String ip = "172.16.42.13";
	private final static int cranePort = 6770;
	private final static int chickPort = 6880;
	private ServerSocket welcomeSocket;

	@Override
	public void run() {
		String receivedText;
		Craneprint_craneserverUI ui = (Craneprint_craneserverUI)UI.getCurrent();
		System.out.println("TCPThread Started");
		try {
		welcomeSocket = new ServerSocket(cranePort);
			while(true){
				Socket connectionSocket = welcomeSocket.accept();             
				BufferedReader inFromAgent = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToAgent = new DataOutputStream(connectionSocket.getOutputStream());
				receivedText = inFromAgent.readLine();
				if(receivedText.length() > 2){
					System.out.println("Received From Agent: " + receivedText);
					JSONObject jo = parseForObj(receivedText);
					int type = getType(jo);
					if(type == RequestType.JOB_COMPLETE){
						ui.getQueueManager().printComplete((int)(long)jo.get("printerId"));
					}
					if(type == RequestType.GET_NEW_JOB){
						ui.getQueueManager().sendNextInQueue((int)(long)jo.get("printerId"));
					}
					outToAgent.writeBytes("{\"resp\":\"success\"}\n");
				}
			}
		} catch(IOException e){
			e.printStackTrace();
		} catch(ParseException pe){
			pe.printStackTrace();
		}
	}
	
	public static String sendCommand(String toSend) throws IOException{
		String resp;
		Socket clientSocket = new Socket(ip, chickPort);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes(toSend + "\n");
		resp = inFromServer.readLine();
		System.out.println("FROM AGENT: " + resp);
		clientSocket.close();
		return resp;
	}
	
	public void closeConnection(){
		try {
			System.out.println("TCPThread Stopped");
			welcomeSocket.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private JSONObject parseForObj(String arg0) throws ParseException{
		JSONParser jsonParser = new JSONParser();
		return (JSONObject) jsonParser.parse(arg0);
	}
	
	private int getType(JSONObject j){
		int ret = (int)(long)j.get("type");
		System.out.println(ret);
		return ret;
	}
}
