package org.proton_di.inject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.proton_di.configuration.Qualifier;
import org.proton_di.scanner.ClassPath;

public class InjectionManager {
	
	public static final String ROOT_QUALIFIER = "root";
	
	private static final Injector ROOT = new RootInjector();
	private static final Map<String, Injector> INSTANCES = new ConcurrentHashMap<>();
	private static Injector defaultInjector;

	/*
	 * Sets the default injector to the injector specified
	 * by the property proton.injector.default.
	 * 
	 * This should be used when running in different environments
	 * from VM args.
	 */
	static{
		String defaultQualifier = System.getProperty("proton.injector.default");
		defaultInjector = (defaultQualifier==null)? ROOT : getInjector(defaultQualifier);
		injectStaticFields();
	}
	
	private InjectionManager(){/* Hide public constructor in static class */}
	
	
	/**
	 * Used to load the Injection Manager class and initialise
	 * the RootInjector singleton instance.<br>
	 * 
	 * Calling any other methods in InjectionManager or 
	 * <code>Class.forName("org.proton_di.inject.InjectionManager")</code>
	 * will achieve the same results, but this method makes the
	 * intention more obvious.
	 */
	public static void init(){ /* Doesn't do anything other than force loading of the class */ }
	
	/**
	 * Gets an injector instance according to the qualifier passed.
	 * @param qualifier The qualifier string to determine which Injector should
	 * be returned.
	 * @return If the qualifier is equivalent to {@link InjectionManager#ROOT_QUALIFIER}
	 * then the root Injector will be returned, else the Injector with the
	 * corresponding qualifier will be retrieved from the map and returned.
	 */
	public static Injector getInjector(String qualifier){
		if(qualifier.equals(ROOT_QUALIFIER)) return ROOT;
		else return INSTANCES.computeIfAbsent(qualifier, QualifiedInjector::new);
	}
	
	/**
	 * Gets the Injector instance corresponding to a class.
	 * @param c The class for which to retrieve the managing Injector.
	 * @return <ul>
	 * <li>If the class is not annotated with {@link Qualifier},
	 * return the default injector</li>
	 * <li>If the class is annotated, but a specific value has not
	 * been set (the annotation value defaults to {@link #ROOT_QUALIFIER}),
	 * return the root Injector.</li>
	 * <li>Otherwise return the Injector corresponding to the value of
	 * the <code>@Qualifier</code> annotation using {@link #getInjector(String)}.</li>
	 * </ul>
	 */
	public static Injector getInjector(Class<?> c){
		Qualifier q = c.getAnnotation(Qualifier.class);
		if(q == null) return defaultInjector;
		else if(q.value().equals(ROOT_QUALIFIER)) return ROOT;
		else return getInjector(q.value());
	}
	
	/**
	 * @return The default Injector
	 */
	public static Injector getDefaultInjector(){
		return defaultInjector;
	}
	
	/**
	 * Sets the default injector manually to a new Injector
	 * @param qualifier The qualifier to use to retrieve the
	 * new default injector using {@link #getInjector(String)}
	 */
	public static void setDefaultInjector(String qualifier){
		defaultInjector = getInjector(qualifier);
	}
	
	/**
	 * @return The root Injector
	 */
	public static Injector getRootInjector(){
		return ROOT;
	}
	
	/**
	 * Scans all classes on the class path and, for each
	 * class found, injects dependencies into static
	 * fields annotated by the <code>@Inject</code> annotation. 
	 */
	static void injectStaticFields(){
		for(Class<?> c : ClassPath.getInstance().getClasses()){
			getInjector(c).injectIntoStaticFields(c);
		}
	}
	
}
