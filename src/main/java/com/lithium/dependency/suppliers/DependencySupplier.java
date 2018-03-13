package com.lithium.dependency.suppliers;

import java.util.List;
import java.util.function.Supplier;

import com.lithium.dependency.exceptions.DependencyCreationException;
import com.lithium.inject.Injector;

public abstract class DependencySupplier {
	
	Class<?> dependency;
	List<Class<?>> subDependencies;
	Supplier<Object> supplier;
	
	public DependencySupplier(Class<?> dependency) {
		this.dependency = dependency;
	}
	
	abstract List<Class<?>> loadSubDependencies();
	
	public List<Class<?>> getSubDependencies(){
		if(subDependencies == null){
			this.subDependencies = loadSubDependencies();
		}
		return subDependencies;
	}
	
	public abstract Supplier<Object> generateSupplier(Injector injector);
	
	public Object getInstance(){
		return supplier.get();
	}
	
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
			if(!(toBeLoaded(c, toInit) || injector.hasDependency(c))) missing.append(c.getSimpleName() + ", ");
		}
		
		if(missing.length() > 23) throw new DependencyCreationException(missing.substring(0, missing.length() - 2), dependency);
	}
	
	/**
	 * Determines if a DependencySupplier corresponding to a
	 * class is in a list of dependency suppliers to be loaded.
	 * @param c The class
	 * @param toInit The list of Dependency Suppliers to be initialised
	 * @return True if in the list, false if not.
	 */
	boolean toBeLoaded(Class<?> c, List<DependencySupplier> toInit){
		for(DependencySupplier dep : toInit){
			if(dep.getDependencyClass().equals(c)) return true;
		}
		return false;
	}

}
