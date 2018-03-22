package org.proton_di.scanner.resources;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.proton_di.scanner.ClassScanningException;

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
		this.paths = classpath.split(File.pathSeparator);
	}
	
	/**
	 * @return Get all ResourceRoots for the class path
	 */
	public List<ResourceRoot> getRoots(){
		List<ResourceRoot> roots = new ArrayList<>();
		for(String path : paths){
			path = normalisePath(path);
			ResourceRoot root = getRoot(path);
			roots.add(root);
		}
		return roots;
	}
	
	/**
	 * Gets the relevant resource root associated with
	 * this path.
	 * @param path The resource path
	 * @return A ResourceRoot implementation for the 
	 * supplied resource path.
	 */
	private ResourceRoot getRoot(String path){
		if(noPathPrefix(path)) return new JarRoot(getCodeSource());
		else if (isJar(path)) return new JarRoot(path);
		else return new ClasspathResourceRoot(path);
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
	private String getCodeSource() {
		try{
			String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			String decodedPath = URLDecoder.decode(path, "UTF-8");
			
			return normalisePath(decodedPath);
			
		} catch(UnsupportedEncodingException e){
			throw new ClassScanningException(e);
		}
	}

}
