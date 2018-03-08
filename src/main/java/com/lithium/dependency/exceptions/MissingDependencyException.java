package com.lithium.dependency.exceptions;

import com.lithium.inject.exceptions.InjectionException;

/**
 * An exception to be thrown if a dependency
 * required for injection does not exist.
 *
 * @author Luke Stevens
 */
public class MissingDependencyException extends InjectionException {

	private static final long serialVersionUID = -3729738073733840452L;

	/**
	 * Creates a new Exception with a message in the form;
	 * "<code>Missing dependency: <i>c</i></code>"
	 * @param c The class without a defined dependency.
	 */
	public MissingDependencyException(Class<?> c) {
		super("Missing dependency: " + c.getName());
	}

}
