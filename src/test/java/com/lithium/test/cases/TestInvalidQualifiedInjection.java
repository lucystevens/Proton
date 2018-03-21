package com.lithium.test.cases;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.lithium.configuration.Qualifier;
import com.lithium.dependency.exceptions.MissingDependencyException;
import com.lithium.inject.InjectionManager;
import com.lithium.inject.Injector;
import com.lithium.inject.config.Inject;

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
