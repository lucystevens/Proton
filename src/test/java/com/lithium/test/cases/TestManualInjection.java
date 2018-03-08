package com.lithium.test.cases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.lithium.inject.Injector;
import com.lithium.test.dependencies.MultipleDependency;
import com.lithium.test.dependencies.SingletonDependency;
import com.lithium.test.objects.SomeObject;

public class TestManualInjection {
	
	Injector inject = Injector.getInstance();
	
	@Test
	public void testSingletonDependency(){
		SomeObject so = new SomeObject();
		inject.injectDependencies(so);
		
		SingletonDependency s = so.singletonFieldTest();
		assertNotNull("Dependency not injected", s);
		assertTrue("New instance of dependency", s.getInstance() == 1);
	}
	
	@Test
	public void testMultipleDependency(){
		SomeObject so = new SomeObject();
		inject.injectDependencies(so);
		
		MultipleDependency m = so.multipleFieldTest();
		assertNotNull("Dependency not injected", m);
		assertTrue("New instance of dependency not created", m.isUniqueInstance());
	}

}
