package doa.engine.scene.elements.scripts;

import static doa.engine.input.DoaKeyboard.A;
import static doa.engine.input.DoaKeyboard.D;
import static doa.engine.input.DoaKeyboard.S;
import static doa.engine.input.DoaKeyboard.W;

import org.jbox2d.common.Vec2;

import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaPhysics;

/**
 * A simple WASD keys controller. WASD keys translate the transform of which
 * DoaObject this script is attached to. This script is provided for means of
 * quick and easy debug and shouldn't be used in production.
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaWASDControl extends DoaScript {

	private static final long serialVersionUID = -6677857421829435345L;

	/**
	 * Speed of the control.
	 */
	public float speed = 1;

	private DoaVector direction = new DoaVector();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tick() {
		speed = Math.abs(speed);
		direction.x = 0;
		direction.y = 0;
		if (W)		{ direction.y = -1; }
		if (A)		{ direction.x = -1; }
		if (S)		{ direction.y = 1; }
		if (D)		{ direction.x = 1; }
		if (W && S)	{ direction.y = 0; }
		if (A && D)	{ direction.x = 0; }

		if (direction.magnitude() > 0) {
			var normalised = DoaVector.normalise(direction);
			if (getOwner().rigidBody != null) {
				getOwner().rigidBody.getNativeBody().setLinearVelocity(new Vec2(normalised.x * speed * DoaPhysics.PPM, normalised.y * speed * DoaPhysics.PPM));
			} else {
				DoaVector.translate(getOwner().transform.position, normalised.x * speed, normalised.y * speed, getOwner().transform.position);
			}
		} else {
			if (getOwner().rigidBody != null) {
				getOwner().rigidBody.getNativeBody().setLinearVelocity(new Vec2(0, 0));
			}
		}
	}
}
