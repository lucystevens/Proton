package com.lithium.test.injection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.lithium.inject.Injector;
import com.lithium.test.dependencies.MultipleDependency;
import com.lithium.test.dependencies.SingletonDependency;
import com.lithium.test.objects.SomeInjectableObject;

public class TestExtensionInjection {
	
	Injector inject = Injector.getInstance();
	SomeInjectableObject sio = new SomeInjectableObject();
	
	@Test
	public void testSingletonDependency(){
		SingletonDependency s = sio.singletonFieldTest();
		assertNotNull("Dependency not injected", s);
		assertTrue("New instance of dependency", s.getInstance() == 1);
	}
	
	@Test
	public void testMultipleDependency(){
		MultipleDependency m = sio.multipleFieldTest();
		assertNotNull("Dependency not injected", m);
		assertTrue("New instance of dependency not created", m.isUniqueInstance());
	}

}
