package com.doa.engine;

/**
 * Thrown when {@code DoaEngine} is tried to put in an illegal state, such as
 * tried to be run whilst already running.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 1.1
 */
public class DoaEngineStateException extends IllegalStateException {

	private static final long serialVersionUID = -1869227012244052674L;

	/**
	 * Constructor
	 *
	 * @param message Message of the exception
	 */
	public DoaEngineStateException(final String message) {
		super(message);
	}
}