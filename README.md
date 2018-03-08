# Proton (alpha)

Proton is a simple, lightweight dependency injection framework for Java.
It has been designed to be used for smaller projects where all the complexities, features, and additional dependencies of the Spring Framework are not needed.

## Including Proton in your project

Currently to include Proton in your project you must clone the repository and import the project dependency manually.
The project will be available from the maven central repository when it leaves the alpha stage of development


## Documentation

Proton is currently in alpha and documentation will be updated as development continues.

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

		Injector injector = Injector.getInstance();
		InjectorManagedObject imo = injector.newInstance(InjectorManagedObject.class);
		
If the object has a constructor marked with `@Inject`, this will be called and appropriate dependencies passed in as parameters. Otherwise the default constructor will be used.
In addition to this, any fields annotated with `@Inject` will be injected.
	
#### Manual injection
If neither of the previous two methods are suitable for the situation, then the dependencies must be injected manually.
This can be done by annotating the dependency fields with `@Inject` as shown above and passing the created object to the `Injector.injectDependencies(Object o)` method.

		Injector injector = Injector.getInstance();
		SomeObject so = new SomeObject();
		injector.injectDependencies(so);
		
		
### Defining dependencies

Dependencies must be defined prior to them being injected into objects.
This is done by annotating them using `@Dependency` with an `InstanceType` denoting whether only a single instance of the dependency should exist, or whether a new one should be created every time it needs to be injected. 
Dependencies are mapped for not only their concrete class, but also their superclasses and any interfaces they implement. 

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

### Classpath utilities

The Proton Library also contains, in addition to the functionality above, a ClassPath class for retrieving classes that meet certain criteria including;
- Classes within a specific package
- Classes with a specific annotation
- Classes that implement a specific interface
		