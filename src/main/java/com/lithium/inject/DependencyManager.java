package com.lithium.inject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.lithium.configuration.Configuration;
import com.lithium.configuration.ConfigurationException;
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
	private final ClassPath classpath = ClassScanner.getClassPath();
	private final List<Class<?>> classesToLoad = classpath.getClassesWithAnnotation(Dependency.class);
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
	 * Loads all dependencies, both external (from configuration)
	 * and internal (from classpath)
	 * @return A Map of dependency classes to functions that 
	 * supply their objects.
	 */
	Map<Class<?>, Supplier<Object>> loadAllDependencies(){
		loadExternalDependencies();
		loadDependencies();
		return dependencies;
	}
	
	/**
	 * Loads all external dependencies defined in
	 * <code>@Configuration</code>-annotated classes.
	 */
	private void loadExternalDependencies(){
		List<Class<?>> configs = classpath.getClassesWithAnnotation(Configuration.class);
		configs.forEach(this::loadExternalDependencies);
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
			loadExternalDependency(config, m);
		}
	}
	
	/**
	 * Loads a single external dependency from a
	 * method in a configuration class.
	 * @param parent A concrete instance of the configuration class.
	 * @param m The method to use to supply an instance of the dependency.
	 */
	private void loadExternalDependency(Object parent, Method m){
		if(m.getParameterTypes().length != 0){
			throw new ConfigurationException("Configuration methods must not have any parameters.", parent.getClass());
		}
		
		Class<?> depClass = m.getReturnType();
		Object instance = Modifier.isStatic(m.getModifiers())? null : parent;
		Supplier<Object> supplier = () -> tools.invokeMethod(instance, m);
		
		loadDependency(depClass, supplier);
	}
	
	/**
	 * Loads all classesToLoad marked as dependencies from the classpath
	 * and injects necessary dependencies into them.
	 */
	private void loadDependencies(){
		List<Class<?>> loadedClasses = new ArrayList<>();
		
		// Loops through all dependencies left to load
		for(Class<?> dep : classesToLoad){
			
			// If all sub dependencies are loaded, load dependency
			// and add to list to remove
			if(subDependenciesLoaded(dep)){
				loadDependency(dep);
				loadedClasses.add(dep);
			}
			
			// Otherwise check the sub dependencies are valid
			else validateSubDependencies(dep);
		}
		
		// If no classes have been loaded, dependency injection is stuck in a loop
		if(loadedClasses.isEmpty()) {
			throw new DependencyCreationException("Dependency loop detected. Failing dependencies: " + classesToLoad);
		}
		
		// Remove loaded classes
		classesToLoad.removeAll(loadedClasses);
		
		// If there are no more dependencies to load return the current map
		if(!classesToLoad.isEmpty()) loadDependencies();
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
		loadDependency(dep, instance);
	}
	
	private void loadDependency(Class<?> dep, Supplier<Object> instance){	
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
			if(!classesToLoad.contains(c) && !dependencies.containsKey(c)) missing.append(c.getSimpleName() + ", ");
		}
		
		if(missing.length() > 23) throw new DependencyCreationException(missing.substring(0, missing.length() - 2), parent);
	}
	
	/**
	 * Scans all classes on the class path and, for each
	 * class found, injects dependencies into static
	 * fields annotated by the <code>@Inject</code> annotation. 
	 */
	void injectStaticFields(){
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
