package com.lithium.inject;

import com.lithium.configuration.Configuration;
import com.lithium.dependency.exceptions.DependencyCreationException;
import com.lithium.inject.config.InjectableObject;

public interface Injector {
	
	/**
	 * Converts an array of classes to an array of dependencies
	 * by retrieving the dependency associated with each class.
	 * @param classes An array of classes
	 * @return An array of dependencies
	 */
	public Object[] getDependencies(Class<?>[] classes);
	
	/**
	 * Gets an instance of a registered dependency.
	 * This can either have been registered automatically
	 * using the <code>@Dependency</code> annotation or
	 * manually using a {@link Configuration} class.
	 * @param c The class to load the dependency for. Note
	 * this can be an interface that the dependency implements.
	 * @return An instance of the class specified.
	 * @throws DependencyCreationException If there is no stored
	 * dependency for the class, and a new one cannot be loaded.
	 */
	public <T> T getDependency(Class<T> c);
	
	/**
	 * Injects dependencies into an already instantiated 
	 * object. This scans every field within the Object 
	 * for those annotated with <code>@Inject</code> and
	 * sets the value of those fields to a relevant dependency.<br><br>
	 * <i>Note: this method is called during instantiation for
	 * any Object extending {@link InjectableObject} and so
	 * does not need to be called manually for those Objects</i>
	 * @param o The object to inject dependencies into.
	 */
	public void injectDependencies(Object o);
	
	/**
	 * Gets a new instance of the specified class:
	 * If there is a constructor annotated with <code>@Inject</code>
	 * then this will be used and the arguments will be injected as 
	 * dependencies. Otherwise the default constructor will be used.
	 * @param c The class to construct an instance of.
	 * @return An instance of the Class c
	 */
	public <T> T newInstance(Class<T> c);
	
	/**
	 * @return Whether this Injector has a dependency stored for this class.
	 */
	public boolean hasDependency(Class<?> c);
	
	
	/**
	 * Scans all fields on a class and, for each eligible static field,
	 * sets it's value to an appropriate dependency.
	 * @param c The class to scan.
	 */
	public void injectIntoStaticFields(Class<?> c);

}
