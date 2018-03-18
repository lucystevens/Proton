package com.lithium.dependency.loaders;

import com.lithium.configuration.Qualifier;

public class QualifiedClasspathDependencyLoader extends ClasspathDependencyLoader {
	
	String qualifier;
	
	public QualifiedClasspathDependencyLoader(String qualifier){
		this.qualifier = qualifier;
	}
	
	@Override
	boolean shouldLoad(Class<?> c) {
		Qualifier q = c.getAnnotation(Qualifier.class);
		return q != null && q.value().equals(qualifier);
	}

}
