package org.craneprint.craneserver;

import org.craneprint.craneserver.tcp.SendFile;
import org.craneprint.craneserver.tcp.SendHandShake;

public class PrinterConnection {
	//TODO: This class shall provide an interface to the "PostToPrinter" servlet. It will pass on credentials, commands, files, status requests, etc.
	private String password = "password";
	private String ipAddress = "172.16.42.13";
	private int port = 8181;
	private static String pathInitHandShake = "/CranePrint_Virtual_ChickServer/InitHandShake";
	private static String pathInitHandleFile = "/CranePrint_Virtual_ChickServer/HandleFile";
	
	public PrinterConnection(String p, String ip, int prt){
		//port = prt;
		//ipAddress = ip;
		//password = p;
	}
	
	public HandShake initHandShake(){
		HandShake hs = null;
		try {
			hs = SendHandShake.sendIt();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return hs;
	}
	
	public String sendCommand(){
		return "MODIFY";
	}
	
	public boolean sendFile(GCodeFile f){
		// TODO: Don't forget about setting up the notes variable...
		boolean success = true;
		try {
			success = SendFile.sendFile(f);
		} catch(Exception e) {
			e.printStackTrace();
			success = false;
		}
		return false;
	}
	
	public int getPrinterStatus(){
		return -1;
	}
	
	public int getPrintProgress(){
		return -1;
	}
}
