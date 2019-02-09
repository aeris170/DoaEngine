package com.doa.engine.task;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Wrapper for {@code AtomicBoolean}. This class only exists to provide
 * consistent naming within all the {@code DoaEngine} classes. This class
 * behaves exactly as how it's super-class would behave in any and all
 * circumstances.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 1.1
 */
public class DoaTaskGuard extends AtomicBoolean {

	private static final long serialVersionUID = 8734303064285056054L;

	public DoaTaskGuard(final boolean initialValue) {
		super(initialValue);
	}
}