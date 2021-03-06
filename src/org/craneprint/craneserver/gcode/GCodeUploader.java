package org.craneprint.craneserver.gcode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.craneprint.craneserver.ui.Craneprint_craneserverUI;
import org.craneprint.craneserver.users.User;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class GCodeUploader implements Receiver, SucceededListener, FailedListener, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -364033339938703999L;
	private File file;
    private String fileName = "";
    private GCodeFile gcode = null;
    // TODO: Think about this directory- does it really make sense, perhaps get one from the config file?
    private File folder;
    private List _listeners = new ArrayList();
    private Craneprint_craneserverUI ui;
    
    public OutputStream receiveUpload(String filename, String mimeType) {
    	ui = (Craneprint_craneserverUI)UI.getCurrent();
    	folder = new File(System.getProperty("user.home") + File.separator + "CranePrint Uploads" + File.separator + ui.getSessionUser().getUsername());
    	fileName = filename;
    	// Check that the directory exists, and if not create all of the directories necessary
    	if(!folder.exists()){
    		boolean b = folder.mkdirs();
    		if(!b){
    			new Notification("Could Not Open File",
                        "There was an Error Creating the Directory on the Server",
                        Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
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
            new Notification("Could Not Open File",
                             "Error Creating Stream on Server",
                             Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
            return null;
        }
        return fos; // Return the output stream to write to
    }
    
    // This is called if the upload succeeds
    public void uploadSucceeded(SucceededEvent event) {
        // Create a "GCodeFile" and add it to the accordion in the parent class
    	event.getComponent();
    	ui = (Craneprint_craneserverUI)UI.getCurrent();
    	GCodeFile gcf = new GCodeFile(ui.getDBManager(), file, fileName, "", ui.getSessionUser().getUsername());
    	CraneCodePacker c = new CraneCodePacker(gcf);
    	try {
			c.pack();
		} catch (IOException e) {
			new Notification("Error Uploading File",
	                "Could Not Write Metadata",
	                Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
			e.printStackTrace();
		}
    	fireEvent(gcf);
    }
    
    // This is called if the upload fails.
    public void uploadFailed(FailedEvent event) {
        // Tell the user that the upload failed
    	new Notification("Error Uploading File",
                event.getReason().getMessage(),
                Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
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
