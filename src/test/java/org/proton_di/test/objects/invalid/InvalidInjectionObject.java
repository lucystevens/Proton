package org.proton_di.test.objects.invalid;

import org.proton_di.inject.config.Inject;

public class InvalidInjectionObject {
	
	@Inject
	Long notADependency;

}
