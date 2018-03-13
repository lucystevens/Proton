package com.lithium.dependency.loaders;

import java.util.List;

import com.lithium.dependency.suppliers.DependencySupplier;

public interface DependencyLoader {
	
	public List<DependencySupplier> getDependencies();

}
