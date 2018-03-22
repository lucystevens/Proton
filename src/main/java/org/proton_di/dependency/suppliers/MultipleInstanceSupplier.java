package org.proton_di.dependency.suppliers;

import java.util.function.Supplier;

import org.proton_di.dependency.InstanceType;
import org.proton_di.inject.Injector;

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
