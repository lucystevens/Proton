package com.lithium.test;

import com.lithium.inject.config.Inject;

public class InjectorManagedObject {
	
	private SingletonDependency singletonConstructor;
	
	private MultipleDependency multipleConstructor;
	
	@Inject
	private InjectorManagedObject(SingletonDependency singletonConstructor, MultipleDependency multipleConstructor){
		this.singletonConstructor = singletonConstructor;
		this.multipleConstructor = multipleConstructor;
	}

	public SingletonDependency singletonConstructorTest() {
		return singletonConstructor;
	}

	public MultipleDependency multipleConstructorTest() {
		return multipleConstructor;
	}

}
