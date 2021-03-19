package doa.engine.maths;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * 2D float vector.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 3.0
 */
public strictfp class DoaVector implements Serializable {

	private static final long serialVersionUID = 8419672587480255607L;

	/**
	 * the displacement in x this vector represents
	 */
	public float x;

	/**
	 * the displacement in y this vector represents
	 */
	public float y;

	/**
	 * Constructor. Creates a DoaVector with displacement in x and y as 0.
	 */
	public DoaVector() { this(0, 0); }

	/**
	 * Constructor. Creates a DoaVector with displacement in x and y with the given
	 * parameters.
	 *
	 * @param x the displacement in x
	 * @param y the displacement in y
	 */
	public DoaVector(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Copy Constructor. Creates a DoaVector with displacement in x and y same as
	 * given parameter.
	 *
	 * @param other the DoaVector to copy
	 */
	public DoaVector(@NotNull final DoaVector other) { this(other.x, other.y); }

	/**
	 * Adds the left and right and returns the resulting vector. This function
	 * always creates a new vector.
	 *
	 * @param left the term on the left side of the plus sign
	 * @param right the term on the right side of the plus sign
	 * @return the resulting vector
	 */
	public static DoaVector add(@NotNull final DoaVector left, @NotNull final DoaVector right) {
		return new DoaVector(left.x + right.x, left.y + right.y);
	}

	/**
	 * Adds the left and right and writes the resulting vector into result. left +
	 * right = result
	 *
	 * @param left the term on the left side of the plus sign
	 * @param right the term on the right side of the plus sign
	 * @param result the resulting vector
	 */
	public static void add(@NotNull final DoaVector left, @NotNull final DoaVector right, @NotNull final DoaVector result) {
		result.x = left.x + right.x;
		result.y = left.y + right.y;
	}

	/**
	 * Subtracts the left and right and returns the resulting vector. This function
	 * always creates a new vector.
	 *
	 * @param left the term on the left side of the minus sign
	 * @param right the term on the right side of the minus sign
	 * @return the resulting vector
	 */
	public static DoaVector sub(@NotNull final DoaVector left, @NotNull final DoaVector right) {
		return new DoaVector(left.x - right.x, left.y - right.y);
	}

	/**
	 * Subtracts the left and right and writes the resulting vector into result.
	 * left - right = result
	 *
	 * @param left the term on the left side of the minus sign
	 * @param right the term on the right side of the minus sign
	 * @param result the resulting vector
	 */
	public static void sub(@NotNull final DoaVector left, @NotNull final DoaVector right, @NotNull final DoaVector result) {
		result.x = left.x - right.x;
		result.y = left.y - right.y;
	}

	/**
	 * Multiples the vector with a scaler and returns the resulting vector. This
	 * function always creates a new vector.
	 *
	 * @param vector the vector to get multiplied with a scaler
	 * @param scaler the scaler to scale the vector
	 * @return the resulting vector
	 */
	public static DoaVector mul(@NotNull final DoaVector vector, final float scaler) {
		return new DoaVector(vector.x * scaler, vector.y * scaler);
	}

	/**
	 * Multiples the vector with a scaler and writes the resulting vector into
	 * result. vector * scaler = result
	 *
	 * @param vector the vector to get multiplied with a scaler
	 * @param scaler the scaler to scale the vector
	 * @param result the resulting vector
	 */
	public static void mul(@NotNull final DoaVector vector, final float scaler, @NotNull final DoaVector result) {
		result.x = vector.x * scaler;
		result.y = vector.y * scaler;
	}

	/**
	 * Translates the vector in the x direction by dx and in the y direction by dy
	 * and returns the resulting vector. This function always creates a new vector.
	 *
	 * @param vector the vector translate
	 * @param dx the change in x
	 * @param dy the change in y
	 * @return the resulting vector
	 */
	public static DoaVector translate(@NotNull final DoaVector vector, final float dx, final float dy) {
		return new DoaVector(vector.x + dx, vector.y + dy);
	}

	/**
	 * Translates the vector in the x direction by dx and in the y direction by dy
	 * and writes the resulting vector into result. vector + (dx, dy) = result
	 *
	 * @param vector the vector translate
	 * @param dx the change in x
	 * @param dy the change in y
	 * @param result the resulting vector
	 */
	public static void translate(@NotNull final DoaVector vector, final float dx, final float dy, @NotNull final DoaVector result) {
		result.x = vector.x + dx;
		result.y = vector.y + dy;
	}

	/**
	 * Rotates the vector around origin by theta degrees and returns the resulting
	 * vector. This function always creates a new vector.
	 *
	 * @param vector the vector to rotate
	 * @param theta the angle to rotate in degrees
	 * @return the resulting vector
	 */
	public static DoaVector rotateAroundOrigin(@NotNull final DoaVector vector, final float theta) {
		final float rad = (float) Math.toRadians(theta);
		final float sin = (float) Math.sin(rad);
		final float cos = (float) Math.cos(rad);
		return new DoaVector(cos * vector.x - sin * vector.y, sin * vector.x + cos * vector.y);
	}

	/**
	 * Rotates the vector around origin by theta degrees and writes the resulting
	 * vector into result.
	 *
	 * @param vector the vector to rotate
	 * @param theta the angle to rotate in degrees
	 * @param result the resulting vector
	 */
	public static void rotateAroundOrigin(@NotNull final DoaVector vector, final float theta, @NotNull final DoaVector result) {
		final float rad = (float) Math.toRadians(theta);
		final float sin = (float) Math.sin(rad);
		final float cos = (float) Math.cos(rad);
		result.x = cos * vector.x - sin * vector.y;
		result.y = sin * vector.x + cos * vector.y;
	}

	/**
	 * Negates(inverses) the vector and returns the resulting vector. This function
	 * always creates a new vector.
	 *
	 * @param vector the vector to negate
	 * @return the resulting vector
	 */
	public static DoaVector negate(@NotNull final DoaVector vector) {
		return new DoaVector(vector.x * -1, vector.y * -1);
	}

	/**
	 * Negates(inverses) the vector and writes the resulting vector into result.
	 * vector * (-1, -1) = result
	 *
	 * @param vector the vector to negate
	 * @param result the resulting vector
	 */
	public static void negate(@NotNull final DoaVector vector, @NotNull final DoaVector result) {
		result.x = vector.x * -1;
		result.y = vector.y * -1;
	}

	/**
	 * Normalises the vector and returns the resulting vector. This function always
	 * creates a new vector.
	 *
	 * @param vector the vector to normalise
	 * @return the resulting vector
	 */
	public static DoaVector normalise(@NotNull final DoaVector vector) {
		float mag = vector.magnitude();
		return new DoaVector(vector.x / mag, vector.y / mag);
	}

	/**
	 * Normalises the vector and writes the resulting vector into result. vector /
	 * ||vector|| = result
	 *
	 * @param vector the vector to normalise
	 * @param result the resulting vector
	 */
	public static void normalise(@NotNull final DoaVector vector, @NotNull final DoaVector result) {
		float mag = vector.magnitude();
		result.x = vector.x / mag;
		result.y = vector.y / mag;
	}

	/**
	 * Calculates and returns the norm of this DoaVector.
	 *
	 * @return the norm of DoaVector
	 */
	public float magnitude() { return (float) Math.sqrt(magnitudeSquared()); }

	/**
	 * Calculates and returns the square of norm of this DoaVector.
	 *
	 * @return the square of norm of DoaVector
	 */
	public float magnitudeSquared() { return x * x + y * y; }

	/**
	 * Compares this DoaVector to the specified object. The result is true if and
	 * only if the argument is not null and is a DoaVector object that represents
	 * the same amount of displacement as this DoaVector.
	 *
	 * @param obj the object to compare this DoaVector against
	 * @return true if the given object represents a DoaVector equivalent to this
	 * DoaVector, false otherwise {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		final DoaVector other = (DoaVector) obj;
		return Float.floatToIntBits(x) == Float.floatToIntBits(other.x) && Float.floatToIntBits(y) == Float
			.floatToIntBits(other.y);
	}

	/**
	 * Returns a hash code for this DoaVector.
	 *
	 * @return hash code value for this DoaVector {@inheritDoc}
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
	 * Returns the String representation of this DoaVector.
	 *
	 * @return the String representation of this DoaVector {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new StringBuilder(128).append("DoaVector [\n\t{\n\t\tx:")
			.append(x)
			.append(",\n\t\ty:")
			.append(y)
			.append("\n\t}\n]")
			.toString();
	}
}
