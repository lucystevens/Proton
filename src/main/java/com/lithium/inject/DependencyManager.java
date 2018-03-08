package com.lithium.inject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;
import com.lithium.dependency.exceptions.AmbiguousDependencyException;
import com.lithium.dependency.exceptions.DependencyCreationException;
import com.lithium.scanner.ClassPath;
import com.lithium.scanner.ClassScanner;

/**
 * A class for managing the initial loading of dependencies
 * and injection of static fields.
 * 
 * @author Luke Stevens
 *
 */
public class DependencyManager {
	
	private final Map<Class<?>, Supplier<Object>> dependencies = new HashMap<>();
	private final List<Class<?>> classesToLoad = ClassScanner.getClassPath().getClassesWithAnnotation(Dependency.class);
	private InjectionTools tools = new InjectionTools();
	private Injector injector;
	
	/**
	 * Constructs a new DependencyManager using the injector
	 * @param injector The injector this constructor should be called from
	 */
	DependencyManager(Injector injector){ 
		this.injector = injector;
	}
	
	/**
	 * Loads all classesToLoad marked as dependencies from the classpath
	 * and injects necessary dependencies into them.
	 * @return A Map of dependency classesToLoad to functions that 
	 * supply their objects.
	 */
	Map<Class<?>, Supplier<Object>> loadDependencies(){
		
		// Loops through all dependencies left to load
		for(int i = 0; i < classesToLoad.size(); i++){
			Class<?> dep = classesToLoad.get(i);
			
			// If all sub dependencies are loaded, load dependency and remove from list
			if(subDependenciesLoaded(dep)){
				loadDependency(dep);
				classesToLoad.remove(i--);
			}
			
			// Otherwise check the sub dependencies are valid
			else validateSubDependencies(dep);
		}
		
		// If there are no more dependencies to load return the current map
		if(!classesToLoad.isEmpty()) return loadDependencies();
		else return dependencies;
	}
	
	/**
	 * Loads the supplier for a dependency class and stores
	 * in the dependency map for the class and all interfaces and superclasses.
	 * @param c The class to load as a dependency. Must be
	 * annotated with <code>@Dependency</code>
	 */
	private void loadDependency(Class<?> dep){
		InstanceType type = dep.getAnnotation(Dependency.class).type();
		Supplier<Object> instance = injector.getSupplier(dep, type);
		
		// Load concrete dependency class
		loadDependency(dep, instance, true);
		
		// Load superclasses
		for(Class<?> superclass : tools.getSuperClasses(dep)){
			loadDependency(superclass, instance, false);
		}
		
		// Load implementing interfaces
		for(Class<?> iface : dep.getInterfaces()){
			loadDependency(iface, instance, false);
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
	private void loadDependency(Class<?> dep, Supplier<Object> instance, boolean concrete){
		boolean exists = dependencies.containsKey(dep);
		
		// If the concrete dependency already exists, throw an exception
		if(exists && concrete) throw new DependencyCreationException("Dependency already exists for class.", dep);
		
		// If it exists, but isn't concrete, store an exception to throw if retrieval is attempted
		else if(exists) dependencies.put(dep, () -> new AmbiguousDependencyException(dep));
		
		// Otherwise store dependency and supplier
		else dependencies.put(dep, instance);
		
	}
	
	/**
	 * Checks if all sub dependencies for a parent dependency
	 * are loaded.
	 * @param parent The parent dependency class
	 * @return True if all sub dependencies to be injected
	 * have suppliers in the dependency map. False if not.
	 */
	private boolean subDependenciesLoaded(Class<?> parent){
		List<Class<?>> subDependencies = tools.getSubDependencies(parent);
		
		for (Class<?> c : subDependencies) {
			if(!dependencies.containsKey(c)) return false;
		}
		
		return true;
	}
	
	/**
	 * Checks the queue of classes to load to ensure all
	 * sub dependencies of the parent are valid and queued
	 * to be loaded.
	 * @param parent The parent dependency class
	 * @throws DependencyCreationException If any of the required
	 * sub dependencies have not been registered as dependencies.
	 */
	private void validateSubDependencies(Class<?> parent){
		StringBuilder missing = new StringBuilder("Missing dependencies : ");
		List<Class<?>> subDependencies = tools.getSubDependencies(parent);
		
		for (Class<?> c : subDependencies) {
			if(!classesToLoad.contains(c)) missing.append(c.getSimpleName() + ", ");
		}
		
		if(missing.length() > 23) throw new DependencyCreationException(missing.substring(0, missing.length() - 2), parent);
	}
	
	/**
	 * Scans all classes on the class path and, for each
	 * class found, injects dependencies into static
	 * fields annotated by the <code>@Inject</code> annotation. 
	 */
	void injectStaticFields(){
		ClassPath classpath = ClassScanner.getClassPath();
		for(Class<?> c : classpath.getClasses()){
			scanFields(c);
		}
	}
	
	/**
	 * Scans all fields on a class and, for each eligible field,
	 * sets it's value to an appropriate dependency.
	 * @param c The class to scan.
	 */
	private void scanFields(Class<?> c){
		for(Field f : c.getDeclaredFields()){
			if(tools.isInjectable(f, true)) injector.injectIntoField(f, null);
		}
	}

}
