package org.proton_di.inject;

import java.util.function.Supplier;

import org.proton_di.dependency.exceptions.MissingDependencyException;
import org.proton_di.dependency.loaders.ClasspathDependencyLoader;
import org.proton_di.dependency.loaders.ConfigurationDependencyLoader;

/**
 * Singleton class for getting and injecting
 * dependencies.<br>
 * On being loaded this class will
 * create a single Injector instance which scans
 * all classes on the classpath an injects dependencies
 * into static fields marked with <code>@Inject</code>
 * 
 * @author Luke Stevens
 */
class RootInjector extends AbstractInjector{
	
	/**
	 * Creates the single instance of this Injector,
	 * by scanning all classes on the classpath and
	 * injected static dependencies where appropriate.
	 */
	RootInjector(){
		super(new ConfigurationDependencyLoader(), new ClasspathDependencyLoader());
		this.dependencies.put(RootInjector.class, () -> this);
	}
	
	@Override
	public <T> T getDependency(Class<T> c){
		Supplier<Object> instance = dependencies.get(c);
		if(instance == null) throw new MissingDependencyException(c);
		
		return c.cast(instance.get());
	}
	
	@Override
	public boolean hasDependency(Class<?> c){
		return dependencies.containsKey(c);
	}

}
