package org.proton_di.test.objects.invalid;

import org.proton_di.inject.config.Inject;
import org.proton_di.test.dependencies.MultipleDependency;
import org.proton_di.test.dependencies.SingletonDependency;

public class MultipleConstructorObject {

	@Inject
	public MultipleConstructorObject(MultipleDependency m){
		
	}
	
	@Inject
	public MultipleConstructorObject(SingletonDependency m){
		
	}
	
}
