package com.lithium.inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import com.lithium.dependency.exceptions.DependencyCreationException;
import com.lithium.dependency.exceptions.MissingConstructorException;
import com.lithium.inject.config.Inject;

/**
 * A tools class to separate out stateless methods
 * from the {@link Injector} and {@link DependencyManager}.
 * 
 * @author Luke Stevens
 *
 */
public class InjectionTools {
	
	// Restrict access to package
	InjectionTools(){}
	
	/**
	 * Determines whether a field is injectable e.g.
	 * whether a dependency should be injected into it.
	 * @param f The field to check
	 * @param checkStatic Whether the field is static or not. e.g. if
	 * this is true then this method will only return true for static
	 * injectable methods, and vice-versa
	 * @return True if injectable, false if not.
	 */
	boolean isInjectable(Field f, boolean checkStatic){
		return f.getAnnotation(Inject.class) != null && (checkStatic == Modifier.isStatic(f.getModifiers()));
	}
	
	/**
	 * Gets a constructor types for the injection constructor to
	 * use for the given.
	 * @param c The class to get a constructor for.
	 * @return The parameter types for constructor annotated with
	 * <code>@Inject</code> if one exists, otherwise the default constructor.
	 * @throws DependencyCreationException If there are multiple 
	 * constructors annotated with <code>@Inject</code>
	 */
	Class<?>[] getConstructorTypes(Class<?> c){
		Class<?>[] types = {};
		
		for(Constructor<?> con : c.getDeclaredConstructors()){
			if(con.getAnnotation(Inject.class) != null){
				if(types.length > 0) throw new DependencyCreationException("Multiple constructors annotated with @Inject.", c);
				else types = con.getParameterTypes();
			}
		}
		
		return types;
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
	<T> T construct(Class<T> c, Object...params){
		Class<?>[] classes = argsToClasses(params);
		try {
			Constructor<T> con = c.getDeclaredConstructor(classes);
			con.setAccessible(true);
			return con.newInstance(params);
		} catch(Exception e){
			 throw new MissingConstructorException(c, classes);
		}
	}
	
	/**
	 * Converts an array of arguments to an array of
	 * their respective classes. Used for retrieving methods
	 * and constructors with specific parameters from a class.
	 * @param args An array of arguments to be passed to a method
	 * @return An array of classes of the original arguments.
	 */
	Class<?>[] argsToClasses(Object...args){
		Class<?>[] classes = new Class<?>[args.length];
		for(int i = 0; i<args.length; i++){
			classes[i] = args[i].getClass();
		}
		return classes;
	}
	
	/**
	 * Gets all sub dependencies from a parent dependency
	 * @param parent The parent dependency class
	 * @return A List of all sub dependencies within a parent
	 * dependency
	 */
	List<Class<?>> getSubDependencies(Class<?> parent){
		List<Class<?>> subDependencies = new ArrayList<>();
		
		// Get field sub dependencies
		for(Field f : parent.getDeclaredFields()){
			if(isInjectable(f, false)) subDependencies.add(f.getType());
		}
		
		// Get constructor sub dependencies
		for(Class<?> c : getConstructorTypes(parent)){
			subDependencies.add(c);
		}
		return subDependencies;
	}
	
	/**
	 * Gets all superclasses for a given class
	 * @param parent The class to get all superclasses for
	 * @return A List of superclasses for the given class
	 */
	List<Class<?>> getSuperClasses(Class<?> parent){
		List<Class<?>> superClasses = new ArrayList<>();
		Class<?> superclass = parent.getSuperclass();
		while(superclass != null){
			superClasses.add(superclass);
			superclass = superclass.getSuperclass();
		}
		return superClasses;
	}
	
	/**
	 * Invokes a method using an object instance and arguments.
	 * @param o The object instance to call the method on.
	 * If the method is static then this should be null.
	 * @param m The method to invoke.
	 * @param args The arguments to pass to the method.
	 * @return The Object returned by the method invoked.
	 */
	Object invokeMethod(Object o, Method m, Object...args){
		try{
			m.setAccessible(true);
			return m.invoke(o, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new DependencyCreationException("Root cause: " + e.getMessage(), o.getClass());
		}
	}

}
