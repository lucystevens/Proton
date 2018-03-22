package org.proton_di.test.dependencies;

import java.io.Serializable;

import org.proton_di.dependency.Dependency;
import org.proton_di.dependency.InstanceType;

@Dependency(type = InstanceType.MULTIPLE)
public class MultipleDependency implements Serializable{

	private static final long serialVersionUID = -3000498178277330865L;

	private boolean unique = true;
	
	public boolean isUniqueInstance(){
		if(unique){
			unique = false;
			return true;
		}
		else return false;
	}

}
