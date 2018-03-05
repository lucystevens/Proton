package com.lithium.inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.lithium.dependency.Dependency;
import com.lithium.dependency.DependencyCreationException;
import com.lithium.dependency.InstanceType;
import com.lithium.scanner.ClassPath;
import com.lithium.scanner.ClassScanner;

/**
 * Singleton class for getting and injecting
 * dependencies.<br>
 * On being loaded this class will
 * create a single Injector instance which scans
 * all classes on the classpath an injects dependencies
 * into static fields marked with <code>@Inject</code>
 * 
 * @author Luke Stevens
 */
public class Injector {
	
	private static final Injector INSTANCE = new Injector();
	
	/**
	 * @return The single Injector instance.
	 */
	public static Injector getInstance(){
		return INSTANCE;
	}
	
	private Map<Class<?>, Supplier<Object>> dependencies = new HashMap<>();
	
	/**
	 * Creates the single instance of this Injector,
	 * by scanning all classes on the classpath and
	 * injected static dependencies where appropriate.
	 */
	private Injector(){
		scanClasses();
	}
	
	/**
	 * Scans all classes on the class path and, for each
	 * class found, injects dependencies into static
	 * fields annotated by the <code>@Inject</code> annotation. 
	 */
	private void scanClasses(){
		ClassPath classpath = ClassScanner.getClassPath();
		for(Class<?> c : classpath.getClasses()){
			scanFields(c);
		}
	}
	
	/**
	 * Scans all fields on a class and, for each eligible field,
	 * sets it's value to an appropriate dependency.
	 * @param c The class to scan.
	 */
	private void scanFields(Class<?> c){
		for(Field f : c.getDeclaredFields()){
			if(isInjectable(f)) injectIntoField(f, null);
		}
	}
	
	/**
	 * Determines whether a field is injectable e.g.
	 * whether a dependency should be injected into it.
	 * @param f The field to check
	 * @return True if injectable, false if not.
	 */
	private boolean isInjectable(Field f){
		return f.getAnnotation(Inject.class) != null && Modifier.isStatic(f.getModifiers());
	}
	
	/**
	 * Injects a dependency into an injectable field.
	 * @param f The field to inject into. Must be
	 * annotated with <code>@Inject</code>
	 * @param o The object instance to inject into. If used
	 * for static dependencies, this should be null.
	 */
	private void injectIntoField(Field f, Object o){
		try{
			Object dependency = getDependency(f.getType());
			f.setAccessible(true);
			f.set(o, dependency);
		} catch (Exception e) {
			throw new InjectionException(e, f.getClass());
		}
	}
	
	/**
	 * Loads the supplier for a dependency class and stores
	 * in the dependency map for the class and all interfaces.
	 * @param c The class to load as a dependency. Must be
	 * annotated with <code>@Dependency</code>
	 */
	private void loadDependencies(Class<?> c){
		if(c.getAnnotation(Dependency.class) == null) throw new DependencyCreationException("No @Dependency annotation found.", c);
		InstanceType type = c.getAnnotation(Dependency.class).type();
		loadDependencies(c, type);
	}
	
	/**
	 * Loads the supplier for a dependency class and stores
	 * in the dependency map for the class and all interfaces.
	 * @param c The class to load as a dependency.
	 * @param type The InstanceType for the dependency
	 */
	private void loadDependencies(Class<?> c, InstanceType type){
		Supplier<Object> instance = getSupplier(c, type);
		loadDependency(c, instance);
		for(Class<?> iface : c.getInterfaces()){
			loadDependency(iface, instance);
		}
	}
	
	/**
	 * Creates the supplier for a dependency. 
	 * @param c The class to create the supplier for
	 * @param type The InstanceType; e.g. whether an existing instance
	 * should always be supplied, or a new instance created
	 * @return A lambda function that either creates a new instance
	 * or returns the existing singleton instance when called.
	 */
	private Supplier<Object> getSupplier(Class<?> c, InstanceType type){
		if(type == InstanceType.SINGLETON){
			Object instance = newInstance(c);
			return () -> instance;
		}
		else {
			return () -> newInstance(c);
		}
	}
	
	/**
	 * Gets a new instance of the specified class:
	 * If there is a constructor annotated with <code>@Inject</code>
	 * then this will be used and the arguments will be injected as 
	 * dependencies. Otherwise the default constructor will be used.
	 * @param c The class to construct an instance of.
	 * @return An instance of the Class c
	 */
	private Object newInstance(Class<?> c) {
		Constructor<?> construct = getConstructor(c);
		
		if(construct == null){
			return construct(c);
		}
		else {
			// TODO change to use List rather than array
			Class<?>[] classes = construct.getParameterTypes();
			Object[] params = new Object[classes.length];
			for(int i = 0; i < classes.length; i++){
				params[i] = getDependency(classes[i]);
			}
			return construct(c, params);
		}
	}
	
