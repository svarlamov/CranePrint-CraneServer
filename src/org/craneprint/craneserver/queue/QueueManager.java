package org.craneprint.craneserver.queue;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.craneprint.craneserver.db.DBManager;
import org.craneprint.craneserver.db.PrintStatus;
import org.craneprint.craneserver.gcode.GCodeFile;
import org.craneprint.craneserver.printers.PrintersManager;
import org.craneprint.craneserver.ui.Craneprint_craneserverUI;
import org.json.simple.parser.ParseException;

import com.vaadin.ui.UI;

public class QueueManager {
	private ServletContext context;
	
	public QueueManager(ServletContext sc){
		context = sc;
	}
	
	public void addFileToQueue(int printerId, GCodeFile f){
		if(this.getDBManager().isQueueEmpty(printerId)){
			this.getDBManager().addFileToQueue(printerId, f);
			sendNextInQueue(printerId);
		}
		else {
			this.getDBManager().addFileToQueue(printerId, f);
		}
	}
	
	public boolean sendNextInQueue(int printerId){
		boolean success = true;
		// Keep in mind that the following method will return null if there is nothing to print
		GCodeFile gcf = this.getDBManager().getNextInQueue(printerId);
		if(gcf != null){
			try {
				this.getPrintersManager().getPrinter(printerId).getPrinterConnection().sendFile(gcf);
			} catch (IOException e) {
				//TODO: Add some code to replace the status of the print to failed
				success = false;
				e.printStackTrace();
			} catch (ParseException e) {
				//TODO: Add some code to replace the status of the print to failed
				success = false;
				e.printStackTrace();
			}
			return success;
		}
		else {
			try {
				// Tell the printer to keep waiting, becuase there is nothing in the queue
				this.getPrintersManager().getPrinter(printerId).getPrinterConnection().sendQueueEmpty();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public void printComplete(int printerId){
		this.getDBManager().finishFile(printerId, PrintStatus.COMPLETED);
	}
	
	private DBManager getDBManager(){
		return (DBManager)context.getAttribute("dbManager");
	}
	
	private PrintersManager getPrintersManager(){
		return (PrintersManager)context.getAttribute("printersManager");
	}
}
