package com.lithium.test.cases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.lithium.inject.Injector;
import com.lithium.inject.config.Inject;
import com.lithium.test.dependencies.DependencyWithDependencies;
import com.lithium.test.dependencies.MultipleDependency;
import com.lithium.test.dependencies.SingletonDependency;

public class TestDependenciesWithDependencies {
	
	@Inject
	static Injector injector;
	
	DependencyWithDependencies d = injector.getDependency(DependencyWithDependencies.class);
	
	@Test
	public void testSingletonDependencyInField(){
		SingletonDependency s = d.singletonFieldTest();
		assertNotNull("Dependency not injected", s);
		assertTrue("New instance of dependency", s.getInstance() == 1);
	}
	
	@Test
	public void testMultipleDependencyInField(){
		MultipleDependency m = d.multipleFieldTest();
		assertNotNull("Dependency not injected", m);
		assertTrue("New instance of dependency not created", m.isUniqueInstance());
	}
	
	@Test
	public void testSingletonDependencyInConstructor(){
		SingletonDependency s = d.singletonConstructorTest();
		assertNotNull("Dependency not injected", s);
		assertTrue("New instance of dependency", s.getInstance() == 1);
	}
	
	@Test
	public void testMultipleDependencyInConstructor(){
		MultipleDependency m = d.multipleConstructorTest();
		assertNotNull("Dependency not injected", m);
		assertTrue("New instance of dependency not created", m.isUniqueInstance());
	}
	
	@Test
	public void testExternalDependency(){
		int i = d.getInteger();
		assertTrue("Integer not injected", i == 3);
	}

}
