package doa.engine.maths;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Math utilities.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 3.0
 */
public final strictfp class DoaMath {

	private DoaMath() {}

	/**
	 * Clamps a number between the given min and max values.
	 *
	 * @param value the number to clamp
	 * @param min lower bound
	 * @param max upper bound
	 * @return value if min &lt; value &lt; max, min if value &lt; min, max if value
	 *         &gt; max
	 */
	public static float clamp(final float value, final float min, final float max) { return Math.max(min, Math.min(max, value)); }

	/**
	 * Linearly interpolates between first and second by amt. The parameter amt
	 * should be in the range [0, 1]. When t = 0, returns first. When t = 1, returns
	 * second. When t = 0.5, returns the midpoint of first and second. DoaEngine
	 * guarantees the result to be true if and only if the parameter amt is in the
	 * range [0, 1]. Parameter amt not being in the range [0, 1] is an undocumented
	 * behaviour and <em>might</em> give unexpected results.
	 *
	 * @param first start value
	 * @param second end value
	 * @param amt interpolation value between the two doubles
	 * @return the interpolated double result between the two double values
	 */
	public static float lerp(final float first, final float second, final float amt) { return first * (1 - amt) + second * amt; }

	/**
	 * Maps a number from one range to another. Value not being in the range
	 * [sourceRangeMin, sourceRangeMax] is an undocumented behaviour and
	 * <em>might</em> give unexpected results.
	 *
	 * @param value the incoming value to be converted
	 * @param sourceRangeMin lower bound of the value's current range
	 * @param sourceRangeMax upper bound of the value's current range
	 * @param destinationRangeMin lower bound of the value's target range
	 * @param destinationRangeMax upper bound of the value's target range
	 * @return mapped number
	 */
	public static float map(final float value, final float sourceRangeMin, final float sourceRangeMax, final float destinationRangeMin, final float destinationRangeMax) {
		return (value - sourceRangeMin) / (sourceRangeMax - sourceRangeMin) * (destinationRangeMax - destinationRangeMin) + destinationRangeMin;
	}

	/**
	 * Returns a pseudo-random floating point number between the specified lower
	 * (inclusive) and upper (exclusive).
	 *
	 * @param lower the least value returned
	 * @param upper the greatest bound (exclusive)
	 * @return a pseudo-random floating point number between the specified lower
	 *         (inclusive) and upper (exclusive)
	 */
	public static float randomBetween(final float lower, final float upper) { return ThreadLocalRandom.current().nextFloat() * (upper - lower) + lower; }

	/**
	 * Returns a pseudo-random integer between the specified lower (inclusive) and
	 * upper (exclusive).
	 *
	 * @param lower the least value returned
	 * @param upper the greatest bound (exclusive)
	 * @return a pseudo-random integer between the specified lower (inclusive) and
	 *         upper (exclusive)
	 */
	public static int randomIntBetween(final int lower, final int upper) { return ThreadLocalRandom.current().nextInt(lower, upper); }

	/**
	 * Calculates and returns the angle in degrees converted from the parameter.
	 * 
	 * @param radians the angle to convert to degrees, in radians
	 * @return the angle in degrees
	 */
	public static float toDegress(float radians) { return (float) (radians * 180 / Math.PI); }

	/**
	 * Calculates and returns the angle in radians converted from the parameter.
	 * 
	 * @param degrees the angle to convert to radians, in degrees
	 * @return the angle in radians
	 */
	public static float toRadians(float degrees) { return (float) (degrees * Math.PI / 180); }
}
