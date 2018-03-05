# Proton (alpha)

Proton is a simple, lightweight dependency injection framework for Java.
It has been designed to be used for smaller projects where all the complexities, features, and additional dependencies of the Spring Framework are not needed.

## Including Proton in your project

Currently to include Proton in your project you must clone the repository and import the project dependency manually.
I am currently working on adding the dependency to the maven central repository.


## Documentation

Proton is currently in alpha and documentation will be updated as development continues.

### Injection into objects

Dependency Injection with Proton is designed to be as simple and straightfoward as possible.
There are three ways to inject a dependency into an object or class;

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
	
#### Manual injection
If neither of the previous two methods are suitable for the situation, then the dependencies must be injected manually.
This can be done by annotating the dependency fields with `@Inject` as shown above and passing the created object to the `Injector.injectDependencies(Object o)` method.

		Injector injector = Injector.getInstance();
		SomeObject so = new SomeObject();
		injector.injectDependencies(so);
		
		
### Defining dependencies

Dependencies must be defined prior to them being injected into objects.
This is done by annotating them using `@Dependency` with an `InstanceType` denoting whether only a single instance of the dependency should exist, or whether a new one should be created every time it needs to be injected. 
It should be noted that currently dependencies are mapped for both their class, and all intefaces they implement. This means that you cannot have multiple dependencies that implement the same interface.

#### Retrieving dependencies

Other than being injected into objects, dependencies can also be retrieved manually by calling `Injector.getDependency(Class<?> dependency)`, with the passed class being the dependency or any interface it implements.

#### Dependencies within dependencies

Currently dependencies can only be injected into other dependencies through the constructor like so;

	private SingletonDependency singletonConstructor;
	
	private MultipleDependency multipleConstructor;
	
	@Inject
	public DependencyWithDependencies(SingletonDependency singletonConstructor, MultipleDependency multipleConstructor){
		this.singletonConstructor = singletonConstructor;
		this.multipleConstructor = multipleConstructor;
	}
	
Ensure that you don't have any circular dependencies when defining them like this.

#### Registering external dependencies

Sometimes external dependencies are needed to be added to a project, which cannot be annotated with `@Dependency`.
These can be registered by using `Injector.registerDependency(Class<?> dependency, InstanceType type)` where the InstanceType is as defined above.
It should also be noted that since these dependencies are only registered after the Injector is initialised, they cannot be injected into static fields or other dependencies.
There is currently an open issue to extend the configuration to allow this.

### Classpath utilities

The Proton Library also contains, in addition to the functionality above, a ClassPath class for retrieving classes that meet certain criteria including;
- Classes within a specific package
- Classes with a specific annotation
- Classes that implement a specific interface
		