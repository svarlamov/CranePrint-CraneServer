package org.craneprint.craneserver;

import java.util.EventObject;

public class GCodeUploadedEvent extends EventObject {
	private GCodeFile gcode = null;
	
	public GCodeUploadedEvent(Object source, GCodeFile gcf){
		super(source);
		gcode = gcf;
	}
	
	public GCodeFile getGCode(){
		return gcode;
	}
}
