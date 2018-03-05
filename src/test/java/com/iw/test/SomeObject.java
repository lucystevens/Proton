package com.iw.test;

import com.iw.dependency.Dependency;
import com.iw.dependency.InstanceType;
import com.iw.inject.Inject;

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
