package org.proton_di.dependency.loaders;

import java.util.List;

import org.proton_di.dependency.suppliers.DependencySupplier;

/**
 * An interface to define classes that load dependencies
 * as a {@link DependencySupplier} from sources on the classpath.
 * 
 * @author Luke Stevens
 */
public interface DependencyLoader {
	
	/**
	 * @return Gets a list of dependencies as Dependency
	 * suppliers from sources on the classpath.
	 */
	public List<DependencySupplier> getDependencies();

}
