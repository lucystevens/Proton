package com.lithium.config;

import java.util.ArrayList;
import java.util.List;

import com.lithium.configuration.Configuration;

@Configuration
public class Config {
	
	Integer integerDependency(){
		return 3;
	}

	List<String> defaultList(){
		List<String> list = new ArrayList<>();
		list.add("first entry");
		return list;
	}
}
