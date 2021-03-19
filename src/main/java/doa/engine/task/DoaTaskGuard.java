package doa.engine.task;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Wrapper for AtomicBoolean. This class only exists to provide consistent naming within all the
 * DoaEngine classes. This class behaves exactly as how it's super-class would behave in any and all
 * circumstances.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 3.0
 */
public class DoaTaskGuard extends AtomicBoolean {

	private static final long serialVersionUID = 8734303064285056054L;

	/**
	 * Instantiates a DoaTaskGuard with its initialValue as true.
	 */
	public DoaTaskGuard() {
		super(true);
	}

	/**
	 * Instantiates a DoaTaskGuard with the initial value.
	 *
	 * @param initialValue what this task guard is set to at time = 0
	 */
	public DoaTaskGuard(final boolean initialValue) {
		super(initialValue);
	}
}