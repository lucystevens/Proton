package com.lithium.inject;

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
class RootInjector extends AbstractInjector{
	
	/**
	 * Creates the single instance of this Injector,
	 * by scanning all classes on the classpath and
	 * injected static dependencies where appropriate.
	 */
	RootInjector(){
		super();
		this.dependencies.put(RootInjector.class, () -> this);
	}

}
