package com.lithium.inject;

/**
 * An exception to be thrown if errors
 * arise during the injection of dependencies.
 * <br>
 * Extends Runtime Exception as any errors with 
 * dependency injection may be application critical.
 *
 * @author Luke Stevens
 */
public class InjectionException extends RuntimeException {

	private static final long serialVersionUID = -5311835491784902850L;
	
	/**
	 * Creates a new Exception with a message in the form;
	 * <code>"Failed to inject into <i>c</i>. <i>message</i></code>"
	 * @param message The reason for the exception
	 * @param c The class that failed dependency injection
	 */
	public InjectionException(String message, Class<?> c){
		this("Failed to inject into " + c.getName() + ". " + message);
	}
	
	/**
	 * Creates a new Exception with a message in the form;
	 * <code>"Failed to inject into <i>c</i>. Root cause: <i>e</i></code>"
	 * @param e The root exception that caused the error
	 * @param c The class that failed dependency injection
	 */
	public InjectionException(Exception e, Class<?> c){
		this("Root cause: " + e.getMessage(), c);
	}
	
	/**
	 * Creates a new exception with a specific message
	 * @param message The reason for the exception 
	 */
	public InjectionException(String message){
		super(message);
	}
}
