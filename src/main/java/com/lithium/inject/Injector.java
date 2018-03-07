package com.lithium.inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.lithium.dependency.InstanceType;
import com.lithium.dependency.exceptions.DependencyCreationException;
import com.lithium.dependency.exceptions.MissingDependencyException;
import com.lithium.inject.config.InjectableObject;
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
	private InjectionTools tools = new InjectionTools();
	
	/**
	 * Creates the single instance of this Injector,
	 * by scanning all classes on the classpath and
	 * injected static dependencies where appropriate.
	 */
	private Injector(){
		DependencyManager manager = new DependencyManager(this);
		this.dependencies = manager.loadDependencies();
		
		this.dependencies.put(ClassPath.class, ClassScanner::getClassPath);
		this.dependencies.put(Injector.class, () -> this);
		
		injectStaticFields();
	}
	
	/**
	 * Scans all classes on the class path and, for each
	 * class found, injects dependencies into static
	 * fields annotated by the <code>@Inject</code> annotation. 
	 */
	private void injectStaticFields(){
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
			if(tools.isInjectable(f, true)) injectIntoField(f, null);
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
	Supplier<Object> getSupplier(Class<?> c, InstanceType type){
		if(type == InstanceType.SINGLETON){
			Object instance = newInstance(c);
			return () -> instance;
		}
		else {
			return () -> newInstance(c);
		}
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
	 * Converts an array of classes to an array of dependencies
	 * by retrieving the dependency associated with each class.
	 * @param classes An array of classes
	 * @return An array of dependencies
	 */
	public Object[] getDependencies(Class<?>[] classes){
		Object[] params = new Object[classes.length];
		for(int i = 0; i < classes.length; i++){
			params[i] = getDependency(classes[i]);
		}
		return params;
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
		if(instance == null) throw new MissingDependencyException(c);
		
		Object o = instance.get();
		if(o instanceof InjectionException) throw (InjectionException) o;
		else return c.cast(o);
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
			if(tools.isInjectable(f, false)) injectIntoField(f, o);
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
	public <T> T newInstance(Class<T> c) {
		T instance = null;
		Constructor<?> construct = tools.getConstructor(c);
		
		if(construct == null){
			instance = tools.construct(c);
		}
		else {
			Class<?>[] classes = construct.getParameterTypes();
			Object[] params = getDependencies(classes);
			instance =  tools.construct(c, params);
		}
		injectDependencies(instance);
		return instance;
	}

}
