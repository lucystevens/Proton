package com.lithium.dependency.exceptions;

import com.lithium.inject.InjectionException;

public class AmbiguousDependencyException extends InjectionException {

	private static final long serialVersionUID = -686577732705047355L;

	public AmbiguousDependencyException(Class<?> c) {
		super("Failed to inject. Multiple possible dependencies for " + c.getName());
	}

}
