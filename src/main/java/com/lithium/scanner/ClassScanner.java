package com.lithium.scanner;

import java.util.ArrayList;
import java.util.List;
import com.lithium.scanner.resources.ResourceRoot;
import com.lithium.scanner.resources.ResourceRootFactory;

/**
 * A static class that scans the Java class
 * path, retrieves all <code>.class</code> files
 * on load, and uses them to instantiate the single
 * instance of {@link ClassPath}.
 * 
 * @author Luke Stevens
 */
public class ClassScanner {
	
	private static final ClassPath INSTANCE = initClassPath();
	
	/**
	 * @return Gets the single ClassPath instance
	 */
	public static ClassPath getClassPath(){
		return INSTANCE;
	}
	
	private ClassScanner(){ /* Private constructor for utility class */ }
	
	/**
	 * Initialises the single instance of ClassPath on load.
	 * @return An instance of ClassPath containing a list of 
	 * java classes.
	 * @throws ClassScanningException If there is an error instantiating
	 * the ClassPath.
	 */
	private static ClassPath initClassPath(){
		List<Class<?>> classes = new ArrayList<>();
		ResourceRootFactory factory = new ResourceRootFactory();
		for(ResourceRoot root : factory.getRoots()){
			classes.addAll(root.getClasses());
		}
		return new ClassPath(classes);
	}
	
	


}
