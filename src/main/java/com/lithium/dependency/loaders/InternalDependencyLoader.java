package com.lithium.dependency.loaders;

import java.util.ArrayList;
import java.util.List;

import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;
import com.lithium.dependency.suppliers.DependencySupplier;
import com.lithium.scanner.ClassPath;
import com.lithium.scanner.ClassScanner;

public class InternalDependencyLoader implements DependencyLoader {
	
	private final ClassPath classpath = ClassScanner.getClassPath();
	private final List<DependencySupplier> dependencySuppliers = new ArrayList<>();

	@Override
	public List<DependencySupplier> getDependencies() {
		List<Class<?>> classes = classpath.getClassesWithAnnotation(Dependency.class);
		classes.forEach(this::loadInternalDependency);
		return dependencySuppliers;
	}
	
	private void loadInternalDependency(Class<?> depClass){
		InstanceType type = depClass.getAnnotation(Dependency.class).type();
		dependencySuppliers.add(type.createSupplier(depClass));
	}

}
