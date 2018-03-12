package com.lithium.test.cases;

import java.io.Serializable;

import org.junit.Test;

import com.lithium.dependency.exceptions.AmbiguousDependencyException;
import com.lithium.dependency.exceptions.DependencyCreationException;
import com.lithium.dependency.exceptions.MissingConstructorException;
import com.lithium.dependency.exceptions.MissingDependencyException;
import com.lithium.inject.Injector;
import com.lithium.inject.config.Inject;
import com.lithium.inject.exceptions.InjectionException;
import com.lithium.test.objects.invalid.InaccessbileInjectionObject;
import com.lithium.test.objects.invalid.InvalidConstructorObject;
import com.lithium.test.objects.invalid.InvalidInjectionObject;
import com.lithium.test.objects.invalid.MultipleConstructorObject;

public class TestInjectionExceptions {
	
	@Inject
	static Injector injector;

	@Test(expected = AmbiguousDependencyException.class)
	public void retrieveAmbiguousDependency(){
		injector.getDependency(Serializable.class);
	}
	
	@Test(expected = InjectionException.class)
	public void injectInaccessibleDependency(){
		injector.newInstance(InaccessbileInjectionObject.class);
	}
	
	@Test(expected = MissingDependencyException.class)
	public void injectInvalidDependency(){
		injector.newInstance(InvalidInjectionObject.class);
	}
	
	@Test(expected = MissingConstructorException.class)
	public void noValidConstructor(){
		injector.newInstance(InvalidConstructorObject.class);
	}
	
	@Test(expected = DependencyCreationException.class)
	public void multipleAnnotatedConstructors(){
		injector.newInstance(MultipleConstructorObject.class);
	}
}
