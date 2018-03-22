package org.proton_di.test.cases;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.proton_di.scanner.ClassPath;
import org.proton_di.test.dependencies.MultipleDependency;
import org.proton_di.test.dependencies.SingletonDependency;
import org.proton_di.test.suites.InjectionSuiteRunner;
import org.proton_di.test.suites.InjectionTestSuite;

import java.io.Serializable;
import java.util.List;

import static org.junit.Assert.*;

public class TestClassPath {
	
	ClassPath classpath = ClassPath.getInstance();

	@Test
	public void filterByAnnotation(){
		List<Class<?>> classes = classpath.getClassesWithAnnotation(RunWith.class);
		assertFalse("List of classes annotated by RunWith is empty", classes.isEmpty());
		assertTrue("First element actually " + classes.get(0), classes.get(0).equals(InjectionTestSuite.class));
	}
	
	@Test
	public void filterByPackage(){
		List<Class<?>> classes = classpath.getClassesInPackage("org.proton_di.test.suites");
		assertTrue("Expected 2 classes in package. Actually " + classes.size(), classes.size() == 2);
		assertTrue("List does not contain InjectionTestSuite.class", classes.contains(InjectionTestSuite.class));
		assertTrue("List does not contain InjectionSuiteRunner.class", classes.contains(InjectionSuiteRunner.class));
	}
	
	@Test
	public void filterByInterface(){
		List<Class<?>> classes = classpath.getImplementingClasses(Serializable.class);
		assertFalse("List of classes implementing Serializable is empty", classes.isEmpty());
		assertTrue("List does not contain MultipleDependency", classes.contains(MultipleDependency.class));
		assertTrue("List does not contain SingletonDependency", classes.contains(SingletonDependency.class));
	}
}
