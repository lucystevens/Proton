package org.proton_di.test.objects;

import org.proton_di.inject.config.Inject;
import org.proton_di.test.dependencies.MultipleDependency;
import org.proton_di.test.dependencies.SingletonDependency;

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
