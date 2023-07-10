package doa.engine.scene.elements.scripts;

import static doa.engine.input.DoaKeyboard.KEY_DOWN;
import static doa.engine.input.DoaKeyboard.KEY_LEFT;
import static doa.engine.input.DoaKeyboard.KEY_RIGHT;
import static doa.engine.input.DoaKeyboard.KEY_UP;

import org.jbox2d.common.Vec2;

import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaPhysics;

/**
 * A simple arrow keys controller. Arrow keys translate the transform of which
 * DoaObject this script is attached to. This script is provided for means of
 * quick and easy debug and shouldn't be used in production.
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaArrowKeysControl extends DoaScript {

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
		if (KEY_UP)					{ direction.y = -1; }
		if (KEY_LEFT)				{ direction.x = -1; }
		if (KEY_DOWN)				{ direction.y = 1; }
		if (KEY_RIGHT)				{ direction.x = 1; }
		if (KEY_UP && KEY_DOWN)		{ direction.y = 0; }
		if (KEY_LEFT && KEY_RIGHT)	{ direction.x = 0; }

		if (direction.magnitude() > 0) {
			var normalised = DoaVector.normalise(direction);
			if (getOwner().rigidBody == null) {
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
