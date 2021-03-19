package doa.engine.utils;

/**
 * Useful functions to shorten AND/OR simply code.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 3.0
 */
public final class DoaUtils {

	private DoaUtils() {}

	/**
	 * Pauses the calling thread to sleep for millis milliseconds. Put here to reduce the amount of
	 * try's and catch's in the program code. <br>
	 * Causes the currently executing thread to sleep (temporarily cease execution) for the specified
	 * number of milliseconds, subject to the precision and accuracy of system timers and schedulers.
	 * The thread does not lose ownership of any monitors.
	 *
	 * @param millis the length of time to sleep in milliseconds
	 * @throws IllegalArgumentException if the value of millis is negative
	 */
	public static void sleepFor(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
	}
}