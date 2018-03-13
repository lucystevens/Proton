package com.lithium.dependency.suppliers;

import java.util.List;
import java.util.function.Supplier;

import com.lithium.inject.InjectionTools;
import com.lithium.inject.Injector;

public class MultipleInstanceSupplier extends DependencySupplier {

	public MultipleInstanceSupplier(Class<?> dependency) {
		super(dependency);
	}

	@Override
	List<Class<?>> loadSubDependencies() {
		InjectionTools tools = new InjectionTools();
		return tools.getSubDependencies(dependency);
	}

	@Override
	public Supplier<Object> generateSupplier(Injector injector) {
		if(supplier == null){
			supplier = () -> injector.newInstance(dependency);
		}
		return supplier;
	}

}
