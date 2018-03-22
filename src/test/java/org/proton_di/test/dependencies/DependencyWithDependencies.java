package org.proton_di.test.dependencies;

import org.proton_di.dependency.Dependency;
import org.proton_di.dependency.InstanceType;
import org.proton_di.inject.config.Inject;

@Dependency(type = InstanceType.MULTIPLE)
public class DependencyWithDependencies extends BlankSuperClass{
	
	@Inject
	private Integer integer;

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
	
	public int getInteger(){
		return integer;
	}
	
}
