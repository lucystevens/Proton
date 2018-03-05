package com.iw.dependency;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for declaring a class as a
 * dependency to be loaded by the Injector
 * class.
 * 
 * @author Luke Stevens
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface Dependency {

	/**
	 * @return The instantiation type e.g. Whether the
	 * same instance should be returned whenever used, or
	 * whether a new instance should be created.
	 */
	InstanceType type();
	
}
