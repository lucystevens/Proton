package com.lithium.test;

import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;

@Dependency(type = InstanceType.SINGLETON)
public class AnotherObject {

	public int test = 5;
	
	private AnotherObject(){}
	
	public void setTestInt(int test){
		this.test = test;
	}
}
