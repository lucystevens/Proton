package org.proton_di.test.cases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.proton_di.inject.Injector;
import org.proton_di.inject.config.Inject;
import org.proton_di.test.dependencies.MultipleDependency;
import org.proton_di.test.dependencies.SingletonDependency;
import org.proton_di.test.objects.SomeInjectableObject;

public class TestExtensionInjection {
	
	@Inject
	static Injector injector;
	
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
