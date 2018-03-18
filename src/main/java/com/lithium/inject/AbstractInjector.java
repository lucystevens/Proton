package com.lithium.inject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.lithium.configuration.Configuration;
import com.lithium.dependency.exceptions.AmbiguousDependencyException;
import com.lithium.dependency.exceptions.DependencyCreationException;
import com.lithium.dependency.loaders.DependencyLoader;
import com.lithium.dependency.suppliers.DependencySupplier;
import com.lithium.inject.config.InjectableObject;
import com.lithium.inject.exceptions.InjectionException;
import com.lithium.scanner.ClassPath;

abstract class AbstractInjector implements Injector{
	
	final Map<Class<?>, Supplier<Object>> dependencies = new HashMap<>();
	final InjectionTools tools = new InjectionTools();
	final ClassPath classpath = ClassPath.getInstance();
	
	/**
	 * Creates the single instance of this Injector,
	 * by scanning all classes on the classpath and
	 * injected static dependencies where appropriate.
	 */
	AbstractInjector(DependencyLoader...loaders){
		loadAllDependencies(loaders);
		
		this.dependencies.put(ClassPath.class, ClassPath::getInstance);
		this.dependencies.put(AbstractInjector.class, () -> this);
		this.dependencies.put(Injector.class, () -> this);
	}
	
	/**
	 * Loads all dependencies using the provided dependency
	 * loaders.
	 * @param loaders An array of dependency loaders used to 
	 * load dependencies from different sources.
	 */
	void loadAllDependencies(DependencyLoader...loaders){
		List<DependencySupplier> toInitialise = new ArrayList<>();
		for (DependencyLoader dependencyLoader : loaders) {
			toInitialise.addAll(dependencyLoader.getDependencies());
		}
		initialiseDependencies(toInitialise);
	}
	
	/**
	 * Recursively initialises all loaded dependencies until
	 * there are no more to intialise.
	 * @param toInitialise A List of loaded DependencySuppliers to initalise.
	 */
	void initialiseDependencies(List<DependencySupplier> toInitialise){
		List<DependencySupplier> loadedSuppliers = new ArrayList<>();
		
		// Loops through all dependencies left to load
		for(DependencySupplier dep : toInitialise){
			
			// If all sub dependencies are loaded, load dependency
			// and add to list to remove
			if(dep.subDependenciesLoaded(this)){
				loadDependency(dep);
				loadedSuppliers.add(dep);
			}
			
			// Otherwise check the sub dependencies are valid
			else dep.validateSubDependencies(toInitialise, this);
		}
		
		// If no classes have been loaded, but there are still classes to load, dependency injection is stuck in a loop
		if(loadedSuppliers.isEmpty() && !toInitialise.isEmpty()) {
			throw new DependencyCreationException("Dependency loop detected. Failing dependencies: " + toInitialise);
		}
		
		// Remove loaded classes
		toInitialise.removeAll(loadedSuppliers);
		
		// If there are no more dependencies to load return the current map
		if(!toInitialise.isEmpty()) initialiseDependencies(toInitialise);
	}
	
	/**
	 * Generates the supplier for a dependency supplier and stores
	 * in the dependency map for the class and all interfaces and superclasses.
	 * @param c The DependencySupplier to load as a dependency.
	 */
	void loadDependency(DependencySupplier dep){	
		// Load concrete dependency class
		loadDependency(dep.getDependencyClass(), dep.getSupplier(this), true);
		
		// Loads assignable classes (super and interfaces)
		for(Class<?> assignableClass : dep.getAssignableClasses()){
			loadDependency(assignableClass, dep.getSupplier(this), false);
		}
	}
	
	/**
	 * Loads a dependency into the dependency map
	 * @param d The class to store the dependency under. This may be 
	 * the actual dependency class, or any interface/superclass it implements.
	 * @param instance The supplier to use to get an instance of this
	 * dependency.
	 * @param concrete Whether this is the concrete class or an interface/superclass
	 * @throws DependencyCreationException If a dependency already exists
	 * for the specified concrete class
	 */
	void loadDependency(Class<?> dep, Supplier<Object> instance, boolean concrete){
		boolean exists = hasDependency(dep);
		
		// If the concrete dependency already exists, throw an exception
		if(exists && concrete) throw new DependencyCreationException("Dependency already exists for class.", dep);
		
		// If it exists, but isn't concrete, store an exception to throw if retrieval is attempted
		else if(exists) dependencies.put(dep, () -> { throw new AmbiguousDependencyException(dep); });
		
		// Otherwise store dependency and supplier
		else dependencies.put(dep, instance);
		
	}
	
	/**
	 * Scans all fields on a class and, for each eligible field,
	 * sets it's value to an appropriate dependency.
	 * @param c The class to scan.
	 */
	public void injectIntoStaticFields(Class<?> c){
		Field[] fields = new Field[0];
		
		// Classes far down the dependency chain sometimes have 
		// fields which reference missing classes. Ignore these.
		try{
			fields = c.getDeclaredFields();
		} catch(NoClassDefFoundError e){
			// Ignore classes with missing fields
		}
		
		for(Field f : fields){
			if(tools.isInjectable(f, true)) injectIntoField(f, null);
		}
	}
	
	/**
	 * Injects a dependency into an injectable field.
	 * @param f The field to inject into. Must be
	 * annotated with <code>@Inject</code>
	 * @param o The object instance to inject into. If used
	 * for static dependencies, this should be null.
	 */
	void injectIntoField(Field f, Object o){
		try{
			Object dependency = getDependency(f.getType());
			f.setAccessible(true);
			f.set(o, dependency);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new InjectionException(e, f.getClass());
		}
	}
	
	/**
	 * Converts an array of classes to an array of dependencies
	 * by retrieving the dependency associated with each class.
	 * @param classes An array of classes
	 * @return An array of dependencies
	 */
	public Object[] getDependencies(Class<?>[] classes){
		Object[] params = new Object[classes.length];
		for(int i = 0; i < classes.length; i++){
			params[i] = getDependency(classes[i]);
		}
		return params;
	}
	
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
	public abstract <T> T getDependency(Class<T> c);
	
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
	public void injectDependencies(Object o){
		Class<?> c = o.getClass();
		for(Field f : c.getDeclaredFields()){
			if(tools.isInjectable(f, false)) injectIntoField(f, o);
		}
	}
	
	/**
	 * Gets a new instance of the specified class:
	 * If there is a constructor annotated with <code>@Inject</code>
	 * then this will be used and the arguments will be injected as 
	 * dependencies. Otherwise the default constructor will be used.
	 * @param c The class to construct an instance of.
	 * @return An instance of the Class c
	 */
	public <T> T newInstance(Class<T> c) {
		Class<?>[] classes = tools.getConstructorTypes(c);

		Object[] params = getDependencies(classes);
		T instance =  tools.construct(c, params);
				
		injectDependencies(instance);
		return instance;
	}
	
	/**
	 * @return Whether this Injector has a dependency stored for this class.
	 */
	public abstract boolean hasDependency(Class<?> c);

}
