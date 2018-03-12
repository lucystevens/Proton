package com.lithium.test.cases;

import org.junit.Test;

import com.lithium.inject.Injector;
import com.lithium.inject.config.Inject;
import com.lithium.test.dependencies.MultipleDependency;
import com.lithium.test.dependencies.SingletonDependency;
import com.lithium.test.objects.SomeInjectableObject;
import com.lithium.test.objects.SomeObject;
import static org.junit.Assert.*;

public class TestStaticInjection {
	
	@Inject
	static Injector injector;
	
	@Test
	public void testSingletonDependency(){
		SingletonDependency s = SomeObject.singletonStaticInjectionTest();
		assertNotNull("Dependency not injected", s);
		assertTrue("New instance of dependency", s.getInstance() == 1);
	}
	
	@Test
	public void testMultipleDependency(){
		MultipleDependency m = SomeInjectableObject.mulitpleStaticInjectionTest();
		assertNotNull("Dependency not injected", m);
		assertTrue("New instance of dependency not created", m.isUniqueInstance());
	}

}
