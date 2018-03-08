package com.lithium.test.cases;

import java.io.Serializable;

import org.junit.Test;

import com.lithium.dependency.exceptions.AmbiguousDependencyException;
import com.lithium.dependency.exceptions.DependencyCreationException;
import com.lithium.dependency.exceptions.MissingConstructorException;
import com.lithium.dependency.exceptions.MissingDependencyException;
import com.lithium.inject.Injector;
import com.lithium.inject.exceptions.InjectionException;
import com.lithium.test.objects.invalid.InaccessbileInjectionObject;
import com.lithium.test.objects.invalid.InvalidConstructorObject;
import com.lithium.test.objects.invalid.InvalidInjectionObject;
import com.lithium.test.objects.invalid.MultipleConstructorObject;

public class TestInjectionExceptions {
	
	Injector inject = Injector.getInstance();

	@Test(expected = AmbiguousDependencyException.class)
	public void retrieveAmbiguousDependency(){
		inject.getDependency(Serializable.class);
	}
	
	@Test(expected = InjectionException.class)
	public void injectInaccessibleDependency(){
		inject.newInstance(InaccessbileInjectionObject.class);
	}
	
	@Test(expected = MissingDependencyException.class)
	public void injectInvalidDependency(){
		inject.newInstance(InvalidInjectionObject.class);
	}
	
	@Test(expected = MissingConstructorException.class)
	public void noValidConstructor(){
		inject.newInstance(InvalidConstructorObject.class);
	}
	
	@Test(expected = DependencyCreationException.class)
	public void multipleAnnotatedConstructors(){
		inject.newInstance(MultipleConstructorObject.class);
	}
}
