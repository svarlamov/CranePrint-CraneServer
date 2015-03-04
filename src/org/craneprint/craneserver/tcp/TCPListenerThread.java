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
	private String success;
	private volatile boolean stop = false;
	
	public TCPListenerThread(ServletContext sc, int crp){
		// TODO: Actually set these values
		context = sc;
		cranePort = crp;
		JSONObject s = new JSONObject();
		s.put("type", RequestType.REQUEST_SUCCEEDED);
		success = s.toJSONString();
		System.out.println("CRANEPORT: " + crp);
	}

	@Override
	public void run() {
		String receivedText;
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
						this.getQueueManager().printComplete((int)(long)jo.get("printerId"));
						outToAgent.writeBytes(success + "\n");
					} else if(type == RequestType.GET_NEW_JOB){
						this.getQueueManager().sendNextInQueue((int)(long)jo.get("printerId"));
						outToAgent.writeBytes(success + "\n");
					} else if(type == RequestType.ADD_PRINTER){
						int i = this.getDBManager().addPrinter((String)jo.get("name"), (String)jo.get("password"), (String)jo.get("ip"), (int)jo.get("port"));
						if(i != -1){
							JSONObject j = new JSONObject();
							j.put("type", RequestType.PRINTER_ADDED);
							j.put("id", i);
							outToAgent.writeBytes(j.toJSONString() + "\n");
						} else {
							JSONObject j = new JSONObject();
							j.put("type", RequestType.REQUEST_FAILED);
							outToAgent.writeBytes(j.toJSONString() + "\n");
						}
					} else {
						JSONObject j = new JSONObject();
						j.put("type", RequestType.UNKNOWN_REQUEST_CODE);
						outToAgent.writeBytes(j.toJSONString() + "\n");
					}
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
			Thread.sleep(1000);
			welcomeSocket.close();
		} catch(IOException | InterruptedException e){
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
	
	private DBManager getDBManager(){
		return (DBManager)context.getAttribute("org.craneprint.craneserver.queue.dbManager");
	}
}
