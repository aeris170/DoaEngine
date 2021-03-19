package doa.engine.scene.elements.physics;

import javax.validation.constraints.NotNull;

import org.jbox2d.dynamics.Body;

import doa.engine.Internal;
import doa.engine.maths.DoaMath;
import doa.engine.maths.DoaVector;
import doa.engine.physics.DoaBodyType;
import doa.engine.physics.DoaPhysics;
import doa.engine.scene.DoaComponent;

public class DoaRigidBody extends DoaComponent {

	private static final long serialVersionUID = -3538700573123115076L;

	/**
	 * Velocity of the body.
	 */
	@NotNull
	public DoaVector velocity = new DoaVector();

	/**
	 * Angular dampening of the body. Default value is 0.01.
	 */
	public float angularDampening = 0.01f;

	/**
	 * Linear dampening of the body. Default value is 0.
	 */
	public float linearDampening = 0f;

	/**
	 * Mass of the body. Default value is 1.
	 */
	public float mass = 1;

	/**
	 * Friction coefficient of the body. Default value is 0.2.
	 */
	public float friction = 0.2f;

	/**
	 * Bounciness of the body. Default value is 0. Recommended value is 0.6 for
	 * bouncy objects.
	 */
	public float elasticity = 0;

	/**
	 * One of the three body types. See {@link DoaBodyType}
	 */
	@NotNull
	public DoaBodyType type = DoaBodyType.DYNAMIC;

	/**
	 * Shape of the body.
	 */
	@NotNull
	public DoaCollider collider = null;

	/**
	 * When true, the body will not rotate.
	 */
	public boolean fixedRotation = false;

	/**
	 * When true, the physics engine will treat this object as a bullet and apply
	 * continuous collision check to it. Prevents ghosting at the cost of
	 * performance.
	 */
	public boolean isBullet = false;

	/**
	 * Set this flag to false if this body should never fall asleep. Note that costs
	 * performance. Sleeping bodies have little CPU overhead.
	 */
	public boolean canSleep = true;

	/**
	 * Handle to Box2D Body.
	 */
	private Body box2DBody;

	public void tick() {
		if (box2DBody == null) { return; }
		getOwner().transform.position.x = box2DBody.getPosition().x * DoaPhysics.PPM;
		getOwner().transform.position.y = box2DBody.getPosition().y * DoaPhysics.PPM;

		getOwner().transform.rotation = DoaMath.toDegress(box2DBody.getAngle());
	}

	@Internal
	public void setNativeBody(Body nativeBody) { box2DBody = nativeBody; }

	@Internal
	public Body getNativeBody() { return box2DBody; }
}