	/**
	 * Gets a constructor for a specified class:
	 * @param c The class to get a constructor for.
	 * @return The constructor annotated with <code>@Inject</code> if one
	 * exists, otherwise the default constructor.
	 * @throws DependencyCreationException If there are multiple 
	 * constructors annotated with <code>@Inject</code>
	 */
	private Constructor<?> getConstructor(Class<?> c){
		Constructor<?> construct = null;
		
		for(Constructor<?> con : c.getDeclaredConstructors()){
			if(con.getAnnotation(Inject.class) != null){
				if(construct != null) throw new DependencyCreationException("Multiple constructors annotated with @Inject.", c);
				else construct = con;
			}
		}
		
		return construct;
	}
	
	/**
	 * Constructs an object, given the parameters to use.
	 * @param c The class to construct an instance of.
	 * @param params The parameters to pass to the constructor.
	 * @return A new instance of the object, constructed using the
	 * supplied parameters.
	 * @throws DependencyCreationException If there is
	 * not constructor matching the supplied parameters.
	 */
	private Object construct(Class<?> c, Object...params){
		try {
			Constructor<?> con = c.getDeclaredConstructor(argsToClasses(params));
			con.setAccessible(true);
			return con.newInstance(params);
		} catch(Exception e){
			// TODO change to be more generic, match params
			 throw new DependencyCreationException("Default constructor not found.", c);
		}
	}
	
	/**
	 * Converts an array of arguments to an array of
	 * their respective classes. Used for retrieving methods
	 * and constructors with specific parameters from a class.
	 * @param args An array of arguments to be passed to a method
	 * @return An array of classes of the original arguments.
	 */
	private Class<?>[] argsToClasses(Object...args){
		Class<?>[] classes = new Class<?>[args.length];
		for(int i = 0; i<args.length; i++){
			classes[i] = args[i].getClass();
		}
		return classes;
	}
	
	/**
	 * Loads a dependency into the dependency map
	 * @param d The class to store the dependency under. This may be 
	 * the actual dependency class, or any interface it implements.
	 * @param instance The supplier to use to get an instance of this
	 * dependency.
	 * @throws DependencyCreationException If a dependency already exists
	 * for the specified class
	 */
	private void loadDependency(Class<?> d, Supplier<Object> instance){
		if(dependencies.containsKey(d)) throw new DependencyCreationException("Dependency already exists for class.", d);
		else dependencies.put(d, instance);
		
	}
	
	/**
	 * Gets an instance of a registered dependency.
	 * This can either have been registered automatically
	 * using the <code>@Dependency</code> annotation or
	 * manually using the {@link Injector#registerDependency(Class, InstanceType)}
	 * method.
	 * @param c The class to load the dependency for. Note
	 * this can be an interface that the dependency implements.
	 * @return An instance of the class specified.
	 * @throws DependencyCreationException If there is no stored
	 * dependency for the class, and a new one cannot be loaded.
	 */
	public <T> T getDependency(Class<T> c){
		Supplier<Object> instance = dependencies.get(c);
		if(instance == null){
			loadDependencies(c);
			instance = dependencies.get(c);
		}
		
		if(instance == null) throw new DependencyCreationException("", c);
		
		return c.cast(instance.get());
	}
	
	/**
	 * Injects dependencies into an already instantiated 
	 * object. This scans every field within the Object 
	 * for those annotated with <code>@Inject</code> and
	 * sets the value of those fields to a relevant dependency.<br><br>
	 * <i>Note: this method is called during instantiation for
	 * any Object extending {@link InjectableObject} and so
	 * does not need to be called manually for those Objects</i>
	 * @param o The object to inject dependencies into.
	 */
	public void injectDependencies(Object o){
		Class<?> c = o.getClass();
		for(Field f : c.getDeclaredFields()){
			if(f.getAnnotation(Inject.class) != null
					&& !Modifier.isStatic(f.getModifiers())){
						injectIntoField(f, o);
			}
		}
	}
	
	/**
	 * Registers a new dependency. This should be used for
	 * third party classes that cannot be annotated with
	 * the <code>@Dependency</code> annotation and have
	 * to be registered manually.
	 * @param c The class to register
	 * @param type The instantiation type e.g. Whether the
	 * same instance should be returned whenever used, or
	 * whether a new instance should be created.
	 */
	public void registerDependency(Class<?> c, InstanceType type){
		loadDependencies(c, type);
	}

}
