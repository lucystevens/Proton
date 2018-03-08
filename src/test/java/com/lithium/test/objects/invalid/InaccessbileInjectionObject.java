package com.lithium.test.objects.invalid;

import com.lithium.inject.config.Inject;

public class InaccessbileInjectionObject {
	
	@Inject
	final String inaccessibleDependency = "";

}
