package org.proton_di.test.cases;

import java.io.Serializable;

import org.junit.Test;
import org.proton_di.dependency.exceptions.AmbiguousDependencyException;
import org.proton_di.dependency.exceptions.DependencyCreationException;
import org.proton_di.dependency.exceptions.MissingConstructorException;
import org.proton_di.dependency.exceptions.MissingDependencyException;
import org.proton_di.inject.Injector;
import org.proton_di.inject.config.Inject;
import org.proton_di.test.objects.invalid.InvalidConstructorObject;
import org.proton_di.test.objects.invalid.InvalidInjectionObject;
import org.proton_di.test.objects.invalid.MultipleConstructorObject;

public class TestInjectionExceptions {
	
	@Inject
	static Injector injector;

	@Test(expected = AmbiguousDependencyException.class)
	public void retrieveAmbiguousDependency(){
		injector.getDependency(Serializable.class);
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
