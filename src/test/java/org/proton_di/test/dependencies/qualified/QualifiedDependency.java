package org.proton_di.test.dependencies.qualified;

import org.proton_di.configuration.Qualifier;
import org.proton_di.dependency.Dependency;
import org.proton_di.dependency.InstanceType;
import org.proton_di.inject.config.Inject;
import org.proton_di.test.dependencies.BlankSuperClass;
import org.proton_di.test.dependencies.DependencyWithDependencies;

@Dependency(type = InstanceType.MULTIPLE)
@Qualifier("qual")
public class QualifiedDependency extends BlankSuperClass{
	
	@Inject
	DependencyWithDependencies dwd;
	
	public DependencyWithDependencies getNestedDependency(){
		return dwd;
	}
	
}
