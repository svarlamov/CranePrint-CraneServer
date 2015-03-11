package org.craneprint.craneserver.gcode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.craneprint.craneserver.ui.Craneprint_craneserverUI;

public class FolderLoad {
	private final File dir;
	private Craneprint_craneserverUI ui;
	private HashMap<GCodeFile, String> ls = new HashMap<GCodeFile, String>();
	
	public FolderLoad(Craneprint_craneserverUI u, String d){
		ui = u;
		dir = new File(d);
		dir.mkdirs();
	}
	
	public HashMap<GCodeFile, String> loadAllFiles() throws IOException{
		    for (final File fileEntry : dir.listFiles()) {
		        if (!fileEntry.isDirectory() && !fileEntry.getName().endsWith(".meta") && !fileEntry.getName().endsWith(".db")) {
		            ls.put(new CraneCodeExtractor(ui, fileEntry).getMyGCodeFile(), fileEntry.getName());
		        } else {
		            // TODO: Should there ever be any sub-directories?
		        }
		    }
		    return ls;
	}
}
