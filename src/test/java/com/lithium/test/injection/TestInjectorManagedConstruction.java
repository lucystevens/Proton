package com.lithium.test.injection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.lithium.inject.Injector;
import com.lithium.test.dependencies.MultipleDependency;
import com.lithium.test.dependencies.SingletonDependency;
import com.lithium.test.objects.InjectorManagedObject;

public class TestInjectorManagedConstruction {
	
	Injector inject = Injector.getInstance();
	InjectorManagedObject imo = inject.newInstance(InjectorManagedObject.class);
	
	@Test
	public void testSingletonDependency(){
		SingletonDependency s = imo.singletonConstructorTest();
		assertNotNull("Dependency not injected", s);
		assertTrue("New instance of dependency", s.getInstance() == 1);
	}
	
	@Test
	public void testMultipleDependency(){
		MultipleDependency m = imo.multipleConstructorTest();
		assertNotNull("Dependency not injected", m);
		assertTrue("New instance of dependency not created", m.isUniqueInstance());
	}

}
