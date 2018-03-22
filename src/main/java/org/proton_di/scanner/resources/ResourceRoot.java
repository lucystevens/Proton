package org.proton_di.scanner.resources;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class to represent a root resource
 * on the classpath. This may be a standard directory
 * or a jar.
 * 
 * @author Luke Stevens
 */
public abstract class ResourceRoot {
	
	String path;
	List<Class<?>> classes = new ArrayList<>();

	/**
	 * Constructs a new ResourceRoot and loads all classes from it
	 * @param path representation the path to the resource
	 */
	public ResourceRoot(String path) {
		this.path = path;
		loadClasses();
	}
	
	/**
	 * Loads a single class to the list. If it is
	 * not a <code>.class</code> file or cannot be found
	 * then it is simply ignored.
	 * @param root The classpath root to be removed when
	 * creating the fully qualified class name needed to
	 * load the class.
	 * @param path The path to the file to load.
	 */
	void loadClass(String root, String path){
		
		// Removes line separators from root path
		root = root.replaceAll("\\\\|\\/", ".");
		
		try{
			if(path.endsWith(".class")){
				
				// Formats the path name to load it as a class
				String name = path.replaceAll("\\\\|\\/", ".").replace(root, "").replace(".class", "");
				if(name.startsWith(".")) name = name.substring(1);
				
				Class<?> c = Class.forName(name, false, ClassLoader.getSystemClassLoader());
				classes.add(c);
			}
		} catch(NoClassDefFoundError | ClassNotFoundException e){
			// Just don't load the class if it can't be found
		}
	}
	
	/**
	 * Loads all classes from this resource
	 */
	abstract void loadClasses();
	
	/**
	 * @return All classes from this resource
	 */
	public List<Class<?>> getClasses(){
		return classes;
	}

}
