package com.doa.maths;

import java.io.Serializable;

/**
 * 2D int vector.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.7
 */
public class DoaVectorI implements Cloneable, Serializable {

	private static final long serialVersionUID = 947702833498717348L;

	/**
	 * the displacement in x this vector represents
	 */
	public int x;

	/**
	 * the displacement in y this vector represents
	 */
	public int y;

	/**
	 * Constructor. Creates a {@code DoaVectorI} with displacement in x and y as 0.
	 */
	public DoaVectorI() {
		this(0, 0);
	}

	/**
	 * Constructor. Creates a {@code DoaVectorI} with displacement in x and y with the given parameters.
	 *
	 * @param x the displacement in x
	 * @param y the displacement in y
	 */
	public DoaVectorI(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Adds the parameter to this instance of {@code DoaVectorI}, and returns the resulting
	 * {@code DoaVectorI}.
	 *
	 * @param other the {@code DoaVectorI} to add to this {@code DoaVectorI}
	 * @return the result of addition
	 */
	public DoaVectorI add(final DoaVectorI other) {
		x += other.x;
		y += other.y;
		return clone();
	}

	/**
	 * Subtracts the parameter from this instance of {@code DoaVectorI}, and returns the resulting
	 * {@code DoaVectorI}.
	 *
	 * @param other the {@code DoaVectorI} to subtract from this {@code DoaVectorI}
	 * @return the result of subtraction
	 */
	public DoaVectorI sub(final DoaVectorI other) {
		x -= other.x;
		y -= other.y;
		return clone();
	}

	/**
	 * Multiplies the parameter with this instance of {@code DoaVectorI}, and returns the resulting
	 * {@code DoaVectorI}.
	 *
	 * @param n the int to multiply with this {@code DoaVectorI}
	 * @return the result of multiplication
	 */
	public DoaVectorI mul(final int n) {
		x *= n;
		y *= n;
		return clone();
	}

	/**
	 * Translates this {@code DoaVectorI} with the parameters dx and dy, and returns the resulting
	 * {@code DoaVectorI}.
	 *
	 * @param dx translation in x axis
	 * @param dy translation in y axis
	 * @return the result of translation
	 */
	public DoaVectorI translate(final int dx, final int dy) {
		x += dx;
		y += dy;
		return clone();
	}

	/**
	 * Rotates this {@code DoaVectorI} around the origin of the coordinate system(default (0,0)), and
	 * returns the resulting {@code DoaVectorI}.
	 *
	 * @param angleDeg rotation angle in degrees
	 * @return the result of translation
	 */
	public DoaVectorI rotateAngle(final double angleDeg) {
		return rotateRadians(Math.toRadians(angleDeg));
	}

	/**
	 * Rotates this {@code DoaVectorI} around the origin of the coordinate system(default (0,0)), and
	 * returns the resulting {@code DoaVectorI}.
	 *
	 * @param angleRad rotation angle in radians
	 * @return the result of translation
	 */
	public DoaVectorI rotateRadians(final double angleRad) {
		final int newX = (int) (Math.cos(angleRad) * x - Math.sin(angleRad) * y);
		final int newY = (int) (Math.sin(angleRad) * x + Math.cos(angleRad) * y);
		x = newX;
		y = newY;
		return clone();
	}

	/**
	 * Multiplies the parameter {@code DoaVectorI} with this instance of {@code DoaVectorI}, and returns
	 * the resulting {@code DoaVectorI}.
	 *
	 * @param other the {@code DoaVectorI} to multiply with this {@code DoaVectorI}
	 * @return the result of multiplication
	 */
	public int mul(final DoaVectorI other) {
		return x * other.y + y * other.x;
	}

	/**
	 * Negates(inverses) this {@code DoaVectorI} and returns the resulting {@code DoaVectorI}.
	 *
	 * @return the result of negation
	 */
	public DoaVectorI negate() {
		x *= -1;
		y *= -1;
		return clone();
	}

	/**
	 * Normalises this {@code DoaVectorI} and returns the resulting {@code DoaVectorI}.
	 *
	 * @return the result of normalisation
	 */
	public DoaVectorI normalise() {
		final double norm = norm();
		x /= norm;
		y /= norm;
		return clone();
	}

	/**
	 * Calculates and returns the norm of this {@code DoaVectorI}.
	 *
	 * @return the norm of {@code DoaVectorI}
	 */
	public double norm() {
		return Math.sqrt(normSquared());
	}

	/**
	 * Calculates and returns the square of norm of this {@code DoaVectorI}.
	 *
	 * @return the square of norm of {@code DoaVectorI}
	 */
	public double normSquared() {
		return Math.pow(x, 2) + Math.pow(y, 2);
	}

	/**
	 * Deep copies this {@code DoaVectorI}. The returned copy will satisfy the conditions below:
	 * <ul>
	 * <li>originalDoaVectorI == originalDoaVectorI.clone() will return false</li>
	 * <li>originalDoaVectorI.equals(originalDoaVectorI.clone()) will return true</li>
	 * </ul>
	 *
	 * @return the deep copy of this {@code DoaVectorI} {@inheritDoc}
	 */
	@Override
	public DoaVectorI clone() {
		return new DoaVectorI(x, y);
	}

	/**
	 * Compares this {@code DoaVectorI} to the specified object. The result is true if and only if the
	 * argument is not null and is a {@code DoaVectorI} object that represents the same amount of
	 * displacement as this {@code DoaVectorI}.
	 *
	 * @param obj the object to compare this {@code DoaVectorI} against
	 * @return true if the given object represents a {@code DoaVectorI} equivalent to this
	 *         {@code DoaVectorI}, false otherwise {@inheritDoc}
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
		final DoaVectorI other = (DoaVectorI) obj;
		return x == other.x && y == other.y;
	}

	/**
	 * Returns a hash code for this {@code DoaVectorI}.
	 *
	 * @return hash code value for this {@code DoaVectorI} {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/**
	 * Returns the String representation of this {@code DoaVectorI}.
	 *
	 * @return the String representation of this {@code DoaVectorI} {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new StringBuilder(128).append("DoaVectorI [\n\t{\n\t\tx:").append(x).append(",\n\t\ty:").append(y).append("\n\t}\n]").toString();
	}
}