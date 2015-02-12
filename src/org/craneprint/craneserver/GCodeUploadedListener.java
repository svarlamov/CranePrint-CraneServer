package org.craneprint.craneserver;

import java.util.EventObject;

public interface GCodeUploadedListener {
	public void handleGCodeUploadedEvent(GCodeUploadedEvent e);
}
