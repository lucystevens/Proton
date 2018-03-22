package org.proton_di.test.cases;

import org.junit.Test;
import org.proton_di.inject.Injector;
import org.proton_di.inject.config.Inject;
import org.proton_di.test.dependencies.MultipleDependency;
import org.proton_di.test.dependencies.SingletonDependency;
import org.proton_di.test.objects.SomeInjectableObject;
import org.proton_di.test.objects.SomeObject;

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
