package com.lithium.test.dependencies.qualified;

import com.lithium.configuration.Qualifier;
import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;
import com.lithium.inject.config.Inject;
import com.lithium.test.dependencies.BlankSuperClass;
import com.lithium.test.dependencies.DependencyWithDependencies;

@Dependency(type = InstanceType.MULTIPLE)
@Qualifier("qual")
public class QualifiedDependency extends BlankSuperClass{
	
	@Inject
	DependencyWithDependencies dwd;
	
	public DependencyWithDependencies getNestedDependency(){
		return dwd;
	}
	
}
