package org.proton_di.test.cases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.proton_di.inject.Injector;
import org.proton_di.inject.config.Inject;
import org.proton_di.test.dependencies.MultipleDependency;
import org.proton_di.test.dependencies.SingletonDependency;
import org.proton_di.test.objects.InjectorManagedObject;

public class TestInjectorManagedConstruction {
	
	@Inject
	static Injector injector;
	
	InjectorManagedObject imo = injector.newInstance(InjectorManagedObject.class);
	
	@Test
	public void testSingletonDependencyInField(){
		SingletonDependency s = imo.singletonFieldTest();
		assertNotNull("Dependency not injected", s);
		assertTrue("New instance of dependency", s.getInstance() == 1);
	}
	
	@Test
	public void testMultipleDependencyInField(){
		MultipleDependency m = imo.multipleFieldTest();
		assertNotNull("Dependency not injected", m);
		assertTrue("New instance of dependency not created", m.isUniqueInstance());
	}
	
	@Test
	public void testSingletonDependencyInConstructor(){
		SingletonDependency s = imo.singletonConstructorTest();
		assertNotNull("Dependency not injected", s);
		assertTrue("New instance of dependency", s.getInstance() == 1);
	}
	
	@Test
	public void testMultipleDependencyInConstructor(){
		MultipleDependency m = imo.multipleConstructorTest();
		assertNotNull("Dependency not injected", m);
		assertTrue("New instance of dependency not created", m.isUniqueInstance());
	}

}
