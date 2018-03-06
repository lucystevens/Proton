package com.lithium.scanner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * A Class to represent the Classes on the Java classpath
 * and to provide utility methods to access these.
 * 
 * @author Luke Stevens
 */
public class ClassPath {
	
	private List<Class<?>> classes;
	
	/**
	 * Constructed by {@link ClassScanner}
	 * @param classes A List of all classes on the classpath
	 */
	ClassPath(List<Class<?>> classes) {
		this.classes = classes;
	}
	
	/**
	 * @return All classes on the java class path
	 */
	public List<Class<?>> getClasses(){
		return classes;
	}
	
	/**
	 * Retrieves all classes on the java class path that
	 * satisfy a condition
	 * @param filter A {@link Predicate} to filter classes by
	 * @return A List of classes that satisfy the given Predicate
	 */
	public List<Class<?>> getClasses(Predicate<Class<?>> filter){
		List<Class<?>> results = new ArrayList<>();
		for(Class<?> c : classes){
			if(filter.test(c)) results.add(c);
		}
		return results;
	}
	
	/**
	 * Retrieves all classes on the java class path that
	 * are annotated with a given annotation
	 * @param a The annotation class to check against
	 * @return A List of classes annotated with the given annotation
	 */
	public List<Class<?>> getClassesWithAnnotation(Class<? extends Annotation> a){
		Predicate<Class<?>> filter = c -> c.getAnnotation(a) != null;
		return getClasses(filter);
	}
	
	/**
	 * Retrieves all classes on the java class path that within
	 * a specific package
	 * @param packageName The name of the package to check within
	 * @return A List of classes within the given package
	 */
	public List<Class<?>> getClassesInPackage(String packageName){
		Predicate<Class<?>> filter = c -> c.getName().startsWith(packageName);
		return getClasses(filter);
	}
	
	/**
	 * Retrieves all classes on the java class path that 
	 * that implement a specific interface
	 * @param iface The interface class to check for implementations
	 * @return A List of classes that implement the given interface
	 */
	public List<Class<?>> getImplementingClasses(Class<?> iface){
		if(iface.isInterface()) {
			Predicate<Class<?>> filter = c -> Arrays.asList(c.getInterfaces()).contains(iface);
			return getClasses(filter);
		}
		else return new ArrayList<>();
	}

}
