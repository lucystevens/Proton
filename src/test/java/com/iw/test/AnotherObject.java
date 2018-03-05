package com.iw.test;

import com.iw.dependency.Dependency;
import com.iw.dependency.InstanceType;

@Dependency(type = InstanceType.SINGLETON)
public class AnotherObject {

	public int test = 5;
	
	private AnotherObject(){}
	
	public void setTestInt(int test){
		this.test = test;
	}
}
