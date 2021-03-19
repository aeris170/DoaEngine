package doa.engine.physics;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import doa.engine.Internal;
import doa.engine.log.DoaLogger;
import doa.engine.maths.DoaMath;
import doa.engine.maths.DoaVector;
import doa.engine.scene.elements.DoaTransform;
import doa.engine.scene.elements.physics.DoaBoxCollider;
import doa.engine.scene.elements.physics.DoaCircleCollider;
import doa.engine.scene.elements.physics.DoaCollider;
import doa.engine.scene.elements.physics.DoaRigidBody;

/**
 * Physics engine. Config only, never call the methods of this class!
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public final class DoaPhysics {

	/**
	 * Global gravity vector. Default value is 0 vector.
	 */
	@NotNull
	public static DoaVector Gravity = new DoaVector();

	/**
	 * Pixel per meter factor. Default value is 64ppm. 64 pixels = 1 meter.
	 */
	public static float PPM = 64;

	/**
	 * Number of iterations in the velocity constraint solver. Default value is 8.
	 * Must be bigger than 0. Higher values allow more precise simulations at the
	 * cost of performance.
	 */
	public static int VelocityIterations = 8;

	/**
	 * Number of iterations in the position constraint solver. Default value is 3.
	 * Must be bigger than 0. Higher values allow more precise simulations at the
	 * cost of performance.
	 */
	public static int PositionIterations = 3;

	private static World box2DWorld = new World(new Vec2(0, 0));

	private DoaPhysics() {}

	@Internal
	public static void registerBody(@NotNull final DoaRigidBody rigidBody) {
		if (rigidBody.getNativeBody() == null) {
			DoaTransform t = rigidBody.getOwner().transform;

			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(t.position.x / PPM, t.position.y / PPM);
			bodyDef.angle = DoaMath.toRadians(t.rotation);
			bodyDef.angularDamping = rigidBody.angularDampening;
			bodyDef.linearDamping = rigidBody.linearDampening;
			bodyDef.fixedRotation = rigidBody.fixedRotation;
			bodyDef.bullet = rigidBody.isBullet;
			bodyDef.allowSleep = rigidBody.canSleep;

			switch (rigidBody.type) {
			case STATIC:
				bodyDef.type = BodyType.STATIC;
				break;
			case DYNAMIC:
				bodyDef.type = BodyType.DYNAMIC;
				break;
			case KINEMATIC:
				bodyDef.type = BodyType.KINEMATIC;
				break;
			default:
				throw new IllegalArgumentException("??");
			}

			Shape shape = new PolygonShape();

			DoaCollider collider = rigidBody.collider;
			if (collider != null) {
				List<DoaVector> points = collider.getPoints();
				if (points != null) {
					Vec2[] b2Points = new Vec2[points.size()];
					for (int i = 0; i < points.size(); i++) {
						b2Points[i] = new Vec2(points.get(i).x / PPM, points.get(i).y / PPM);
					}
					((PolygonShape) shape).set(b2Points, points.size());
				} else if (rigidBody.collider instanceof DoaCircleCollider circleCollider) {
					shape = new CircleShape();
					shape.setRadius(circleCollider.getRadius() / PPM);
				} else if (rigidBody.collider instanceof DoaBoxCollider boxCollider) {
					((PolygonShape) shape).setAsBox(boxCollider.getDimensions().x / PPM / 2,
					        boxCollider.getDimensions().y / PPM / 2,
					        new Vec2(boxCollider.getOffset().x / PPM, boxCollider.getOffset().y / PPM),
					        0);
				} else {
					DoaLogger.LOGGER.warning(new StringBuilder(128).append("Collider of rigidbody of ").append(rigidBody.getOwner().name).append(
					        " has returned no points. Please correctly implement DoaCollider#getPoints(void)!"));
				}
			} else {
				DoaLogger.LOGGER.warning(new StringBuilder(128).append("Collider of rigidbody of ").append(rigidBody.getOwner().name).append(
				        " is null. Please set a collider before adding the rigidbody to the DoaObject!"));
			}

			Body body = box2DWorld.createBody(bodyDef);

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = shape;
			fixtureDef.density = rigidBody.mass;
			fixtureDef.friction = rigidBody.friction;
			fixtureDef.restitution = rigidBody.elasticity;

			body.createFixture(fixtureDef);

			rigidBody.setNativeBody(body);
		} else {
			DoaLogger.LOGGER.warning(
			        new StringBuilder(128).append("Rigidbody of ").append(rigidBody.getOwner().name).append(" was already registered to the physics engine."));
		}
	}

	@Internal
	public static void deleteBody(@NotNull final DoaRigidBody rigidBody) { box2DWorld.destroyBody(rigidBody.getNativeBody()); }

	@Internal
	public static void tick(final int ticks) {
		box2DWorld.setGravity(new Vec2(Gravity.x, Gravity.y));
		box2DWorld.step(1.f / ticks, VelocityIterations, PositionIterations);
	}

}
