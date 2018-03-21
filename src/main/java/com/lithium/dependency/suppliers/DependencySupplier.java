package com.lithium.dependency.suppliers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.lithium.dependency.exceptions.DependencyCreationException;
import com.lithium.inject.InjectionTools;
import com.lithium.inject.Injector;

/**
 * An abstract class to represent dependencies, 
 * their suppliers, and convienience methods
 * 
 * @author Luke Stevens
 */
public abstract class DependencySupplier {
	
	Class<?> dependency;
	List<Class<?>> subDependencies;
	Supplier<Object> supplier;
	
	InjectionTools tools = new InjectionTools();
	
	/**
	 * Constructs a new Dependency supplier for
	 * a specified class.<br>
	 * <i>Note: Only one DependencySupplier should be created for
	 * each class</i>
	 * @param dependency The class to construct
	 * a Dependency supplier for.
	 */
	DependencySupplier(Class<?> dependency) {
		this.dependency = dependency;
	}
	
	/**
	 * Generates the supplier for this dependency
	 * @param injector The injector used to supply any
	 * sub dependencies needed for initialisation
	 * @return A supplier to return the correct instance 
	 * of the dependency.
	 */
	abstract Supplier<Object> generateSupplier(Injector injector);
	
	/**
	 * Gets the supplier for this dependency
	 * @param injector The injector used to supply any
	 * sub dependencies needed for initialisation
	 * @return A supplier to return the correct instance 
	 * of the dependency.
	 */
	public Supplier<Object> getSupplier(Injector injector){
		if(supplier == null){
			supplier = generateSupplier(injector);
		}
		return supplier;
	}
	
	
	@Override
	public String toString() {
		return dependency.getName();
	}
	
	/**
	 * Default method of loading sub dependencies.
	 * Adds types of fields annotated with <code>@Inject</code>
	 * and the types parameters for the constructor to use.
	 */
	void loadSubDependencies(){
		subDependencies = new ArrayList<>();
		
		// Get field sub dependencies
		for(Field f : tools.getAllInjectableFields(dependency)){
			subDependencies.add(f.getType());
		}
		
		// Get constructor sub dependencies
		for(Class<?> c : tools.getConstructorTypes(dependency)){
			subDependencies.add(c);
		}
	}
	
	/**
	 * @return A list of all sub dependencies this
	 * dependency depends upon
	 */
	public List<Class<?>> getSubDependencies(){
		if(subDependencies == null){
			loadSubDependencies();
		}
		return subDependencies;
	}
	
	/**
	 * @return The required instance of this object from
	 * the generated supplier.
	 */
	public Object getInstance(){
		return supplier.get();
	}
	
	/**
	 * @return The dependency class this supplier wraps
	 */
	public Class<?> getDependencyClass(){
		return dependency;
	}
	
	/**
	 * Checks if all sub dependencies for a parent dependency
	 * are loaded.
	 * @param parent The parent dependency class
	 * @return True if all sub dependencies to be injected
	 * have suppliers in the dependency map. False if not.
	 */
	public boolean subDependenciesLoaded(Injector injector){	
		for (Class<?> c : getSubDependencies()) {
			if(!injector.hasDependency(c)) return false;
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
	public void validateSubDependencies(List<DependencySupplier> toInit, Injector injector){
		StringBuilder missing = new StringBuilder("Missing dependencies : ");
		
		for (Class<?> c : getSubDependencies()) {
			if(!(tools.toBeLoaded(c, toInit) || injector.hasDependency(c))) missing.append(c.getSimpleName() + ", ");
		}
		
		if(missing.length() > 23) throw new DependencyCreationException(missing.substring(0, missing.length() - 2), dependency);
	}
	
	/**
	 * @return A List of assignable classes (superclasses
	 * and interfaces) for this dependency
	 */
	public List<Class<?>> getAssignableClasses(){
		List<Class<?>> ifaces = Arrays.asList(dependency.getInterfaces());
		List<Class<?>> assignableClasses = new ArrayList<>(ifaces);
		
		// Recursively adds superclasses up the hierarchy
		Class<?> superclass = dependency.getSuperclass();
		while(superclass != null){
			assignableClasses.add(superclass);
			assignableClasses.addAll(Arrays.asList(superclass.getInterfaces()));
			superclass = superclass.getSuperclass();
		}
		
		return assignableClasses;
	}

}
