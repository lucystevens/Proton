package com.lithium.dependency;

import com.lithium.dependency.suppliers.DependencySupplier;
import com.lithium.dependency.suppliers.MultipleInstanceSupplier;
import com.lithium.dependency.suppliers.SingletonSupplier;

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
	SINGLETON() {
		
		@Override
		public DependencySupplier createSupplier(Class<?> dependency) {
			return new SingletonSupplier(dependency);
		}
		
	}, 
	
	/**
	 * Indicates that a new instance of a dependency
	 * should be created every time it is injected.
	 */
	MULTIPLE {
		
		@Override
		public DependencySupplier createSupplier(Class<?> dependency) {
			return new MultipleInstanceSupplier(dependency);
		}
		
	};
	
	/**
	 * Creates the supplier for a dependency. 
	 * @param c The class to create the supplier for
	 * @return A lambda function that either creates a new instance
	 * or returns the existing singleton instance when called.
	 */
	public abstract DependencySupplier createSupplier(Class<?> dependency);

}
