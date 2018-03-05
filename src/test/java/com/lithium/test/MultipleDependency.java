package com.lithium.test;

import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;

@Dependency(type = InstanceType.MULTIPLE)
public class MultipleDependency {
	
	private static int instanceCount = 0;
	
	private int instance;
	
	private MultipleDependency(){
		instance = ++instanceCount;
	}

	public int getInstance() {
		return instance;
	}

}
