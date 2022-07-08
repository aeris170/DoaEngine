package doa.engine.scene.elements.physics;

import static doa.engine.core.DoaGraphicsFunctions.drawOval;
import static doa.engine.core.DoaGraphicsFunctions.setColor;

import java.awt.Color;
import java.util.List;

import doa.engine.maths.DoaVector;

/**
 * A collider shaped as a circle.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaCircleCollider extends DoaCollider {

	private static final long serialVersionUID = -5841632222316281146L;

	private float radius = 0;
	private DoaVector offset;

	/**
	 * Constructor.
	 * 
	 * @param radius radius of the collider
	 */
	public DoaCircleCollider(float radius) { this(radius, new DoaVector()); }

	/**
	 * Constructor.
	 * 
	 * @param radius radius of the collider
	 * @param offset offset of the collider
	 */
	public DoaCircleCollider(float radius, DoaVector offset) {
		this.radius = radius;
		this.offset = offset;
	}

	public float getRadius() { return radius; }

	public DoaVector getOffset() { return offset; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DoaVector> getPoints() { return List.of(); }

	/**
	 * Renders the collider. For debug only.
	 */
	@Override
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
		drawOval(offset.x - radius, offset.y - radius, radius * 2, radius * 2);
	}
}
