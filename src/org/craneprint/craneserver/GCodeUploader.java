package org.craneprint.craneserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class GCodeUploader implements Receiver, SucceededListener, FailedListener {
    private File file;
    private String fileName = "";
    private GCodeFile gcode = null;
    // TODO: Actually get the session user!!!
    private final String user = "testUser";
    // TODO: Think about this directory- does it really make sense, perhaps get one from the config file?
    private final File folder = new File(System.getProperty("user.home") + File.separator + "CranePrint Uploads" + File.separator + user);
    private List _listeners = new ArrayList();
    
    
    public OutputStream receiveUpload(String filename, String mimeType) {
    	fileName = filename;
    	// Check that the directory exists, and if not create all of the directories necessary
    	if(!folder.exists()){
    		boolean b = folder.mkdirs();
    		if(!b){
    			new Notification("Could Not Open File",
                        "There was an error creating the directory on the server",
                        Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
    			return null;
    		}
    	}
    	// TODO: Make it so that the file is the filename + the username
        // Create file and initialize outputstream
    	file = new File(folder, fileName);
    	FileOutputStream fos = null;
        try {
            // Open the file for writing.
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",
                             e.getMessage(),
                             Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
            return null;
        }
        return fos; // Return the output stream to write to
    }
    
    // This is called if the upload succeeds
    public void uploadSucceeded(SucceededEvent event) {
        //TODO: Create a "GCodeFile" and add it to the accordion in the parent class
    	GCodeFile gcf = new GCodeFile(file, fileName, user);
    	fireEvent(gcf);
    }
    
    // This is called if the upload fails.
    public void uploadFailed(FailedEvent event) {
        // Tell the user that the upload failed
    	new Notification("Error Uploading File<br/>",
                event.getReason().getMessage(),
                Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
    }
    
    public synchronized void addEventListener(GCodeUploadedListener listener)  {
        _listeners.add(listener);
    }
     
    public synchronized void removeEventListener(GCodeUploadedListener listener)   {
    	_listeners.remove(listener);
    }
     
     // call this method whenever you want to notify
     //the event listeners of the particular event
     private synchronized void fireEvent(GCodeFile gcf) {
        GCodeUploadedEvent event = new GCodeUploadedEvent(this, gcf);
        Iterator i = _listeners.iterator();
        while(i.hasNext())  {
          ((GCodeUploadedListener) i.next()).handleGCodeUploadedEvent(event);
        }
     }
}
