package com.iw.dependency;

/**
 * An exception to be thrown if errors
 * arise during the creation of dependencies.
 * <br>
 * Extends Runtime Exception as any errors with 
 * dependency injection may be application critical.
 *
 * @author Luke Stevens
 */
public class DependencyCreationException extends RuntimeException {

	private static final long serialVersionUID = -5311835491784902850L;
	
	/**
	 * Creates a new Exception with a message in the form;
	 * <code>"Dependency creation of <i>c</i> failed. <i>message</i></code>"
	 * @param message The reason for the exception
	 * @param c The class that failed dependency creation
	 */
	public DependencyCreationException(String message, Class<?> c){
		this("Dependency creation of " + c.getName() + " failed. " + message);
	}
	
	/**
	 * Creates a new exception with a specific message
	 * @param message The reason for the exception 
	 */
	public DependencyCreationException(String message){
		super(message);
	}
}
