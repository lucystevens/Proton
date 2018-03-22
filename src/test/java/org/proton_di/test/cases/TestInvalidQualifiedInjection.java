package org.proton_di.test.cases;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.proton_di.configuration.Qualifier;
import org.proton_di.dependency.exceptions.MissingDependencyException;
import org.proton_di.inject.InjectionManager;
import org.proton_di.inject.Injector;
import org.proton_di.inject.config.Inject;

@Qualifier("qual")
public class TestInvalidQualifiedInjection {
	
	@Inject
	Long l;
	
	@Test(expected = MissingDependencyException.class)
	public void injectDependencies(){
		Injector i = InjectionManager.getInjector(getClass());
		i.injectDependencies(this);
	}
	
	@Test
	public void testLong(){
		assertTrue("Long value injected from test config", l == null);
	}


}
