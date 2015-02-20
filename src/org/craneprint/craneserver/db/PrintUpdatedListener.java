package org.craneprint.craneserver.db;

import java.util.EventObject;

public interface PrintUpdatedListener {
	public void handleGCodeUploadedEvent(PrintUpdatedEvent e);
}
