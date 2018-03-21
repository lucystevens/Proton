package com.lithium.inject;

import java.util.function.Supplier;

import com.lithium.dependency.loaders.ConfigurationDependencyLoader;
import com.lithium.dependency.loaders.QualifiedClasspathDependencyLoader;

class QualifiedInjector extends AbstractInjector {
	
	String qualifier;
	
	QualifiedInjector(String qualifier){
		super(new ConfigurationDependencyLoader(qualifier), new QualifiedClasspathDependencyLoader(qualifier));
		this.dependencies.put(QualifiedInjector.class, () -> this);
	}

	@Override
	public <T> T getDependency(Class<T> c){
		Supplier<Object> instance = dependencies.get(c);
		if(instance == null) return root().getDependency(c);
		else return c.cast(instance.get());
	}
	
	@Override
	public boolean hasDependency(Class<?> c){
		return dependencies.containsKey(c) || root().hasDependency(c);
	}
	
	// Convinience method to get the root injector
	private Injector root(){
		return InjectionManager.getRootInjector();
	}
}
