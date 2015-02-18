package org.craneprint.craneserver.gcode;

import java.util.EventObject;

public interface GCodeUploadedListener {
	public void handleGCodeUploadedEvent(GCodeUploadedEvent e);
}
