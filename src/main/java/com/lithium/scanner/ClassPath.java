package com.lithium.scanner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ClassPath {
	
	private List<Class<?>> classes;
	
	ClassPath(List<Class<?>> classes) {
		this.classes = classes;
	}
	
	public List<Class<?>> getClasses(){
		return classes;
	}
	
	public List<Class<?>> getClasses(Predicate<Class<?>> filter){
		List<Class<?>> results = new ArrayList<>();
		for(Class<?> c : classes){
			if(filter.test(c)) results.add(c);
		}
		return results;
	}
	
	public List<Class<?>> getClassesWithAnnotation(Class<? extends Annotation> a){
		Predicate<Class<?>> filter = c -> c.getAnnotation(a) != null;
		return getClasses(filter);
	}
	
	public List<Class<?>> getClassesInPackage(String packageName){
		Predicate<Class<?>> filter = c -> c.getName().startsWith(packageName);
		return getClasses(filter);
	}
	
	public List<Class<?>> getImplementingClasses(Class<?> iface){
		if(iface.isInterface()) {
			Predicate<Class<?>> filter = c -> Arrays.asList(c.getInterfaces()).contains(iface);
			return getClasses(filter);
		}
		else return new ArrayList<>();
	}

}
