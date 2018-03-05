package com.iw.test;

import com.iw.inject.Inject;
import com.iw.inject.InjectableObject;

public class YetAnotherObject extends InjectableObject {
	
	public int test;
	
	@Inject
	private SomeObject s;
	
	public YetAnotherObject(int test){
		this.test = test * s.test;
	}

	public void setTestInt(int test){
		this.test = test;
	}
}
