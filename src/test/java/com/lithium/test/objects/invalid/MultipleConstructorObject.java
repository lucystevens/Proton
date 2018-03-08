package com.lithium.test.objects.invalid;

import com.lithium.inject.config.Inject;
import com.lithium.test.dependencies.MultipleDependency;
import com.lithium.test.dependencies.SingletonDependency;

public class MultipleConstructorObject {

	@Inject
	public MultipleConstructorObject(MultipleDependency m){
		
	}
	
	@Inject
	public MultipleConstructorObject(SingletonDependency m){
		
	}
	
}
