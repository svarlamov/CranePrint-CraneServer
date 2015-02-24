package org.craneprint.craneserver.gcode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FolderLoad {
	private final File dir;
	private ArrayList<GCodeFile> ls = new ArrayList<GCodeFile>();
	
	public FolderLoad(String d, String u){
		dir = new File(d);
		dir.mkdirs();
	}
	
	public ArrayList<GCodeFile> loadAllFiles() throws IOException{
		    for (final File fileEntry : dir.listFiles()) {
		        if (!fileEntry.isDirectory() && !fileEntry.getName().endsWith(".meta") && !fileEntry.getName().endsWith(".db")) {
		            ls.add(new CraneCodeExtractor(fileEntry).getMyGCodeFile());
		        } else {
		            // TODO: Should there ever be any sub-directories?
		        }
		    }
		    return ls;
	}
}
