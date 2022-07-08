package doa.engine.scene.elements.physics;

import static doa.engine.core.DoaGraphicsFunctions.drawPolygon;
import static doa.engine.core.DoaGraphicsFunctions.drawString;
import static doa.engine.core.DoaGraphicsFunctions.setColor;
import static doa.engine.core.DoaGraphicsFunctions.setFont;

import java.awt.Color;
import java.awt.Font;
import java.awt.Polygon;
import java.io.Serializable;
import java.util.List;
import java.util.ListIterator;

import com.google.errorprone.annotations.ForOverride;

import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaComponent;
import doa.engine.scene.DoaObject;

/**
 * Blueprint of a physics collider.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public abstract class DoaCollider implements Serializable {

	private static final long serialVersionUID = 8368862995317149654L;

	protected boolean isTrigger = false;
	protected int group = 0;
	DoaBodyType type;

	/**
	 * Returns the list of points the physics engine will use to form the shape of a
	 * rigidbody. The returned list of points will be used to create a convex hull.
	 * Colinear points are allowed but discouraged since they lead to poor stacking
	 * behavior.
	 * 
	 * @return the points of the convex hull of the rigidbody
	 */
	public abstract List<DoaVector> getPoints();
	
	/**
	 * Returns an iterator of the list of points returned by 
	 * {@link DoaCollider#getPoints()}.
	 * 
	 * @return the points of the convex hull of the rigidbody
	 */
	public ListIterator<DoaVector> getPointsIterator() { return getPoints().listIterator(); }

	/**
	 * Makes the DoaCollider a trigger. Triggers do not participate in collision
	 * detection, they don't collide, they only store the collision info. It is the
	 * programmer's duty to handle what to do with that info by overriding
	 * {@link DoaCollider#onTriggerEnter(DoaObject, DoaObject)} and
	 * {@link DoaCollider#onTriggerExit(DoaObject, DoaObject)}
	 * 
	 * @return this
	 */
	public DoaCollider makeTrigger() {
		isTrigger = true;
		return this;
	}

	/**
	 * Makes the DoaCollider a non-trigger.
	 * 
	 * @return this
	 */
	public DoaCollider makeNonTrigger() {
		isTrigger = false;
		return this;
	}

	/**
	 * Returns whether this collider is a trigger or not. Triggers do not
	 * participate in collision detection, they don't collide, they only store the
	 * collision info. It is the programmer's duty to handle what to do with that
	 * info by overriding {@link DoaCollider#onTriggerEnter(DoaObject, DoaObject)}
	 * and {@link DoaCollider#onTriggerExit(DoaObject, DoaObject)}
	 * 
	 * @return isTrigger
	 */
	public boolean isTrigger() { return isTrigger; }

	/**
	 * Sets the group index of the DoaCollider. Collision groups allow a certain
	 * group of objects to never collide (negative)or always collide (positive).
	 * Zero means no collision group.
	 * 
	 * @param groupIndex group index of the collider.
	 * @return this
	 */
	public DoaCollider group(final int groupIndex) {
		group = groupIndex;
		return this;
	}

	/**
	 * Returns the group index of the DoaCollider. Collision groups allow a certain
	 * group of objects to never collide (negative)or always collide (positive).
	 * Zero means no collision group.
	 * 
	 * @return group index of the collider.
	 */
	public int getGroup() { return group; }

	/**
	 * Called when a DoaObject's rigidbody with a non-trigger collider enters the
	 * boundary of this collider. This collider must be a trigger for this method to
	 * fire.
	 * 
	 * @param entered object which the trigger is attached
	 * @param enterer object which entered the trigger
	 */
	@ForOverride
	public void onTriggerEnter(DoaObject entered, DoaObject enterer) {}

	/**
	 * Called when a DoaObject's rigidbody with a non-trigger collider exits the
	 * boundary of this collider. This collider must be a trigger for this method to
	 * fire.
	 * 
	 * @param exited object which the trigger is attached
	 * @param exiter object which exited the trigger
	 */
	@ForOverride
	public void onTriggerExit(DoaObject exited, DoaObject exiter) {}

	/**
	 * <strong>Do not call this method, merely override it.</strong> Called when
	 * {@link DoaComponent#enableDebugRender} is set to true.
	 */
	@ForOverride
	public void debugRender() {
		if (isTrigger()) {
			setColor(Color.CYAN);
		} else {
			switch (type) {
			case STATIC:
				setColor(Color.RED);
				break;
			case DYNAMIC:
				setColor(Color.GREEN);
				break;
			case KINEMATIC:
				setColor(Color.BLUE);
				break;
			default:
				setColor(Color.PINK);
				break;
			}
		}
		List<DoaVector> points = getPoints();
		if (points == null) {
			setFont(new Font("Helvetica", Font.BOLD, 16));
			drawString("getPoints() == null, please override DoaCollider#debugRender!", 0, 0);
			return;
		}
		int[] xp = new int[points.size()];
		int[] yp = new int[points.size()];
		for (int i = 0; i < points.size(); i++) {
			xp[i] = (int) points.get(i).x;
			yp[i] = (int) points.get(i).y;
		}
		drawPolygon(new Polygon(xp, yp, points.size()));
	}
}
