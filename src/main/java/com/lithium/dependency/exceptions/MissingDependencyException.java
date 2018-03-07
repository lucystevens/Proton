package com.lithium.dependency.exceptions;

import com.lithium.inject.InjectionException;

public class MissingDependencyException extends InjectionException {

	private static final long serialVersionUID = -3729738073733840452L;

	public MissingDependencyException(Class<?> c) {
		super("Missing dependency: " + c.getName());
	}

}
