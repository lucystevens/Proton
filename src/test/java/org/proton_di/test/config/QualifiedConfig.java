package org.proton_di.test.config;

import org.proton_di.configuration.Configuration;

@Configuration(qualifier = "test")
public class QualifiedConfig {
	
	Long qualfiedDependency(){
		return 27L;
	}

}
