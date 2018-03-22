package org.proton_di.test.objects;

import org.proton_di.inject.config.Inject;
import org.proton_di.test.dependencies.MultipleDependency;
import org.proton_di.test.dependencies.SingletonDependency;

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
