package com.lithium.test.objects;

import com.lithium.inject.config.Inject;
import com.lithium.inject.config.InjectableObject;
import com.lithium.test.dependencies.MultipleDependency;
import com.lithium.test.dependencies.SingletonDependency;

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
