package com.doa.maths;

/**
 * 2D float vector.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.0
 */
public class DoaVectorF implements Cloneable {

	/**
	 * the displacement in x this vector represents
	 */
	public float x;

	/**
	 * the displacement in y this vector represents
	 */
	public float y;

	/**
	 * Constructor. Creates a {@code DoaVectorF} with displacement in x and y as 0.
	 */
	public DoaVectorF() {
		this(0, 0);
	}

	/**
	 * Constructor. Creates a {@code DoaVectorF} with displacement in x and y with
	 * the given parameters.
	 *
	 * @param x the displacement in x
	 * @param y the displacement in y
	 */
	public DoaVectorF(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Adds the parameter to this instance of {@code DoaVectorF}, and returns the
	 * resulting {@code DoaVectorF}.
	 *
	 * @param other the {@code DoaVectorF} to add to this {@code DoaVectorF}
	 * @return the result of addition
	 */
	public DoaVectorF add(final DoaVectorF other) {
		x += other.x;
		y += other.y;
		return clone();
	}

	/**
	 * Subtracts the parameter from this instance of {@code DoaVectorF}, and returns
	 * the resulting {@code DoaVectorF}.
	 *
	 * @param other the {@code DoaVectorF} to subtract from this {@code DoaVectorF}
	 * @return the result of subtraction
	 */
	public DoaVectorF sub(final DoaVectorF other) {
		x -= other.x;
		y -= other.y;
		return clone();
	}

	/**
	 * Multiplies the parameter with this instance of {@code DoaVectorF}, and
	 * returns the resulting {@code DoaVectorF}.
	 *
	 * @param n the float to multiply with this {@code DoaVectorF}
	 * @return the result of multiplication
	 */
	public DoaVectorF mul(final float n) {
		x *= n;
		y *= n;
		return clone();
	}

	/**
	 * Translates this {@code DoaVectorF} with the parameters dx and dy, and returns
	 * the resulting {@code DoaVectorF}.
	 *
	 * @param dx translation in x axis
	 * @param dy translation in y axis
	 * @return the result of translation
	 */
	public DoaVectorF translate(final float dx, final float dy) {
		x += dx;
		y += dy;
		return clone();
	}

	/**
	 * Rotates this {@code DoaVectorF} around the origin of the coordinate
	 * system(default (0,0)), and returns the resulting {@code DoaVectorF}.
	 *
	 * @param angleDeg rotation angle in degrees
	 * @return the result of translation
	 */
	public DoaVectorF rotateAngle(final double angleDeg) {
		return rotateRadians(Math.toRadians(angleDeg));
	}

	/**
	 * Rotates this {@code DoaVectorF} around the origin of the coordinate
	 * system(default (0,0)), and returns the resulting {@code DoaVectorF}.
	 *
	 * @param angleRad rotation angle in radians
	 * @return the result of translation
	 */
	public DoaVectorF rotateRadians(final double angleRad) {
		final float newX = (float) (Math.cos(angleRad) * x - Math.sin(angleRad) * y);
		final float newY = (float) (Math.sin(angleRad) * x + Math.cos(angleRad) * y);
		x = newX;
		y = newY;
		return clone();
	}

	/**
	 * Multiplies the parameter {@code DoaVectorF} with this instance of
	 * {@code DoaVectorF}, and returns the resulting {@code DoaVectorF}.
	 *
	 * @param other the {@code DoaVectorF} to multiply with this {@code DoaVectorF}
	 * @return the result of multiplication
	 */
	public float mul(final DoaVectorF other) {
		return x * other.y + y * other.x;
	}

	/**
	 * Negates(inverses) this {@code DoaVectorF} and returns the resulting
	 * {@code DoaVectorF}.
	 *
	 * @return the result of negation
	 */
	public DoaVectorF negate() {
		x *= -1;
		y *= -1;
		return clone();
	}

	/**
	 * Normalises this {@code DoaVectorF} and returns the resulting
	 * {@code DoaVectorF}.
	 *
	 * @return the result of normalisation
	 */
	public DoaVectorF normalise() {
		final double norm = norm();
		x /= norm;
		y /= norm;
		return clone();
	}

	/**
	 * Calculates and returns the norm of this {@code DoaVectorF}.
	 *
	 * @return the norm of {@code DoaVectorF}
	 */
	public double norm() {
		return Math.sqrt(normSquared());
	}

	/**
	 * Calculates and returns the square of norm of this {@code DoaVectorF}.
	 *
	 * @return the square of norm of {@code DoaVectorF}
	 */
	public double normSquared() {
		return Math.pow(x, 2) + Math.pow(y, 2);
	}

	/**
	 * Deep copies this {@code DoaVectorF}.
	 *
	 * @return the deep copy of this {@code DoaVectorF} {@inheritDoc}
	 */
	@Override
	public DoaVectorF clone() {
		return new DoaVectorF(x, y);
	}

	/**
	 * Compares this {@code DoaVectorF} to the specified object. The result is true
	 * if and only if the argument is not null and is a {@code DoaVectorF} object
	 * that represents the same amount of displacement as this {@code DoaVectorF}.
	 *
	 * @param obj the object to compare this {@code DoaVectorF} against
	 * @return true if the given object represents a {@code DoaVectorF} equivalent
	 *         to this {@code DoaVectorF}, false otherwise {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DoaVectorF other = (DoaVectorF) obj;
		return Float.floatToIntBits(x) == Float.floatToIntBits(other.x) && Float.floatToIntBits(y) == Float.floatToIntBits(other.y);
	}

	/**
	 * Returns a hash code for this {@code DoaVectorF}.
	 *
	 * @return hash code value for this {@code DoaVectorF} {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	/**
	 * Returns the String representation of this {@code DoaVectorF}.
	 *
	 * @return the String representation of this {@code DoaVectorF} {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "DoaVectorF [x = " + x + ", y = " + y + "]";
	}
}