package com.lithium.inject;

import java.util.HashMap;
import java.util.Map;

import com.lithium.configuration.Qualifier;

public class InjectionManager {
	
	public static final String ROOT_QUALIFIER = "root";
	
	private static final Injector ROOT = new RootInjector();
	private static final Map<String, Injector> INSTANCES = new HashMap<>();
	private static Injector DEFAULT;
	
	static{
		String defaultQualifier = System.getProperty("proton.injector.default");
		DEFAULT = (defaultQualifier==null)? ROOT : getInjector(defaultQualifier);
	}
	
	/**
	 * Used to load the Injection Manager class and initialise
	 * the RootInjector singleton instance.<br>
	 * 
	 * Calling {@link InjectionManager#getInjector()} or 
	 * <code>Class.forName("com.lithium.inject.Injector")</code>
	 * will achieve the same results, but this method makes the
	 * intention more obvious.
	 */
	public static void init(){ /* Doesn't do anything other than force loading of the class */ };
	
	public static Injector getInjector(String qualifier){
		if(qualifier.equals(ROOT_QUALIFIER)) return ROOT;
		else {
			
			synchronized (Injector.class) {
				Injector i = INSTANCES.get(qualifier);
				if(i == null){
					i = new QualifiedInjector(qualifier);
					INSTANCES.put(qualifier, i);
				}
				return i;
			}
		}
	}
	
	public static Injector getInjector(Class<?> c){
		Qualifier q = c.getAnnotation(Qualifier.class);
		if(q == null || q.value().equals(ROOT_QUALIFIER)) return ROOT;
		else return getInjector(q.value());
	}
	
	public static Injector getDefaultInjector(){
		return DEFAULT;
	}
	
	public static void setDefaultInjector(String qualifier){
		DEFAULT = getInjector(qualifier);
	}
	
	public static Injector getRootInjector(){
		return ROOT;
	}

}
