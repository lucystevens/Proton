package com.lithium.test.cases;


import static org.junit.Assert.*;

import org.junit.Test;

import com.lithium.configuration.Qualifier;
import com.lithium.inject.config.Inject;
import com.lithium.inject.config.InjectableObject;
import com.lithium.test.dependencies.BlankSuperClass;
import com.lithium.test.dependencies.qualified.QualifiedDependency;

@Qualifier("qual")
public class TestQualifiedInjection extends InjectableObject{
	
	@Inject
	BlankSuperClass dep;
	
	@Test
	public void testDependency(){
		assertFalse("Qualified dependency not injected", dep == null);
		assertFalse("Depdendency not injected from root", ((QualifiedDependency) dep).getNestedDependency() == null);
	}
}
