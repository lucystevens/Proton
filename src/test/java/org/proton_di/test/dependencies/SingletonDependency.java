package org.proton_di.test.dependencies;

import java.io.Serializable;

import org.proton_di.dependency.Dependency;
import org.proton_di.dependency.InstanceType;

@Dependency(type = InstanceType.SINGLETON)
public class SingletonDependency implements Serializable{
	
	private static final long serialVersionUID = 3068463032517817157L;

	private static int instanceCount = 0;
	
	private int instance;
	
	private SingletonDependency(){
		instance = ++instanceCount;
	}

	public int getInstance() {
		return instance;
	}

}
