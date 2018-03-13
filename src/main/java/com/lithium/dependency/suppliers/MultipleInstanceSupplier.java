package com.lithium.dependency.suppliers;

import java.util.function.Supplier;

import com.lithium.dependency.InstanceType;
import com.lithium.inject.Injector;

/**
 * A DependencySupplier implementation for dependencies
 * with an {@link InstanceType} of MULTIPLE.
 * 
 * @author Luke Stevens
 */
public class MultipleInstanceSupplier extends DependencySupplier {

	public MultipleInstanceSupplier(Class<?> dependency) {
		super(dependency);
	}

	@Override
	public Supplier<Object> generateSupplier(Injector injector) {
		return () -> injector.newInstance(dependency);
	}

}
