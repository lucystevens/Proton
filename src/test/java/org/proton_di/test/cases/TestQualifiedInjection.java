package org.proton_di.test.cases;


import static org.junit.Assert.*;

import org.junit.Test;
import org.proton_di.configuration.Qualifier;
import org.proton_di.inject.config.Inject;
import org.proton_di.inject.config.InjectableObject;
import org.proton_di.test.dependencies.BlankSuperClass;
import org.proton_di.test.dependencies.qualified.QualifiedDependency;

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
