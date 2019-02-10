package com.doa.maths;

/**
 * Math utilities.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 1.1
 */
public final class DoaMath {

	private DoaMath() {}

	/**
	 * Clamps a int variable between the given min and max values.
	 *
	 * @param value the variable to clamp
	 * @param min lower bound
	 * @param max upper bound
	 * @return value if min &lt; value &lt; max, min if value &lt; min, max if value
	 *         &gt; max
	 */
	public static int clamp(final int value, final int min, final int max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * Clamps a float variable between the given min and max values.
	 *
	 * @param value the variable to clamp
	 * @param min lower bound
	 * @param max upper bound
	 * @return value if min &lt; value &lt; max, min if value &lt; min, max if value
	 *         &gt; max
	 */
	public static float clamp(final float value, final float min, final float max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * Clamps a double variable between the given min and max values.
	 *
	 * @param value the variable to clamp
	 * @param min lower bound
	 * @param max upper bound
	 * @return value if min &lt; value &lt; max, min if value &lt; min, max if value
	 *         &gt; max
	 */
	public static double clamp(final double value, final double min, final double max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * Linearly interpolates between first and second by amt. The parameter amt
	 * should be in the range [0, 1]. When t = 0, returns first. When t = 1, returns
	 * second. When t = 0.5, returns the midpoint of first and second.
	 * {@code DoaEngine} guarantees the result to be true if and only if the
	 * parameter amt is in the range [0, 1].
	 *
	 * @param first start value
	 * @param second end value
	 * @param amt interpolation value between the two ints
	 * @return the interpolated int result between the two int values
	 */
	public static int lerp(final int first, final int second, final int amt) {
		return first * (1 - amt) + second * amt;
	}

	/**
	 * Linearly interpolates between first and second by amt. The parameter amt
	 * should be in the range [0, 1]. When t = 0, returns first. When t = 1, returns
	 * second. When t = 0.5, returns the midpoint of first and second.
	 * {@code DoaEngine} guarantees the result to be true if and only if the
	 * parameter amt is in the range [0, 1].
	 *
	 * @param first start value
	 * @param second end value
	 * @param amt interpolation value between the two floats
	 * @return the interpolated float result between the two float values
	 */
	public static float lerp(final float first, final float second, final float amt) {
		return first * (1 - amt) + second * amt;
	}

	/**
	 * Linearly interpolates between first and second by amt. The parameter amt
	 * should be in the range [0, 1]. When t = 0, returns first. When t = 1, returns
	 * second. When t = 0.5, returns the midpoint of first and second.
	 * {@code DoaEngine} guarantees the result to be true if and only if the
	 * parameter amt is in the range [0, 1].
	 *
	 * @param first start value
	 * @param second end value
	 * @param amt interpolation value between the two doubles
	 * @return the interpolated double result between the two double values
	 */
	public static double lerp(final double first, final double second, final double amt) {
		return first * (1 - amt) + second * amt;
	}
}