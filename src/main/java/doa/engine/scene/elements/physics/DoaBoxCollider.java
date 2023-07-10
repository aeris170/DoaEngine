package doa.engine.scene.elements.physics;

import static doa.engine.core.DoaGraphicsFunctions.drawRect;
import static doa.engine.core.DoaGraphicsFunctions.setColor;

import java.awt.Color;
import java.util.List;

import doa.engine.maths.DoaVector;

/**
 * A collider shaped as a rectangle.
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaBoxCollider extends DoaCollider {

	private static final long serialVersionUID = 3596967320371375193L;

	private DoaVector dimensions;
	private DoaVector offset;

	/**
	 * Constructor.
	 *
	 * @param dimensions dimensions of the collider
	 */
	public DoaBoxCollider(final DoaVector dimensions) { this(dimensions, new DoaVector()); }

	/**
	 * Constructor.
	 *
	 * @param dimensions dimensions of the collider
	 * @param offset offset of the collider
	 */
	public DoaBoxCollider(final DoaVector dimensions, final DoaVector offset) {
		this.dimensions = dimensions;
		this.offset = offset;
	}

	public DoaVector getDimensions() { return dimensions; }

	public DoaVector getOffset() { return offset; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DoaVector> getPoints() { return null; }

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
		drawRect(offset.x - dimensions.x / 2, offset.y - dimensions.y / 2, dimensions.x, dimensions.y);
	}
}
