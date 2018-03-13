package com.lithium.dependency.loaders;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.lithium.configuration.Configuration;
import com.lithium.configuration.ConfigurationException;
import com.lithium.dependency.suppliers.ConfiguredDependencySupplier;
import com.lithium.dependency.suppliers.DependencySupplier;
import com.lithium.inject.InjectionTools;
import com.lithium.scanner.ClassPath;
import com.lithium.scanner.ClassScanner;

public class ExternalDependencyLoader implements DependencyLoader {
	
	private final ClassPath classpath = ClassScanner.getClassPath();
	private final InjectionTools tools = new InjectionTools();
	private final List<DependencySupplier> dependencySuppliers = new ArrayList<>();

	@Override
	public List<DependencySupplier> getDependencies() {
		List<Class<?>> configs = classpath.getClassesWithAnnotation(Configuration.class);
		configs.forEach(this::loadExternalDependencies);
		return dependencySuppliers;
	}
	
	/**
	 * Loads all external dependencies from a single
	 * configuration class.
	 * @param configClass The configuration class to load external
	 * dependencies from.
	 */
	private void loadExternalDependencies(Class<?> configClass){
		if(configClass.isInterface()) {
			throw new ConfigurationException("The configuration class must not be abstract or an interface", configClass);
		}
		
		Object config = tools.construct(configClass);
				
		for(Method m : configClass.getDeclaredMethods()){
			dependencySuppliers.add(new ConfiguredDependencySupplier(m, config));
		}
	}

}
