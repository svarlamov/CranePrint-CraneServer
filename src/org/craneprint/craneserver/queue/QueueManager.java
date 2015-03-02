package org.craneprint.craneserver.queue;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.craneprint.craneserver.db.DBManager;
import org.craneprint.craneserver.gcode.GCodeFile;
import org.craneprint.craneserver.gcode.PrintStatus;
import org.craneprint.craneserver.printers.HandShake;
import org.craneprint.craneserver.printers.PrinterStatus;
import org.craneprint.craneserver.printers.PrintersManager;
import org.json.simple.parser.ParseException;

public class QueueManager {
	private ServletContext context;
	
	public QueueManager(ServletContext sc){
		context = sc;
	}
	
	public boolean addFileToQueue(int printerId, GCodeFile f){
		boolean success = true;
		HandShake hs = null;
		try {
			hs = this.getPrintersManager().doHandShake(printerId);
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(this.getDBManager().isQueueEmpty(printerId) && hs != null && hs.getStatus() != PrinterStatus.FAILED_TO_CONNECT_CODE && hs.getStatus() != PrinterStatus.UNKNOWN_ERROR_CODE && hs.getStatus() != PrinterStatus.NO_DATA_CODE && hs.getStatus() != PrinterStatus.FAILED_TO_AUTHENTICATE_CODE){
			this.getDBManager().addFileToQueue(printerId, f);
			success = sendNextInQueue(printerId);
		}
		else {
			this.getDBManager().addFileToQueue(printerId, f);
		}
		return success;
	}
	
	public boolean sendNextInQueue(int printerId){
		boolean success = true;
		// Keep in mind that the following method will return null if there is nothing to print
		GCodeFile gcf = this.getDBManager().getNextInQueue(printerId);
		if(gcf != null){
			try {
				boolean b = this.getPrintersManager().getPrinter(printerId).getPrinterConnection().sendFile(gcf);
				//TODO: Add some code to replace the status of the print to failed
				if(!b)
					success = false;
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
				// Tell the printer to keep waiting, because there is nothing in the queue
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
		return (DBManager)context.getAttribute("org.craneprint.craneserver.db.dbManager");
	}
	
	private PrintersManager getPrintersManager(){
		return (PrintersManager)context.getAttribute("org.craneprint.craneserver.printers.printersManager");
	}
}
