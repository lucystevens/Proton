package com.lithium.configuration;

import com.lithium.dependency.exceptions.DependencyCreationException;

/**
 * An exception to be thrown if errors
 * arise during the creation of external dependencies
 * defined in the configuration class.
 *
 * @author Luke Stevens
 */
public class ConfigurationException extends DependencyCreationException {

	private static final long serialVersionUID = 3211013242573066911L;

	/**
	 * Creates a new Exception with a message in the form;
	 * <code>"Loading of dependencies from config: <i>configClass</i> failed. <i>message</i></code>"
	 * @param message The reason for the exception
	 * @param configClass The configClass that caused the exception
	 */
	public ConfigurationException(String message, Class<?> configClass) {
		super("Loading of dependencies from config: " + configClass.getName() + " failed. " + message);
	}

}
