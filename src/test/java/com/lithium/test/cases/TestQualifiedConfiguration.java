package com.lithium.test.cases;

import org.junit.Test;

import com.lithium.configuration.Qualifier;
import com.lithium.dependency.exceptions.MissingDependencyException;
import com.lithium.inject.InjectionManager;
import com.lithium.inject.Injector;
import com.lithium.inject.config.Inject;

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
