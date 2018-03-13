package com.lithium.dependency.suppliers;

import java.util.function.Supplier;

import com.lithium.dependency.InstanceType;
import com.lithium.inject.Injector;

/**
 * A DependencySupplier implementation for dependencies
 * with an {@link InstanceType} of SINGLETON.
 * 
 * @author Luke Stevens
 */
public class SingletonSupplier extends DependencySupplier {
	
	public SingletonSupplier(Class<?> dependency) {
		super(dependency);
	}

	@Override
	public Supplier<Object> generateSupplier(Injector injector) {
		Object instance = injector.newInstance(dependency);
		return () -> instance;	
	}

}
