package com.lithium.dependency;

public class MissingConstructorException extends DependencyCreationException {

	private static final long serialVersionUID = -5900761707743382688L;

	/**
	 * Creates a new DependencyCreationException with message <code>Missing constructor with signature:
	 * <i>{@link #formatConstructorSignature(Class, Class...)}</i></code>
	 * @param c The dependency class that failed creation
	 * @param params The parameter classes for the missing constructor
	 */
	public MissingConstructorException(Class<?> c, Class<?>...params) {
		super("Missing constructor with signature: " + formatConstructorSignature(c, params), c);
	}
	
	/**
	 * Creates a String representing a constructor signature
	 * in the format <code><i>dependency class name</i>(<i>param class name</i>,...)</code>
	 * @param dependency The class being constructed
	 * @param params The parameters for the constructor
	 * @return The String representation of a constructor signature
	 */
	private static String formatConstructorSignature(Class<?> dependency, Class<?>...params){
		StringBuilder signature = new StringBuilder(dependency.getSimpleName() + "(");
		for(Class<?> param : params){
			signature.append(param.getSimpleName() + ", ");
		}
		return signature.substring(0, signature.length()-2) + ")";
	}

}
