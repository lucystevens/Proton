package com.lithium.scanner.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.lithium.scanner.ClassScanner;
import com.lithium.scanner.ClassScanningException;

/**
 * A factory for creating ResourceRoots from 
 * the paths in the <code>java.class.path</code>
 * System variable.
 * 
 * @author Luke Stevens
 */
public class ResourceRootFactory {
	
	private String[] paths;
	
	public ResourceRootFactory(){
		String classpath = System.getProperty("java.class.path");
		this.paths = classpath.split(";");
	}
	
	public List<ResourceRoot> getRoots(){
		List<ResourceRoot> roots = new ArrayList<>();
		try{
			for(String path : paths){
				path = normalisePath(path);
				ResourceRoot root = noPathPrefix(path)? new JarRoot(getCodeSource())
									: isJar(path)? new JarRoot(path)
									: new ClasspathResourceRoot(path);
				roots.add(root);
			}
		} catch(UnsupportedEncodingException e){
			throw new ClassScanningException(e);
		}
		return roots;
	}
	
	private boolean noPathPrefix(String root){
		return !root.matches("(\\\\|[A-Z]:).*");
	}
	
	private boolean isJar(String root){
		return root.endsWith(".jar");
	}
	
	// This matches windows file patterns (e.g. \C:) and removes the '\'
	private String normalisePath(String path){
		return path.matches("(\\\\|\\/)[A-Z]:.*")? path.substring(1) : path;
	}
	
	private String getCodeSource() throws UnsupportedEncodingException{
		String path = ClassScanner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = URLDecoder.decode(path, "UTF-8");
		
		return normalisePath(decodedPath);
	}

}
