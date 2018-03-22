package org.proton_di.test.cases;

import org.junit.Test;
import org.proton_di.configuration.Qualifier;
import org.proton_di.dependency.exceptions.MissingDependencyException;
import org.proton_di.inject.InjectionManager;
import org.proton_di.inject.Injector;
import org.proton_di.inject.config.Inject;

import static org.junit.Assert.*;

@Qualifier("test")
public class TestQualifiedConfiguration {
	
	@Inject
	static Long longTest;
	
	@Test
	public void qualifiedInjector(){
		Injector i = InjectionManager.getInjector("test");
		Long l = i.getDependency(Long.class);
		assertTrue("Long value not retrieved from injector", l.equals(27L));
	}
	
	@Test(expected = MissingDependencyException.class)
	public void rootInjector(){
		Injector i = InjectionManager.getRootInjector();
		i.getDependency(Long.class);
	}
	
	@Test
	public void qualifiedStaticDependency(){
		assertTrue("Long value not retrieved from injector", longTest.equals(27L));
	}

}
