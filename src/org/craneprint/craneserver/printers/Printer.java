package org.craneprint.craneserver.printers;

import com.mongodb.BasicDBObject;


public class Printer {
	private String name = "";
	private String ipAddress = "";
	private int port = 6880;
	private int status = PrinterStatus.NO_DATA_CODE;
	private String password = "";
	private PrinterConnection connection = null;
	private int id;
	
	// TODO: This class shall contain and manage all of the credentials of the printer and the status of it ie. printing, ready, waiting etc.
	public Printer(BasicDBObject obj){
		id = obj.getInt("id");
		name = obj.getString("name");
		ipAddress = obj.getString("ip");
		password = obj.getString("password");
		port = obj.getInt("port");
		// Create a new connection, keep in mind just because this class has been instantiated
		// and variables initialized the printer has nothing sent to it yet, nor an actual connection initialized
		connection = new PrinterConnection(password, ipAddress, port);
	}
	
	public PrinterConnection getPrinterConnection(){
		// Get the connection in order to communicate with the printer
		return connection;
	}
	
	public String getName(){
		// Return the name of the printer for the GUI
		return name;
	}
	
	public String getIp(){
		// Return ip address, possibly an arbitrary method?
		return ipAddress;
	}
	
	public int getId(){
		return id;
	}
}
