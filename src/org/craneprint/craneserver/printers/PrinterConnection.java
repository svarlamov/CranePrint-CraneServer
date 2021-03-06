package org.craneprint.craneserver.printers;

import java.io.IOException;

import org.craneprint.craneserver.gcode.GCodeFile;
import org.craneprint.craneserver.tcp.RequestType;
import org.craneprint.craneserver.tcp.TCPTransmitter;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class PrinterConnection {
	private String password = "password";
	private String ipAddress = "172.16.42.13";
	private int port = 6880;
	private TCPTransmitter tcp;
	
	public PrinterConnection(String p, String ip, int prt){
		port = prt;
		ipAddress = ip;
		password = p;
		tcp = new TCPTransmitter(ipAddress, port);
	}
	
	public HandShake initHandShake() throws IOException, ParseException{
		HandShake hs = tcp.getHandShake();
		return hs;
	}
	
	public boolean sendFile(GCodeFile f) throws IOException, ParseException{
		// TODO: Don't forget about setting up the notes variable...
		return tcp.sendFile(f);
	}
	
	public void sendQueueEmpty() throws IOException {
		JSONObject j = new JSONObject();
		j.put("type", RequestType.QUEUE_EMPTY);
		tcp.sendCommand(j.toJSONString() + "\n");
	}
}
