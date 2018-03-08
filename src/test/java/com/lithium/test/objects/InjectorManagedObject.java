package com.lithium.test.objects;

import com.lithium.inject.config.Inject;
import com.lithium.test.dependencies.MultipleDependency;
import com.lithium.test.dependencies.SingletonDependency;

public class InjectorManagedObject {
	
	private SingletonDependency singletonConstructor;
	
	private MultipleDependency multipleConstructor;
	
	@Inject
	private SingletonDependency singletonField;
	
	@Inject
	private MultipleDependency multipleField;
	
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
	
	public SingletonDependency singletonFieldTest() {
		return singletonField;
	}

	public MultipleDependency multipleFieldTest() {
		return multipleField;
	}

}
