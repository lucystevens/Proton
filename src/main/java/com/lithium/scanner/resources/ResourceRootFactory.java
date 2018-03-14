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
	
	/**
	 * Constructs a new ResourceRootFactory, and retrieves the
	 * class path resources using the <code>java.class.path</code>
	 * system property.
	 */
	public ResourceRootFactory(){
		String classpath = System.getProperty("java.class.path");
		this.paths = classpath.split(";");
	}
	
	/**
	 * @return Get all ResourceRoots for the class path
	 */
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
	
	/**
	 * Checks if a root has a path prefix, or if it is a lone file
	 * @param root The path representing this resource root
	 * @return True if the root represents a file without
	 * an absolute path.
	 */
	private boolean noPathPrefix(String root){
		return !root.matches("(\\\\|\\/|[A-Z]:).*");
	}
	
	/**
	 * Checks if a resource root is a jar file
	 * @param root The path representing this resource root
	 * @return True if the resource root is a jar file, false
	 * if not.
	 */
	private boolean isJar(String root){
		return root.endsWith(".jar");
	}
	
	/**
	 * This matches windows file patterns (e.g. \C:) and removes
	 * the leading '\'.
	 * @param path The path to normalise
	 * @return The normalised path
	 */
	private String normalisePath(String path){
		return path.matches("(\\\\|\\/)[A-Z]:.*")? path.substring(1) : path;
	}
	
	/**
	 * @return The directory or file where the code is currently being executed from
	 */
	private String getCodeSource() throws UnsupportedEncodingException{
		String path = ClassScanner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = URLDecoder.decode(path, "UTF-8");
		
		return normalisePath(decodedPath);
	}

}
