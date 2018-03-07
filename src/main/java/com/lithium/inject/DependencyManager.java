package com.lithium.inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;
import com.lithium.dependency.exceptions.AmbiguousDependencyException;
import com.lithium.dependency.exceptions.DependencyCreationException;
import com.lithium.scanner.ClassScanner;

public class DependencyManager {
	
	private final Map<Class<?>, Supplier<Object>> dependencies = new HashMap<>();
	private final List<Class<?>> classes = ClassScanner.getClassPath().getClassesWithAnnotation(Dependency.class);
	private InjectionTools tools = new InjectionTools();
	private Injector injector;
	
	
	DependencyManager(Injector injector){ 
		this.injector = injector;
	}
	
	Map<Class<?>, Supplier<Object>> loadDependencies(){
		for(int i = 0; i < classes.size(); i++){
			Class<?> dep = classes.get(i);
			
			if(subDependenciesLoaded(dep)){
				loadDependency(dep);
				classes.remove(i--);
			}
			else subDependenciesQueued(dep);
		}
		if(!classes.isEmpty()) return loadDependencies();
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
	 * the actual dependency class, or any interface it implements.
	 * @param instance The supplier to use to get an instance of this
	 * dependency.
	 * @throws DependencyCreationException If a dependency already exists
	 * for the specified class
	 */
	private void loadDependency(Class<?> dep, Supplier<Object> instance, boolean concrete){
		boolean exists = dependencies.containsKey(dep);
		if(exists && concrete) throw new DependencyCreationException("Dependency already exists for class.", dep);
		else if(exists) dependencies.put(dep, () -> new AmbiguousDependencyException(dep));
		else dependencies.put(dep, instance);
		
	}
	
	private boolean subDependenciesLoaded(Class<?> parent){
		List<Class<?>> subDependencies = tools.getSubDependencies(parent);
		
		for (Class<?> c : subDependencies) {
			if(!dependencies.containsKey(c)) return false;
		}
		
		return true;
	}
	
	private void subDependenciesQueued(Class<?> parent){
		StringBuilder missing = new StringBuilder("Missing dependencies : ");
		List<Class<?>> subDependencies = tools.getSubDependencies(parent);
		
		for (Class<?> c : subDependencies) {
			if(!classes.contains(c)) missing.append(c.getSimpleName() + ", ");
		}
		
		if(missing.length() > 23) throw new DependencyCreationException(missing.substring(0, missing.length() - 2), parent);
	}

}
