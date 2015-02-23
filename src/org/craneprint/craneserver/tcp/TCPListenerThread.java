package org.craneprint.craneserver.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.servlet.ServletContext;

import org.craneprint.craneserver.db.DBManager;
import org.craneprint.craneserver.printers.PrintersManager;
import org.craneprint.craneserver.queue.QueueManager;
import org.craneprint.craneserver.ui.Craneprint_craneserverUI;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.vaadin.ui.UI;

public class TCPListenerThread implements Runnable {
	private final int cranePort;
	private ServerSocket welcomeSocket;
	private ServletContext context;
	private volatile boolean stop = false;
	
	public TCPListenerThread(ServletContext sc, int crp){
		// TODO: Actually set these values
		context = sc;
		cranePort = crp;
		System.out.println("CRANEPORT: " + crp);
	}

	@Override
	public void run() {
		String receivedText;
		System.out.println("TCPThread Started");
		try {
		welcomeSocket = new ServerSocket(cranePort);
			while(!stop){
				Socket connectionSocket = welcomeSocket.accept();             
				BufferedReader inFromAgent = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToAgent = new DataOutputStream(connectionSocket.getOutputStream());
				receivedText = inFromAgent.readLine();
				if(receivedText.length() > 2){
					System.out.println("Received From Agent: " + receivedText);
					JSONObject jo = parseForObj(receivedText);
					int type = getType(jo);
					if(type == RequestType.JOB_COMPLETE){
						//this.getQueueManager().toString();
						this.getQueueManager().printComplete(new Long((long)jo.get("printerId")).intValue());
					}
					if(type == RequestType.GET_NEW_JOB){
						this.getQueueManager().sendNextInQueue(new Long((long)jo.get("printerId")).intValue());
					}
					outToAgent.writeBytes("{\"resp\":\"success\"}\n");
					//outToAgent.close();
				}
			}
		} catch(IOException e){
			e.printStackTrace();
		} catch(ParseException pe){
			pe.printStackTrace();
		}
	}
	
	public void closeConnection(){
		try {
			stop = true;
			welcomeSocket.close();
			System.out.println("TCPThread Stopped");
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
	
	private QueueManager getQueueManager(){
		return (QueueManager)context.getAttribute("org.craneprint.craneserver.queue.queueManager");
	}
}
