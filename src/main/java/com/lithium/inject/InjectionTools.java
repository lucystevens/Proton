package com.lithium.inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.lithium.dependency.InstanceType;
import com.lithium.dependency.exceptions.DependencyCreationException;
import com.lithium.dependency.exceptions.MissingConstructorException;
import com.lithium.inject.config.Inject;

public class InjectionTools {
	
	/**
	 * Determines whether a field is injectable e.g.
	 * whether a dependency should be injected into it.
	 * @param f The field to check
	 * @param checkStatic Whether the field is static or not. e.g. if
	 * this is true then this method will only return true for static
	 * injectable methods, and vice-versa
	 * @return True if injectable, false if not.
	 */
	public boolean isInjectable(Field f, boolean checkStatic){
		return f.getAnnotation(Inject.class) != null && (checkStatic == Modifier.isStatic(f.getModifiers()));
	}
	
	/**
	 * Gets a constructor for a specified class:
	 * @param c The class to get a constructor for.
	 * @return The constructor annotated with <code>@Inject</code> if one
	 * exists, otherwise the default constructor.
	 * @throws DependencyCreationException If there are multiple 
	 * constructors annotated with <code>@Inject</code>
	 */
	public Constructor<?> getConstructor(Class<?> c){
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
	public <T> T construct(Class<T> c, Object...params){
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
	public Class<?>[] argsToClasses(Object...args){
		Class<?>[] classes = new Class<?>[args.length];
		for(int i = 0; i<args.length; i++){
			classes[i] = args[i].getClass();
		}
		return classes;
	}
	
	public List<Class<?>> getSubDependencies(Class<?> parent){
		List<Class<?>> subDependencies = new ArrayList<>();
		for(Field f : parent.getDeclaredFields()){
			if(isInjectable(f, false)) subDependencies.add(f.getType());
		}
		return subDependencies;
	}

}
