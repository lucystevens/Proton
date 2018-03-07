package com.lithium.test;

import com.lithium.inject.config.Inject;

public class SomeObject {
	
	@Inject
	private static SingletonDependency singletonStatic;
	
	@Inject
	private SingletonDependency singletonField;
	
	@Inject
	private MultipleDependency multipleField;
	
	
	public static SingletonDependency singletonStaticInjectionTest(){
		return singletonStatic;
	}

	public SingletonDependency singletonFieldTest() {
		return singletonField;
	}

	public MultipleDependency multipleFieldTest() {
		return multipleField;
	}
	
}
