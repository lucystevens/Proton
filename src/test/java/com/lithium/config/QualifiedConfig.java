package com.lithium.config;

import com.lithium.configuration.Configuration;

@Configuration(qualifier = "test")
public class QualifiedConfig {
	
	Long qualfiedDependency(){
		return 27L;
	}

}
