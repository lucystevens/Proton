package com.lithium.test.suites;

import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import com.lithium.inject.InjectionManager;

public class InjectionSuiteRunner extends Suite {

	/*
	 * Initialise injection manager before running tests
	 */
	static{
		InjectionManager.init();
	}
	
	
	/*
	 * Allow all possible super constructors
	 * 
	 */
	
	public InjectionSuiteRunner(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		super(klass, builder);
    }

    public InjectionSuiteRunner(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
    	super(builder, classes);
    }

    protected InjectionSuiteRunner(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
    	super(klass, suiteClasses);
    }

    protected InjectionSuiteRunner(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
    	super(builder, klass, suiteClasses);
    }

    protected InjectionSuiteRunner(Class<?> klass, List<Runner> runners) throws InitializationError {
        super(klass, runners);
    }


}
