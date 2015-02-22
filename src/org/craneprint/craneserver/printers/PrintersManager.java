package org.craneprint.craneserver.printers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.craneprint.craneserver.gcode.GCodeFile;
import org.craneprint.craneserver.queue.QueueManager;
import org.craneprint.craneserver.ui.Craneprint_craneserverUI;
import org.craneprint.craneserver.ui.GetServerSettings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.vaadin.ui.UI;

public class PrintersManager {
	// A class to manage all printers that we can connect to. This class will be owned by the PrinterComposite
	private ArrayList<Printer> printers = new ArrayList<Printer>();
	
	public PrintersManager(){
		super();
	}
	
	public ArrayList<Printer> loadAll(){
		// TODO: Get all of the printers outlined in the settings, add them to "printers and then return "printers"
		// Get all printers from the settings file and them add them into "printers"
		JSONArray a = GetServerSettings.getArray("printers");
		Iterator i = a.iterator();
		// take each value from the json array separately
		while (i.hasNext()) {
			JSONObject innerObj = (JSONObject) i.next();
			printers.add(new Printer((String)innerObj.get("name"), (String)innerObj.get("password"), (String)innerObj.get("ip"), Integer.parseInt((String)innerObj.get("port"))));
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
	
	public Printer getPrinter(int index){
		return printers.get(index);
	}
	
	public HandShake doHandShake(int index) throws IOException, ParseException{
		// Find the printer to handshake with, initiate the connection and pass back the values
		return printers.get(index).getPrinterConnection().initHandShake();
	}
	
	public void addFile(int index, GCodeFile f){
		// TODO: Get the Printer, PrinterConnection, and then send it off!
		//return printers.get(index).getPrinterConnection().sendFile(f);
		Craneprint_craneserverUI ui = (Craneprint_craneserverUI)UI.getCurrent();
		ui.getQueueManager().addFileToQueue(printers.get(index).getId(), f);
	}
}
