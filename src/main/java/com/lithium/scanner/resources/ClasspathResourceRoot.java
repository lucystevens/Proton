package com.lithium.scanner.resources;

import java.io.File;

/**
 * A class for resolving classes within a 
 * standard directory.
 * 
 * @author Luke Stevens
 */
public class ClasspathResourceRoot extends ResourceRoot {

	public ClasspathResourceRoot(String path) {
		super(path);
	}
	
	void loadClasses(){
		File root = new File(path);
		if(root.isDirectory()) loadClasses(path, root);
		else loadClass(path, root.getPath());
	}
	
	/**
	 * Loads all classes recursively from within a given
	 * directory into the list.
	 * @param root The classpath root
	 * @param dir The directory to scan
	 */
	private void loadClasses(String root, File dir){
		for(File file : dir.listFiles()){
			if(file.isDirectory()) loadClasses(root, file);
			else loadClass(root, file.getPath());
		}
	}

}
