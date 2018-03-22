package org.proton_di.test.cases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.proton_di.inject.Injector;
import org.proton_di.inject.config.Inject;
import org.proton_di.test.dependencies.DependencyWithDependencies;
import org.proton_di.test.dependencies.MultipleDependency;
import org.proton_di.test.dependencies.SingletonDependency;

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
