package com.lithium.test.cases;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.lithium.inject.InjectionManager;
import com.lithium.inject.Injector;
import com.lithium.scanner.ClassPath;
import com.lithium.test.dependencies.MultipleDependency;
import com.lithium.test.dependencies.SingletonDependency;
import com.lithium.test.suites.InjectionTestSuite;

import java.io.Serializable;
import java.util.List;

import static org.junit.Assert.*;

public class TestClassPath {
	
	ClassPath classpath = ClassPath.getInstance();
	Injector injector = InjectionManager.getDefaultInjector();

	@Test
	public void filterByAnnotation(){
		List<Class<?>> classes = classpath.getClassesWithAnnotation(RunWith.class);
		assertFalse("List of classes annotated by RunWith is empty", classes.isEmpty());
		assertTrue("First element actually " + classes.get(0), classes.get(0).equals(InjectionTestSuite.class));
	}
	
	@Test
	public void filterByPackage(){
		List<Class<?>> classes = classpath.getClassesInPackage("com.lithium.test.suites");
		assertFalse("List of classes in package is empty", classes.isEmpty());
		assertTrue("First element actually " + classes.get(0), classes.get(0).equals(InjectionTestSuite.class));
	}
	
	@Test
	public void filterByInterface(){
		List<Class<?>> classes = classpath.getImplementingClasses(Serializable.class);
		assertFalse("List of classes implementing Serializable is empty", classes.isEmpty());
		assertTrue("List does not contain MultipleDependency", classes.contains(MultipleDependency.class));
		assertTrue("List does not contain SingletonDependency", classes.contains(SingletonDependency.class));
	}
}
