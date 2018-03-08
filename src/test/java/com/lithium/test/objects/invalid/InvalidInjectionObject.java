package com.lithium.test.objects.invalid;

import com.lithium.inject.config.Inject;

public class InvalidInjectionObject {
	
	@Inject
	String notADependency;

}
