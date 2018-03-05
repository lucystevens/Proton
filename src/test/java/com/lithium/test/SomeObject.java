package com.lithium.test;

import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;
import com.lithium.inject.Inject;

@Dependency(type = InstanceType.MULTIPLE)
public class SomeObject {

	public int test = 10;
	
	@Inject
	private SomeObject(AnotherObject o){
		test *= o.test;
	}
	
	public void setTestInt(int test){
		this.test = test;
	}
}
