package org.proton_di.test.cases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.proton_di.inject.Injector;
import org.proton_di.inject.config.Inject;
import org.proton_di.test.dependencies.MultipleDependency;
import org.proton_di.test.dependencies.SingletonDependency;
import org.proton_di.test.objects.SomeObject;

public class TestManualInjection {
	
	@Inject
	static Injector injector;
	
	@Test
	public void testSingletonDependency(){
		SomeObject so = new SomeObject();
		injector.injectDependencies(so);
		
		SingletonDependency s = so.singletonFieldTest();
		assertNotNull("Dependency not injected", s);
		assertTrue("New instance of dependency", s.getInstance() == 1);
	}
	
	@Test
	public void testMultipleDependency(){
		SomeObject so = new SomeObject();
		injector.injectDependencies(so);
		
		MultipleDependency m = so.multipleFieldTest();
		assertNotNull("Dependency not injected", m);
		assertTrue("New instance of dependency not created", m.isUniqueInstance());
	}

}
