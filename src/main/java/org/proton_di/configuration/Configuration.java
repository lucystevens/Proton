package org.proton_di.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.proton_di.inject.InjectionManager;

/**
 * Simple annotation to define classes for configuring
 * external dependencies.<br>
 * 
 * This annotation must only be used on <i>concrete</i>
 * configuration classes that can be instantiated using
 * a no-args constructor.<br>
 * 
 * Each method within this class (both static and non-static) will
 * then be stored as the supplier for its dependency return type.
 * 
 * @author Luke Stevens
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface Configuration {
	
	public String qualifier() default InjectionManager.ROOT_QUALIFIER;
}
