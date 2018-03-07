package com.lithium.inject.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for declaring that fields
 * in a class, or parameters in a constructor
 * are to be injected with the appropriate dependency
 * 
 * @author Luke Stevens
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface Inject {}
