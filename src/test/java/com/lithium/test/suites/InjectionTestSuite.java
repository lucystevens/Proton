package com.lithium.test.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.lithium.test.cases.TestClassPath;
import com.lithium.test.cases.TestConfiguration;
import com.lithium.test.cases.TestDependenciesWithDependencies;
import com.lithium.test.cases.TestExtensionInjection;
import com.lithium.test.cases.TestInjectionExceptions;
import com.lithium.test.cases.TestInjectorManagedConstruction;
import com.lithium.test.cases.TestInvalidQualifiedInjection;
import com.lithium.test.cases.TestManualInjection;
import com.lithium.test.cases.TestQualifiedConfiguration;
import com.lithium.test.cases.TestQualifiedInjection;
import com.lithium.test.cases.TestStaticInjection;

@RunWith(InjectionSuiteRunner.class)
@Suite.SuiteClasses({
	TestClassPath.class,
	TestStaticInjection.class,
	TestManualInjection.class,
	TestDependenciesWithDependencies.class,
	TestExtensionInjection.class,
	TestInjectionExceptions.class,
	TestInjectorManagedConstruction.class,
	TestConfiguration.class,
	TestQualifiedConfiguration.class,
	TestQualifiedInjection.class,
	TestInvalidQualifiedInjection.class
	})
public class InjectionTestSuite {

}
