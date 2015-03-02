package org.craneprint.craneserver.printers;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.craneprint.craneserver.db.DBManager;
import org.craneprint.craneserver.gcode.GCodeFile;
import org.craneprint.craneserver.queue.QueueManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;

public class PrintersManager {
	// A class to manage all printers that we can connect to. This class will be owned by the PrinterComposite
	private ArrayList<Printer> printers = new ArrayList<Printer>();
	private ServletContext context;
	
	public PrintersManager(ServletContext sc){
		context = sc;
	}

	public ArrayList<Printer> loadAll(){
		DBManager db = (DBManager)context.getAttribute("org.craneprint.craneserver.db.dbManager");
		ArrayList<BasicDBObject> p = db.getAllPrinters();
		// take each value from the json array separately
		for(BasicDBObject obj : p){
			printers.add(new Printer(obj));
		}
		return printers;
	}
	
	// Possibly insecure method, commented out for now, but we'll see
	/*public ArrayList<Printer> getAll(){
		// Returns the whole "printers" arraylist
		return printers;
	}*/
	
	public int getSize(){
		return printers.size();
	}
	
	/**
	 * 
	 * @param id The id of the Printer
	 * @return The printer of that id, and in the case where the printer can not be found we return null
	 */
	public Printer getPrinter(int id){
		for(Printer p : printers){
			if(p.getId() == id)
				return p;
		}
		return null;
	}
	
	public Printer getPrinter(String name){
		for(Printer p : printers){
			if(p.getName() == name)
				return p;
		}
		return null;
	}
	
	public HandShake doHandShake(int index) throws IOException, ParseException{
		// Find the printer to handshake with, initiate the connection and pass back the values
		return printers.get(index).getPrinterConnection().initHandShake();
	}
	
	public void addFile(int index, GCodeFile f){
		QueueManager q = (QueueManager)context.getAttribute("org.craneprint.craneserver.queue.queueManager");
		q.addFileToQueue(printers.get(index).getId(), f);
	}
}
