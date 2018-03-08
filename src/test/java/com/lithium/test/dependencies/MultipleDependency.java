package com.lithium.test.dependencies;

import java.io.Serializable;
import com.lithium.dependency.Dependency;
import com.lithium.dependency.InstanceType;

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
