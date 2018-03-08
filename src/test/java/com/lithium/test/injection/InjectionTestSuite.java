package com.lithium.test.injection;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestStaticInjection.class,
	TestManualInjection.class,
	TestDependenciesWithDependencies.class,
	TestExtensionInjection.class,
	TestExtensionInjection.class
	})
public class InjectionTestSuite {

}
