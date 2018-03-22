package org.proton_di.dependency.loaders;

import java.lang.reflect.Method;
import java.util.List;

import org.proton_di.configuration.Configuration;
import org.proton_di.configuration.ConfigurationException;
import org.proton_di.dependency.suppliers.ConfiguredDependencySupplier;
import org.proton_di.inject.InjectionManager;
import org.proton_di.inject.InjectionTools;

/**
 * A ClasspathDependencyLoader that loads dependencies from the
 * classes annotated with <code>@</code>{@link Configuration}.
 * 
 * @author Luke Stevens
 */
public class ConfigurationDependencyLoader extends AbstractDependencyLoader {
	
	final InjectionTools tools = new InjectionTools();
	String qualifier;
	
	public ConfigurationDependencyLoader() {
		this(InjectionManager.ROOT_QUALIFIER);
	}
	
	public ConfigurationDependencyLoader(String qualifier) {
		this.qualifier = qualifier;
	}

	@Override
	List<Class<?>> getClasses() {
		return classpath.getClassesWithAnnotation(Configuration.class);
	}

	@Override
	boolean shouldLoad(Class<?> c) {
		return c.getAnnotation(Configuration.class).qualifier().equals(qualifier);
	}

	/**
	 * Loads all external dependencies from a single
	 * configuration class.
	 * @param configClass The configuration class to load external
	 * dependencies from.
	 */
	@Override
	void loadClass(Class<?> c) {
		if(c.isInterface()) {
			throw new ConfigurationException("The configuration class must not be abstract or an interface", c);
		}
		
		Object config = tools.construct(c, new Class<?>[0], new Object[0]);
				
		for(Method m : c.getDeclaredMethods()){
			dependencySuppliers.add(new ConfiguredDependencySupplier(m, config));
		}
	}

}
