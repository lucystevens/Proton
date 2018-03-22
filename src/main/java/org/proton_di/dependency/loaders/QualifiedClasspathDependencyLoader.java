package org.proton_di.dependency.loaders;

import org.proton_di.configuration.Qualifier;

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
