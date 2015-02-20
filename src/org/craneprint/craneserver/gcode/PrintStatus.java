package org.craneprint.craneserver.gcode;

public class PrintStatus {
	public final static int IN_QUE = 0;
	public final static int PRINTING = 1;
	public final static int COMPLETED = 2;
	public final static int CANCELLED = 3;
	
	public static String resolveToString(int i){
		if(i == IN_QUE)
			return "In Queue";
		else if(i == PRINTING)
			return "Printing";
		else if(i == COMPLETED)
			return "Completed";
		else if(i == CANCELLED)
			return "Cancelled";
		else
			return "Status is Unknown";
	}
}
