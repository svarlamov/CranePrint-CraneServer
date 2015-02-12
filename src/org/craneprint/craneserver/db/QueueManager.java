package org.craneprint.craneserver.db;

import java.io.IOException;

import org.craneprint.craneserver.Craneprint_craneserverUI;
import org.craneprint.craneserver.GCodeFile;
import org.craneprint.craneserver.tcp.SendFile;
import org.craneprint.craneserver.tcp.TCPThread;
import org.json.simple.parser.ParseException;

import com.vaadin.ui.UI;

public class QueueManager {
	public void addFileToQueue(int printerId, GCodeFile f){
		Craneprint_craneserverUI ui = (Craneprint_craneserverUI)UI.getCurrent();
		if(ui.getDBManager().isQueueEmpty(printerId)){
			ui.getDBManager().addFileToQueue(printerId, f);
			sendNextInQueue(printerId);
		}
		else {
			ui.getDBManager().addFileToQueue(printerId, f);
		}
	}
	
	public boolean sendNextInQueue(int printerId){
		boolean success = true;
		// Keep in mind that the following method will return null if there is nothing to print
		Craneprint_craneserverUI ui = (Craneprint_craneserverUI)UI.getCurrent();
		GCodeFile gcf = ui.getDBManager().getNextInQueue(printerId);
		if(gcf != null){
			try {
				SendFile.sendFile(gcf);
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
				TCPThread.sendCommand("{\"resp\":\"nothingInQueue\"}\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public void printComplete(int printerId){
		Craneprint_craneserverUI ui = (Craneprint_craneserverUI)UI.getCurrent();
		ui.getDBManager().finishFile(printerId, PrintStatus.COMPLETED);
	}
}
