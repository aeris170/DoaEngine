package doa.engine.scene.elements.physics;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import doa.engine.Internal;
import doa.engine.maths.DoaMath;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaComponent;
import doa.engine.scene.DoaObject;
import doa.engine.scene.DoaPhysics;
import doa.engine.scene.elements.DoaTransform;

/**
 * A body of simulated physics.
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaRigidBody extends DoaComponent {

	private static final long serialVersionUID = -3538700573123115076L;

	/**
	 * Starting linear velocity of the body. Default value is (0, 0).
	 */
	@NotNull
	public DoaVector linearVelocity = new DoaVector();

	/**
	 * Starting angular velocity of the body. Default value is 0.
	 */
	public float angularVelocity = 0;

	/**
	 * Linear dampening of the body. Default value is 0.
	 */
	public float linearDampening = 0f;

	/**
	 * Angular dampening of the body. Default value is 0.01.
	 */
	public float angularDampening = 0.01f;

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
	public final List<DoaCollider> colliders = new ArrayList<>();

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
	private transient Body box2DBody;

	/**
	 * Set the linear velocity of the center of mass.
	 *
	 * @param v the new linear velocity of the center of mass.
	 */
	public void setLinearVelocity(@NotNull final DoaVector v) { box2DBody.setLinearVelocity(new Vec2(v.x, v.y)); }

	/**
	 * Returns copy the linear velocity of the center of mass.
	 *
	 * @return copy of the linear velocity of the center of mass.
	 */
	public DoaVector getLinearVelocity() {
		Vec2 v = box2DBody.getLinearVelocity();
		return new DoaVector(v.x, v.y);
	}

	/**
	 * Set the angular velocity.
	 *
	 * @param w the new angular velocity in degrees/second.
	 */
	public void setAngularVelocity(final float w) { box2DBody.setAngularVelocity(DoaMath.toRadians(w)); }

	/**
	 * Returns the angular velocity.
	 *
	 * @return the angular velocity in degress/second.
	 */
	public float getAngularVelocity() { return box2DBody.getAngularVelocity(); }

	/**
	 * Set the transform. Calling this method is ill-advised, all physics motion
	 * <i>should</i> be left to the physics engine. This method is here for
	 * compatibility reasons.
	 *
	 * @param transform transform to override with
	 */
	public void setTransform(@NotNull final DoaTransform transform) {
		box2DBody.setTransform(new Vec2(transform.position.x / DoaPhysics.PPM, transform.position.y / DoaPhysics.PPM), DoaMath.toRadians(transform.rotation));
	}

	/**
	 * Returns the transform.
	 *
	 * @return transform
	 */
	public DoaTransform getTransform() {
		DoaTransform t = new DoaTransform();
		t.position.x = box2DBody.getPosition().x * DoaPhysics.PPM;
		t.position.y = box2DBody.getPosition().y * DoaPhysics.PPM;
		t.rotation = DoaMath.toDegress(box2DBody.getAngle());
		return t;
	}

	/**
	 * Apply a force at a world point. If the force is not applied at the center of
	 * mass, it will generate a torque and affect the angular velocity. This wakes
	 * up the body.
	 *
	 * @param force the world force vector, usually in Newtons (N).
	 * @param point the world position of the point of application.
	 */
	public void applyForce(@NotNull final DoaVector force, @NotNull final DoaVector point) {
		box2DBody.applyForce(new Vec2(force.x, force.y), new Vec2(point.x, point.y));
	}

	/**
	 * Apply a force to the center of mass. This wakes up the body.
	 *
	 * @param force the world force vector, usually in Newtons (N).
	 */
	public void applyForceToCenter(@NotNull final DoaVector force) { box2DBody.applyForceToCenter(new Vec2(force.x, force.y)); }

	/**
	 * Apply a torque. This affects the angular velocity without affecting the
	 * linear velocity of the center of mass. This wakes up the body.
	 *
	 * @param torque about the z-axis (out of the screen), usually in N-m.
	 */
	public void applyTorque(final float torque) { box2DBody.applyTorque(torque); }

	/**
	 * Apply an impulse at a point. This immediately modifies the velocity. It also
	 * modifies the angular velocity if the point of application is not at the
	 * center of mass. This wakes up the body.
	 *
	 * @param impulse the world impulse vector, usually in N-seconds or kg-m/s.
	 * @param point the world position of the point of application.
	 */
	public void applyLinearImpulse(@NotNull final DoaVector impulse, @NotNull final DoaVector point) {
		box2DBody.applyLinearImpulse(new Vec2(impulse.x, impulse.y), new Vec2(point.x, point.y));
	}

	/**
	 * Apply an angular impulse.
	 *
	 * @param impulse the angular impulse in units of kg*m*m/s
	 */
	public void applyAngularImpulse(final float impulse) { box2DBody.applyAngularImpulse(impulse); }

	public void tick() {
		if (box2DBody == null) { return; }
		getOwner().transform.position.x = box2DBody.getPosition().x * DoaPhysics.PPM;
		getOwner().transform.position.y = box2DBody.getPosition().y * DoaPhysics.PPM;
		// TODO???
		/*
		 * if (collider instanceof DoaCircleCollider circleCollider) {
		 * getOwner().transform.position.x -= circleCollider.getOffset().x; // why no
		 * PPM?? getOwner().transform.position.y -= circleCollider.getOffset().y; }
		 */
		getOwner().transform.rotation = DoaMath.toDegress(box2DBody.getAngle());
	}

	@Override
	protected void onAdd(@NotNull final DoaObject o) {
		colliders.forEach(col -> col.type = type);
		if (o.getScene() != null) {
			o.getScene().registerBody(this);
		}
	}

	@Override
	protected void onRemove(@NotNull final DoaObject o) {
		if (o.getScene() != null) {
			o.getScene().deleteBody(this);
		}
	}

	/**
	 * @hidden
	 */
	@Internal
	public void setNativeBody(final Body nativeBody) { box2DBody = nativeBody; }

	/**
	 * Normally, programmers shouldn't need to call this method. This method is on
	 * the grey when it comes to the {@link Internal} annotation. Call, if you know
	 * what you are doing.
	 *
	 * @return the handle to the native-Box2D Body
	 */
	@Internal
	public Body getNativeBody() { return box2DBody; }
}
