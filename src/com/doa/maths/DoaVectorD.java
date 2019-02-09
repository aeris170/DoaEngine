package com.doa.maths;

/**
 * 2D double vector.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.0
 */
public class DoaVectorD implements Cloneable {

	/**
	 * the displacement in x this vector represents
	 */
	public double x;

	/**
	 * the displacement in y this vector represents
	 */
	public double y;

	/**
	 * Constructor. Creates a {@code DoaVectorD} with displacement in x and y as 0.
	 */
	public DoaVectorD() {
		this(0, 0);
	}

	/**
	 * Constructor. Creates a {@code DoaVectorD} with displacement in x and y with
	 * the given parameters.
	 *
	 * @param x the displacement in x
	 * @param y the displacement in y
	 */
	public DoaVectorD(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Adds the parameter to this instance of {@code DoaVectorD}, and returns the
	 * resulting {@code DoaVectorD}.
	 *
	 * @param other the {@code DoaVectorD} to add to this {@code DoaVectorD}
	 * @return the result of addition
	 */
	public DoaVectorD add(final DoaVectorD other) {
		x += other.x;
		y += other.y;
		return clone();
	}

	/**
	 * Subtracts the parameter from this instance of {@code DoaVectorD}, and returns
	 * the resulting {@code DoaVectorD}.
	 *
	 * @param other the {@code DoaVectorD} to subtract from this {@code DoaVectorD}
	 * @return the result of subtraction
	 */
	public DoaVectorD sub(final DoaVectorD other) {
		x -= other.x;
		y -= other.y;
		return clone();
	}

	/**
	 * Multiplies the parameter with this instance of {@code DoaVectorD}, and
	 * returns the resulting {@code DoaVectorD}.
	 *
	 * @param n the double to multiply with this {@code DoaVectorD}
	 * @return the result of multiplication
	 */
	public DoaVectorD mul(final double n) {
		x *= n;
		y *= n;
		return clone();
	}

	/**
	 * Translates this {@code DoaVectorD} with the parameters dx and dy, and returns
	 * the resulting {@code DoaVectorD}.
	 *
	 * @param dx translation in x axis
	 * @param dy translation in y axis
	 * @return the result of translation
	 */
	public DoaVectorD translate(final double dx, final double dy) {
		x += dx;
		y += dy;
		return clone();
	}

	/**
	 * Rotates this {@code DoaVectorD} around the origin of the coordinate
	 * system(default (0,0)), and returns the resulting {@code DoaVectorD}.
	 *
	 * @param angleDeg rotation angle in degrees
	 * @return the result of translation
	 */
	public DoaVectorD rotateAngle(final double angleDeg) {
		return rotateRadians(Math.toRadians(angleDeg));
	}

	/**
	 * Rotates this {@code DoaVectorD} around the origin of the coordinate
	 * system(default (0,0)), and returns the resulting {@code DoaVectorD}.
	 *
	 * @param angleRad rotation angle in radians
	 * @return the result of translation
	 */
	public DoaVectorD rotateRadians(final double angleRad) {
		final double newX = Math.cos(angleRad) * x - Math.sin(angleRad) * y;
		final double newY = Math.sin(angleRad) * x + Math.cos(angleRad) * y;
		x = newX;
		y = newY;
		return clone();
	}

	/**
	 * Multiplies the parameter {@code DoaVectorD} with this instance of
	 * {@code DoaVectorD}, and returns the resulting {@code DoaVectorD}.
	 *
	 * @param other the {@code DoaVectorD} to multiply with this {@code DoaVectorD}
	 * @return the result of multiplication
	 */
	public double mul(final DoaVectorD other) {
		return x * other.y + y * other.x;
	}

	/**
	 * Negates(inverses) this {@code DoaVectorD} and returns the resulting
	 * {@code DoaVectorD}.
	 *
	 * @return the result of negation
	 */
	public DoaVectorD negate() {
		mul(-1);
		return clone();
	}

	/**
	 * Normalises this {@code DoaVectorD} and returns the resulting
	 * {@code DoaVectorD}.
	 *
	 * @return the result of normalisation
	 */
	public DoaVectorD normalise() {
		mul(1 / norm());
		return clone();
	}

	/**
	 * Calculates and returns the norm of this {@code DoaVectorD}.
	 *
	 * @return the norm of {@code DoaVectorD}
	 */
	public double norm() {
		return Math.sqrt(normSquared());
	}

	/**
	 * Calculates and returns the square of norm of this {@code DoaVectorD}.
	 *
	 * @return the square of norm of {@code DoaVectorD}
	 */
	public double normSquared() {
		return Math.pow(x, 2) + Math.pow(y, 2);
	}

	/**
	 * Deep copies this {@code DoaVectorD}.
	 *
	 * @return the deep copy of this {@code DoaVectorD} {@inheritDoc}
	 */
	@Override
	public DoaVectorD clone() {
		return new DoaVectorD(x, y);
	}

	/**
	 * Compares this {@code DoaVectorD} to the specified object. The result is true
	 * if and only if the argument is not null and is a {@code DoaVectorD} object
	 * that represents the same amount of displacement as this {@code DoaVectorD}.
	 *
	 * @param obj the object to compare this {@code DoaVectorD} against
	 * @return true if the given object represents a {@code DoaVectorD} equivalent
	 *         to this {@code DoaVectorD}, false otherwise {@inheritDoc}
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
		final DoaVectorD other = (DoaVectorD) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
			return false;
		}
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a hash code for this {@code DoaVectorD}.
	 *
	 * @return hash code value for this {@code DoaVectorD} {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ temp >>> 32);
		return result;
	}

	/**
	 * Returns the String representation of this {@code DoaVectorD}.
	 *
	 * @return the String representation of this {@code DoaVectorD} {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "DoaVectorD [x = " + x + ", y = " + y + "]";
	}
}