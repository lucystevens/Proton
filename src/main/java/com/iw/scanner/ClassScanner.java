package com.iw.scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
	
	private static List<Class<?>> classes;
	
	/**
	 * @return Gets the single ClassPath instance
	 */
	public static ClassPath getClassPath(){
		return INSTANCE;
	}
	
	/**
	 * Initialises the single instance of ClassPath on load.
	 * @return An instance of ClassPath containing a list of 
	 * java classes.
	 * @throws ClassScanningException If there is an error instantiating
	 * the ClassPath.
	 */
	private static ClassPath initClassPath(){
		try {
			loadClasses();
		} catch (IOException e) {
			throw new ClassScanningException(e);
		}
		return new ClassPath(classes);
	}
	
	/**
	 * Determines whether this application is running 
	 * as a jar.
	 * @return true if packaged as a jar, false if not.
	 */
	private static boolean isJar(){
		return ClassScanner.class.getResource("ClassScanner.class").toString().startsWith("jar:");
	}
	
	/**
	 * Loads all classes from the java classpaths provided
	 * by the system classloader into a list.
	 * @throws IOException If there is an exception loading
	 * classes from inside a jar file
	 */
	private static void loadClasses() throws IOException{
		classes = new ArrayList<>();
		Enumeration<URL> roots = ClassLoader.getSystemClassLoader().getResources("");
		while(roots.hasMoreElements()){
			loadClasses(roots.nextElement());
		}
	}
	
	/**
	 * Loads all classes from a root URL into a list.
	 * @param url The root directory of a java classpath
	 * @throws IOException If there is an exception loading
	 * classes from inside a jar file
	 */
	private static void loadClasses(URL url) throws IOException{
		String path = URLDecoder.decode(url.getPath().substring(1).replace("/", "\\"), "UTF-8");
		if(isJar()) loadClassesJar();
		else{
			File root = new File(path);
			if(root.isDirectory()) loadClasses(path, root);
			else loadClass(path, root.getPath());
		}
	}
	
	/**
	 * Loads all classes on the classpath within a jar
	 * into the class list.
	 * @throws IOException If there is an exception loading
	 * classes from inside a jar file
	 */
	private static void loadClassesJar() throws IOException{
		
		// Gets the path of the jar
		String path = ClassScanner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = URLDecoder.decode(path, "UTF-8");
		
		// Loops through all files within the Jar and loads each non-directory file
		try(ZipInputStream zip = new ZipInputStream(new FileInputStream(decodedPath))){
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
			    if (!entry.isDirectory()) {
			        loadClass("", entry.getName());
			    }
			}
		}
		
	}
	
	/**
	 * Loads all classes recursively from within a given
	 * directory into the list.
	 * @param root The classpath root
	 * @param dir The directory to scan
	 */
	private static void loadClasses(String root, File dir){
		for(File file : dir.listFiles()){
			if(file.isDirectory()) loadClasses(root, file);
			else loadClass(root, file.getPath());
		}
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
	private static void loadClass(String root, String path){
		try{
			if(path.endsWith(".class")){
				
				// Formats the path name to load it as a class
				Class<?> c = Class.forName(path.replace(root, "")
												.replace("\\", ".")
												.replace("/",  ".")
												.replace(".class", ""));
				classes.add(c);
			}
		} catch(ClassNotFoundException e){
			// Just don't load the class if it can't be found
		}
	}

}
