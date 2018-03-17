package com.lithium.dependency.loaders;

import java.util.List;

import com.lithium.configuration.Qualifier;
import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;
import com.lithium.inject.InjectionManager;

/**
 * A Dependency Loader that loads classes annotated
 * with <code>@</code>{@link Dependency} as dependencies.
 * 
 * @author Luke Stevens
 */
public class InternalDependencyLoader extends AbstractDependencyLoader {
	
	@Override
	boolean shouldLoad(Class<?> c){
		Qualifier q = c.getAnnotation(Qualifier.class);
		return q == null || q.value().equals(InjectionManager.ROOT_QUALIFIER);
	}

	@Override
	List<Class<?>> getClasses() {
		return classpath.getClassesWithAnnotation(Dependency.class);
	}

	@Override
	void loadClass(Class<?> c) {
		InstanceType type = c.getAnnotation(Dependency.class).type();
		dependencySuppliers.add(type.createSupplier(c));
	}

}
