package com.lithium.test;

import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;

@Dependency(type = InstanceType.SINGLETON)
public class SingletonDependency {
	
	private static int instanceCount = 0;
	
	private int instance;
	
	private SingletonDependency(){
		instance = ++instanceCount;
	}

	public int getInstance() {
		return instance;
	}

}
