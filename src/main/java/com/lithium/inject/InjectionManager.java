package com.lithium.inject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lithium.configuration.Qualifier;
import com.lithium.scanner.ClassPath;

public class InjectionManager {
	
	public static final String ROOT_QUALIFIER = "root";
	
	private static final Injector ROOT = new RootInjector();
	private static final Map<String, Injector> INSTANCES = new ConcurrentHashMap<>();
	private static Injector defaultInjector;
	
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
	 * Calling {@link InjectionManager#getInjector()} or 
	 * <code>Class.forName("com.lithium.inject.Injector")</code>
	 * will achieve the same results, but this method makes the
	 * intention more obvious.
	 */
	public static void init(){ /* Doesn't do anything other than force loading of the class */ }
	
	public static Injector getInjector(String qualifier){
		if(qualifier.equals(ROOT_QUALIFIER)) return ROOT;
		else return INSTANCES.computeIfAbsent(qualifier, QualifiedInjector::new);
	}
	
	public static Injector getInjector(Class<?> c){
		Qualifier q = c.getAnnotation(Qualifier.class);
		if(q == null || q.value().equals(ROOT_QUALIFIER)) return ROOT;
		else return getInjector(q.value());
	}
	
	public static Injector getDefaultInjector(){
		return defaultInjector;
	}
	
	public static void setDefaultInjector(String qualifier){
		defaultInjector = getInjector(qualifier);
	}
	
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
