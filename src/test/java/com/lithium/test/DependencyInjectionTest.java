package com.lithium.test;

import java.util.ArrayList;
import java.util.List;

import com.lithium.dependency.InstanceType;
import com.lithium.inject.Inject;
import com.lithium.inject.Injector;

public class DependencyInjectionTest {
	
	@Inject
	private static SomeObject s;
	
	@Inject
	private static AnotherObject a;

	public static void main(String[] args) {
		Injector i = Injector.getInstance();
		
		System.out.println("SomeObject: " + s.test);
		
		System.out.println("AnotherObject: " + a.test);
		
		System.out.println("YetAnother Object: " + new YetAnotherObject(9).test);
		
		i.registerDependency(ArrayList.class, InstanceType.MULTIPLE);
		
		List<?> list = i.getDependency(List.class);
		System.out.println(list.getClass());
		
		/*AnotherObject a = i.getDependency(AnotherObject.class);
		System.out.println("Original a value: " + a.test);
		
		SomeObject b = i.getDependency(SomeObject.class);
		System.out.println("b value before a value changed (expected 50) " + b.test);
		
		a.setTestInt(80);
		
		SomeObject c = i.getDependency(SomeObject.class);
		System.out.println("c value after a value changed (expected 800) " + c.test);*/
	}

}
