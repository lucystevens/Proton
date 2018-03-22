package org.proton_di.test.objects;

import org.proton_di.inject.config.Inject;
import org.proton_di.inject.config.InjectableObject;
import org.proton_di.test.dependencies.MultipleDependency;
import org.proton_di.test.dependencies.SingletonDependency;

public class SomeInjectableObject extends InjectableObject{
	
	@Inject
	private static MultipleDependency mulitpleStatic;
	
	@Inject
	private SingletonDependency singletonField;
	
	@Inject
	private MultipleDependency multipleField;
	
	
	public static MultipleDependency mulitpleStaticInjectionTest(){
		return mulitpleStatic;
	}

	public SingletonDependency singletonFieldTest() {
		return singletonField;
	}

	public MultipleDependency multipleFieldTest() {
		return multipleField;
	}
}
