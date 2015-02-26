package org.craneprint.craneserver.gcode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FolderLoad {
	private final File dir;
	private HashMap<GCodeFile, String> ls = new HashMap<GCodeFile, String>();
	
	public FolderLoad(String d, String u){
		dir = new File(d);
		dir.mkdirs();
	}
	
	public HashMap<GCodeFile, String> loadAllFiles() throws IOException{
		    for (final File fileEntry : dir.listFiles()) {
		        if (!fileEntry.isDirectory() && !fileEntry.getName().endsWith(".meta") && !fileEntry.getName().endsWith(".db")) {
		            ls.put(new CraneCodeExtractor(fileEntry).getMyGCodeFile(), fileEntry.getName());
		        } else {
		            // TODO: Should there ever be any sub-directories?
		        }
		    }
		    return ls;
	}
}
