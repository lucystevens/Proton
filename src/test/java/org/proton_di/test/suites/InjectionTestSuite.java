package org.proton_di.test.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.proton_di.test.cases.TestClassPath;
import org.proton_di.test.cases.TestConfiguration;
import org.proton_di.test.cases.TestDependenciesWithDependencies;
import org.proton_di.test.cases.TestExtensionInjection;
import org.proton_di.test.cases.TestInjectionExceptions;
import org.proton_di.test.cases.TestInjectorManagedConstruction;
import org.proton_di.test.cases.TestInvalidQualifiedInjection;
import org.proton_di.test.cases.TestManualInjection;
import org.proton_di.test.cases.TestQualifiedConfiguration;
import org.proton_di.test.cases.TestQualifiedInjection;
import org.proton_di.test.cases.TestStaticInjection;

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
