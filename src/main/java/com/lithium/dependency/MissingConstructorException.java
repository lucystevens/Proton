package com.lithium.dependency;

public class MissingConstructorException extends DependencyCreationException {

	private static final long serialVersionUID = -5900761707743382688L;

	public MissingConstructorException(Class<?> c, Class<?>...params) {
		super("Missing constructor with signature: " + formatConstructorSignature(c, params), c);
	}
	
	private static String formatConstructorSignature(Class<?> dependency, Class<?>...params){
		StringBuilder signature = new StringBuilder(dependency.getSimpleName() + "(");
		for(Class<?> param : params){
			signature.append(param.getSimpleName() + ", ");
		}
		return signature.substring(0, signature.length()-2) + ")";
	}

}
