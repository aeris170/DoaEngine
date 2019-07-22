package com.doa.engine;

/**
 * Thrown when a {@code DoaObject} is tried to instantiated by the {@code DoaHandler}, but failed.
 *
 * @author Doga Oruc
 * @since DoaEngine 2.6.1
 * @version 2.6.1
 */
public class DoaObjectInstantiationException extends IllegalArgumentException {

	private static final long serialVersionUID = 6545994357059285780L;

	/**
	 * Constructor.
	 *
	 * @param s message of the exception
	 */
	public DoaObjectInstantiationException(final String s) {
		super(s);
	}
}
