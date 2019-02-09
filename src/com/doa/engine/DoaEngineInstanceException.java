package com.doa.engine;

import java.util.MissingResourceException;

/**
 * Thrown when certain {@code DoaEngine} parts are tried to be instantiated more
 * than once. By definition, they are singleton. This exception enforces that
 * feature.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 1.1
 */
public class DoaEngineInstanceException extends MissingResourceException {

	private static final long serialVersionUID = 8678092536847603328L;

	/**
	 * Constructor
	 *
	 * @param s Message of the exception
	 * @param className Name of the throwing class
	 * @param key Key of the exception
	 */
	public DoaEngineInstanceException(final String s, final String className, final String key) {
		super(s, className, key);
	}
}