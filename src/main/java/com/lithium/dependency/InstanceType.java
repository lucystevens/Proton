package com.lithium.dependency;

/**
 * An enum to determine the instantiation type e.g. Whether the
 * same instance should be returned whenever used, or
 * whether a new instance should be created.
 * 
 * @author Luke Stevens
 */
public enum InstanceType {

	/**
	 * Indicates that the same instance of a dependency
	 * should be used every time it is injected.
	 */
	SINGLETON, 
	
	/**
	 * Indicates that a new instance of a dependency
	 * should be created every time it is injected.
	 */
	MULTIPLE;
}
