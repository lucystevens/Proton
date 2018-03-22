package org.proton_di.dependency.loaders;

import java.util.List;

import org.proton_di.configuration.Qualifier;
import org.proton_di.dependency.Dependency;
import org.proton_di.dependency.InstanceType;
import org.proton_di.inject.InjectionManager;

/**
 * A Dependency Loader that loads classes annotated
 * with <code>@</code>{@link Dependency} as dependencies.
 * 
 * @author Luke Stevens
 */
public class ClasspathDependencyLoader extends AbstractDependencyLoader {
	
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
