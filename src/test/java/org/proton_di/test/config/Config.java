package org.proton_di.test.config;

import java.util.ArrayList;
import java.util.List;

import org.proton_di.configuration.Configuration;
import org.proton_di.test.dependencies.DependencyWithDependencies;

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
	
	String complexDep(DependencyWithDependencies d){
		return "The integer dependency is " + d.getInteger();
	}
}
