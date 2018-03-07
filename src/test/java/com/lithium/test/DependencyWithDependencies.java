package com.lithium.test;

import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;
import com.lithium.inject.config.Inject;

@Dependency(type = InstanceType.MULTIPLE)
public class DependencyWithDependencies {

	@Inject
	private SingletonDependency singletonField;
	
	@Inject
	private MultipleDependency multipleField;
	
	private SingletonDependency singletonConstructor;
	
	private MultipleDependency multipleConstructor;
	
	@Inject
	public DependencyWithDependencies(SingletonDependency singletonConstructor, MultipleDependency multipleConstructor){
		this.singletonConstructor = singletonConstructor;
		this.multipleConstructor = multipleConstructor;
	}

	public SingletonDependency singletonFieldTest() {
		return singletonField;
	}

	public MultipleDependency multipleFieldTest() {
		return multipleField;
	}

	public SingletonDependency singletonConstructorTest() {
		return singletonConstructor;
	}

	public MultipleDependency multipleConstructorTest() {
		return multipleConstructor;
	}
	
	
	
}
