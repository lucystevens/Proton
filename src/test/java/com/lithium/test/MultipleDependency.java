package com.lithium.test;

import java.io.Serializable;

import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;

@Dependency(type = InstanceType.MULTIPLE)
public class MultipleDependency implements Serializable{

	private static final long serialVersionUID = -3000498178277330865L;

	private static int instanceCount = 0;
	
	private int instance;
	
	private MultipleDependency(){
		instance = ++instanceCount;
	}

	public int getInstance() {
		return instance;
	}

}
