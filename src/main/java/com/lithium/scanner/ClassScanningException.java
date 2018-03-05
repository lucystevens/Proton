package com.lithium.scanner;

/**
 * An exception to be thrown if errors
 * arise whilst scanning the classpath.
 * <br>
 * Extends Runtime Exception as any errors with 
 * classpath scanning may be application critical.
 *
 * @author Luke Stevens
 */
public class ClassScanningException extends RuntimeException{

	private static final long serialVersionUID = -5311835491784902850L;
	
	/**
	 * Creates a new Exception with a message in the form;
	 * <code>"ClassPath instantiation failed with root cause: <i>cause</i></code>"
	 * @param cause The exception that caused the error
	 */
	public ClassScanningException(Exception cause){
		this("ClassPath instantiation failed with root cause: " + cause.getMessage());
	}
	
	/**
	 * Creates a new exception with a specific message
	 * @param message The reason for the exception 
	 */
	public ClassScanningException(String message){
		super(message);
	}

}
