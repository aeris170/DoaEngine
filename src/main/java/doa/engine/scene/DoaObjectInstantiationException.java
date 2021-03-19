package doa.engine.scene;

/**
 * Thrown when a DoaObject is tried to be instantiated by the DoaObjectBuilder,
 * but failed.
 * 
 * @deprecated This exception and all of its functionality has been deprecated
 *             and will be removed at some future date.
 * @author Doga Oruc
 * @since DoaEngine 2.6.1
 * @version 3.0
 */
@Deprecated(since = "3.0", forRemoval = true)
public class DoaObjectInstantiationException extends IllegalArgumentException {

	private static final long serialVersionUID = 6545994357059285780L;

	/**
	 * Constructor.
	 *
	 * @param s message of the exception
	 */
	DoaObjectInstantiationException(final String s) { super(s); }
}
