package com.lithium.scanner.resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.lithium.scanner.ClassScanningException;

/**
 * A class for resolving classes with a 
 * jar file on the classpath
 * 
 * @author Luke Stevens
 */
public class JarRoot extends ResourceRoot {

	public JarRoot(String path) {
		super(path);
	}
	
	void loadClasses(){
		
		// Loops through all files within the Jar and loads each non-directory file
		try(ZipInputStream zip = new ZipInputStream(new FileInputStream(path))){
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
			    if (!entry.isDirectory()) {
			        loadClass("", entry.getName());
			    }
			}
		} catch(IOException e){
			throw new ClassScanningException(e);
		}
		
	}

}
