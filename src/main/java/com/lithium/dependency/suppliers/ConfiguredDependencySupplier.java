package com.lithium.dependency.suppliers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.lithium.inject.InjectionTools;
import com.lithium.inject.Injector;

public class ConfiguredDependencySupplier extends DependencySupplier {
	
	Method method;
	Object configInstance;
	
	public ConfiguredDependencySupplier(Method method, Object configInstance) {
		super(method.getReturnType());
		this.method = method;
		this.configInstance = configInstance;
	}

	@Override
	List<Class<?>> loadSubDependencies() {
		Class<?>[] types = method.getParameterTypes();
		return Arrays.asList(types);
	}

	@Override
	public Supplier<Object> generateSupplier(Injector injector) {
		if(supplier == null){
			Object[] args = injector.getDependencies(method.getParameterTypes());
			Object instance = Modifier.isStatic(method.getModifiers())? null : configInstance;
			
			InjectionTools tools =  new InjectionTools();
			supplier = () -> tools.invokeMethod(instance, method, args);
		}
		return supplier;
	}

}
