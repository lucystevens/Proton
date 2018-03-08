package com.lithium.dependency.exceptions;

import com.lithium.inject.exceptions.InjectionException;

/**
 * An exception to be thrown if there could be
 * multiple dependencies supplied for a given class.
 *
 * @author Luke Stevens
 */
public class AmbiguousDependencyException extends InjectionException {

	private static final long serialVersionUID = -686577732705047355L;

	/**
	 * Creates a new Exception with a message in the form;
	 * "<code>Failed to inject. Multiple possible dependencies for <i>c</i></code>"
	 * @param c The class for which there are multiple possible 
	 * dependencies.
	 */
	public AmbiguousDependencyException(Class<?> c) {
		super("Failed to inject. Multiple possible dependencies for " + c.getName());
	}

}
