package com.lithium.test.dependencies;

import java.io.Serializable;

import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;

@Dependency(type = InstanceType.SINGLETON)
public class SingletonDependency implements Serializable{
	
	private static final long serialVersionUID = 3068463032517817157L;

	private static int instanceCount = 0;
	
	private int instance;
	
	private SingletonDependency(){
		instance = ++instanceCount;
	}

	public int getInstance() {
		return instance;
	}

}
