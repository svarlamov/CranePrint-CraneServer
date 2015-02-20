package org.craneprint.craneserver.db;

import java.util.EventObject;

public class PrintUpdatedEvent extends EventObject {
	private final int printId;
	private final int printerId;
	
	public PrintUpdatedEvent(Object source, int printid, int printerid){
		super(source);
		printId = printid;
		printerId = printerid;
	}
	
	public int getPrintId(){
		return printId;
	}
	
	public int getPrinterId(){
		return printerId;
	}
}
