package org.proton_di.dependency.suppliers;

import java.util.function.Supplier;

import org.proton_di.dependency.InstanceType;
import org.proton_di.inject.Injector;

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
