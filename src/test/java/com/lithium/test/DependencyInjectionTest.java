package com.lithium.test;

import com.lithium.inject.Injector;

public class DependencyInjectionTest {

	public static void main(String[] args) {
		Injector injector = Injector.getInstance();
		
		int passed = 0;
		
		// Static tests
		
		SingletonDependency s1 = SomeObject.singletonStaticInjectionTest();
		String result = testSingletonDependency(s1);
		System.out.println("Test 1:\t\t" + result);
		if(testPassed(result)) passed++;
		
		MultipleDependency m1 = SomeInjectableObject.mulitpleStaticInjectionTest();
		result = testMultipleDependency(m1, 1);
		System.out.println("Test 2:\t\t" + result);
		if(testPassed(result)) passed++;
		
		// Manually injected tests
		
		SomeObject so = new SomeObject();
		injector.injectDependencies(so);
		
		SingletonDependency s2 = so.singletonFieldTest();
		result = testSingletonDependency(s2);
		System.out.println("Test 3:\t\t" + result);
		if(testPassed(result)) passed++;
		
		MultipleDependency m2 = so.multipleFieldTest();
		result = testMultipleDependency(m2, 2);
		System.out.println("Test 4:\t\t" + result);
		if(testPassed(result)) passed++;
		
		// Extended tests
		
		SomeInjectableObject sio = new SomeInjectableObject();
		
		SingletonDependency s3 = sio.singletonFieldTest();
		result = testSingletonDependency(s3);
		System.out.println("Test 5:\t\t" + result);
		if(testPassed(result)) passed++;
		
		MultipleDependency m3 = sio.multipleFieldTest();
		result = testMultipleDependency(m3, 3);
		System.out.println("Test 6:\t\t" + result);
		if(testPassed(result)) passed++;
		
		// Dependency with dependency tests
		
		DependencyWithDependencies d = injector.getDependency(DependencyWithDependencies.class);
		
		SingletonDependency s4 = d.singletonConstructorTest();
		result = testSingletonDependency(s4);
		System.out.println("Test 7:\t\t" + result);
		if(testPassed(result)) passed++;
		
		MultipleDependency m4 = d.multipleConstructorTest();
		result = testMultipleDependency(m4, 4);
		System.out.println("Test 8:\t\t" + result);
		if(testPassed(result)) passed++;
		
		DependencyWithDependencies d2 = (DependencyWithDependencies) injector.getDependency(BlankSuperClass.class);
		
		SingletonDependency s5 = d2.singletonFieldTest();
		result = testSingletonDependency(s5);
		System.out.println("Test 9:\t\t" + result);
		if(testPassed(result)) passed++;
		
		MultipleDependency m5 = d2.multipleFieldTest();
		result = testMultipleDependency(m5, 7);
		System.out.println("Test 10:\t" + result);
		if(testPassed(result)) passed++;
		
		// Constructing using injector tests
		
		InjectorManagedObject imo = injector.newInstance(InjectorManagedObject.class);
		
		SingletonDependency s6 = imo.singletonConstructorTest();
		result = testSingletonDependency(s6);
		System.out.println("Test 11:\t" + result);
		if(testPassed(result)) passed++;
		
		MultipleDependency m6 = imo.multipleConstructorTest();
		result = testMultipleDependency(m6, 8);
		System.out.println("Test 12:\t" + result);
		if(testPassed(result)) passed++;
		
		// Test final results
		
		System.out.println("\nTests passed : " + passed + "/12");
	}
	
	public static String testSingletonDependency(SingletonDependency dependency){
		if(dependency == null) return "FAILED: Dependency not injected";
		else if(dependency.getInstance() != 1) return "FAILED: Multiple instances of singleton exist.Instance: " + dependency.getInstance();
		else return "PASSED";
	}
	
	public static String testMultipleDependency(MultipleDependency dependency, int expectedInstance){
		if(dependency == null) return "FAILED: Dependency not injected";
		else if(dependency.getInstance() != expectedInstance) return "FAILED: Unexpected dependency instance. Instance: " + dependency.getInstance();
		else return "PASSED";
	}
	
	public static boolean testPassed(String testDesc){
		return testDesc.contains("PASSED");
	}

}
