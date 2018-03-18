package com.lithium.inject;

import java.util.function.Supplier;

import com.lithium.dependency.exceptions.MissingDependencyException;
import com.lithium.dependency.loaders.ConfigurationDependencyLoader;
import com.lithium.dependency.loaders.ClasspathDependencyLoader;

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
