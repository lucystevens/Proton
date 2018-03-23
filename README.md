[![Build Status][travis ci master img]][travis ci] &emsp;&emsp;
[![Maintainability][sonarcloud maintainability master img]][sonarcloud master] &emsp;&emsp;
[![Bugs][sonarcloud bugs master img]][sonarcloud master] &emsp;&emsp;
[![Maven][maven badge]][maven repo] &emsp;&emsp;

# Proton DI 

Proton is a simple, lightweight dependency injection framework for Java.
It has been designed to be used for smaller projects where all the complexities, features, and additional dependencies of the Spring Framework are not needed.

## Including Proton in your project

### Maven
Add this to your pom.xml

	<dependency>
	    <groupId>org.proton-di</groupId>
	    <artifactId>Proton</artifactId>
	    <version>1.0.0-beta</version>
	</dependency>
	
### Gradle
Add this to build.gradle

	dependencies {
	    compile 'org.proton-di:Proton:1.0.0-beta'
	}

Currently to include Proton in your project you must clone the repository and import the project dependency manually.
The project will be available from the maven central repository when it leaves the alpha stage of development


## Documentation
 - [Injection into objects](#injection-into-objects)
 - [Static Injection](#static-injection)
 - [Superclass injection](#superclass-injection)
 - [Injector managed construction](#injector-managed-construction)
 - [Manual injection](#manual-injection)
 - [Defining dependencies](#defining-dependencies)
 - [Defining third-party dependency configuration](#defining-third-party-dependency-configuration)
 - [Retrieving dependencies](#retrieving-dependencies)
 - [Dependencies within dependencies](#dependencies-within-dependencies)
 - [Qualifying dependencies, classes, and injectors](#qualifying-dependencies-classes-and-injectors)
 - [Specifying the default injector](#specifying-the-default-injector)
 - [Classpath utilities](#classpath-utilities)


### Injection into objects

Dependency Injection with Proton is designed to be as simple and straightfoward as possible.
There are four ways to inject a dependency into an object or class;

#### Static Injection
Simply annotate a static field within a class with `@Inject` and the dependency will be injected when `Injector` is initialised.

	@Inject
	private static SingletonDependency singletonStatic;


#### Superclass injection
If the dependencies must be non-static fields within an Object, then annotate the fields with `@Inject` and extend `InjectableObject`.
The dependencies will be injected within the super constructor.

	public class SomeInjectableObject extends InjectableObject{
		
		@Inject
		private SingletonDependency singletonField;
		
		@Inject
		private MultipleDependency multipleField;
		
	}
	
#### Injector managed construction
Objects with dependencies to be injected can be created using `Injector.newInstance(Class c)` where c is the class of the object to create.

		Injector injector = InjectionManager.getInstance(InjectorManagedObject.class);
		InjectorManagedObject imo = injector.newInstance(InjectorManagedObject.class);
		
If the object has a constructor marked with `@Inject`, this will be called and appropriate dependencies passed in as parameters. Otherwise the default constructor will be used.
In addition to this, any fields annotated with `@Inject` will be injected.
	
#### Manual injection
If neither of the previous two methods are suitable for the situation, then the dependencies must be injected manually.
This can be done by annotating the dependency fields with `@Inject` as shown above and passing the created object to the `Injector.injectDependencies(Object o)` method.

		Injector injector = InjectionManager.getInstance(SomeObject.class);
		SomeObject so = new SomeObject();
		injector.injectDependencies(so);
		
	
		
### Defining dependencies

Dependencies must be defined prior to them being injected into objects.
This is done by annotating them using `@Dependency` with an `InstanceType` denoting whether only a single instance of the dependency should exist, or whether a new one should be created every time it needs to be injected. 
Dependencies are mapped for not only their concrete class, but also their superclasses and any interfaces they implement. 

### Defining third-party dependency configuration

For dependencies from third party sources, that you can't simply annotate with `@Dependency`, classes annotated with `@Configuration` should be defined.
Each method in each of these classes should represent the code to be called when injecting an instance of the dependency in question.
The code below, for example, would register dependencies for the `Integer`, `List`, and `String` classes;

	@Configuration
	public class Config {
		
		Integer integerDependency(){
			return 3;
		}
	
		List<String> defaultList(){
			List<String> list = new ArrayList<>();
			list.add("first entry");
			return list;
		}
		
		String complexDep(DependencyWithDependencies d){
			return "The integer dependency is " + d.getInteger();
		}
		
	}
	
As can be seen above, these methods can receive other dependencies as params if necessary, and precautions should be taken to avoid circular dependencies.

#### Retrieving dependencies

Other than being injected into objects, dependencies can also be retrieved manually by calling `Injector.getDependency(Class<?> dependency)`, with the passed class being the dependency, any interface it implements, or any superclass it extends.

#### Dependencies within dependencies

Dependencies can be injected into other dependencies through the constructor like so;

	private SingletonDependency singletonConstructor;
	
	private MultipleDependency multipleConstructor;
	
	@Inject
	public DependencyWithDependencies(SingletonDependency singletonConstructor, MultipleDependency multipleConstructor){
		this.singletonConstructor = singletonConstructor;
		this.multipleConstructor = multipleConstructor;
	}
	
or through annotating fields with `@Inject` as shown above for objects.
	
Ensure that you don't have any circular dependencies when defining dependencies within dependencies!

### Qualifying dependencies, classes, and injectors

Proton contains the functionality to *qualify* dependencies and objects with fields to be injected so they are processed together by a specific Injector instance. This should be done by marking the class with the `@Qualifier` annotation and specifying the specific qualifying string e.g. `"test"`.

For example, if you have two dependencies that implement some interface `DaoService`. Let's say `HibernateService` is your default implementation and so it should not be annotated with `@Qualifier` and `MockDaoService` is your test implementation, so we annotated it with `@Qualifier("test")`. You can retrieve the appropriate Injector for a dependency one of several ways;

 - Calling `InjectionManager.getInjector(Class c)` will return the appropriate Injector for the passed class by looking at it's `@Qualifier` annotation.
 - Calling `InjectionManager.getInjector(String qualifier)` will return the *specified* Injector (e.g. test).
 - Calling `InjectionManager.getDefaultInjector()` will return the default injector for the current application instance (see below for details).
 
If a dependency cannot be found within a qualified injector, it will always default to checking the root injector for an instance.
 
You can also ensure that a specific dependency will be injected into an class by annotating that class with `@Qualifier` e.g.
	
	@Qualifier("test")
	public class UserService {
	
		@Inject
		private DaoService dao;
	
	}
	
Will ensure that the implementation of `DaoService` injected into `UserService` is the *test* implementation or `MockDaoService` in the above example.
 
#### Specifying the default injector

The default injector is used whenever a class does not have a `@Qualifier` annotation. It can be set via one of two ways;

 - Setting the property `proton.injector.default` in the VM args to the string qualifier of the injector you want to use as default. This is the recommended method as it can be specified before `InjectionManager` is initialised.
 - Calling `InjectionManager.setDefaultInjector(String qualifier)` this will set the default injector to the injector with the specified qualifier. It should be noted that since this initialises `InjectionManager` before setting the default Injector, static fields will not be affected.
 
As with other qualified injectors, the default injector will default to checking the root injector if a dependency is not found.


### Classpath utilities

The Proton Library also contains, in addition to the functionality above, a ClassPath class for retrieving classes that meet certain criteria including;
- Classes within a specific package
- Classes with a specific annotation
- Classes that implement a specific interface

You can retrieve the current ClassPath by calling `ClassPath.getInstance()`.


[travis ci master img]:https://travis-ci.org/lukecmstevens/Proton.svg?branch=master
[travis ci dev img]:https://travis-ci.org/lukecmstevens/Proton.svg?branch=development
[travis ci]:https://travis-ci.org/lukecmstevens/Proton

[sonarcloud maintainability master img]:https://sonarcloud.io/api/project_badges/measure?project=org.proton-di%3AProton&metric=sqale_rating
[sonarcloud maintainability dev img]:https://sonarcloud.io/api/project_badges/measure?project=org.proton-di%3AProton%3Adevelopment&metric=sqale_rating

[sonarcloud bugs master img]:https://sonarcloud.io/api/project_badges/measure?project=org.proton-di%3AProton&metric=bugs
[sonarcloud bugs dev img]:https://sonarcloud.io/api/project_badges/measure?project=org.proton-di%3AProton%3Adevelopment&metric=bugs

[sonarcloud master]:https://sonarcloud.io/dashboard?id=org.proton-di%3AProton
[sonarcloud dev]:https://sonarcloud.io/dashboard?id=org.proton-di%3AProton%3Adevelopment

[maven repo]:https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22Proton%22
[maven badge]:https://maven-badges.herokuapp.com/maven-central/org.proton-di/Proton/badge.svg
		