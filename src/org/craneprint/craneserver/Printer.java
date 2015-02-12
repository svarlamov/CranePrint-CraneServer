package org.craneprint.craneserver;

public class Printer {
	private String name = "";
	private String ipAddress = "";
	private int port = 8080;
	private int status = PrinterStatus.NO_DATA_CODE;
	private String password = "";
	private PrinterConnection connection = null;
	private int id = 0;
	
	// TODO: This class shall contain and manage all of the credentials of the printer and the status of it ie. printing, ready, waiting etc.
	public Printer(String n, String pw, String ip, int p){
		name = n;
		ipAddress = ip;
		password = pw;
		if(p != 8080/*default port*/)
			port = p;
		// Create a new connection, keep in mind just because this class has been instantiated
		// and variables initialized the printer has nothing sent to it yet, nor an actual connection initialized
		connection = new PrinterConnection(password, ip, port);
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
