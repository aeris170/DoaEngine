package doa.engine.scene.elements.physics;

import java.util.List;

import doa.engine.maths.DoaVector;

/**
 * A collider shaped as a circle.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaCircleCollider implements DoaCollider {

	private static final long serialVersionUID = -5841632222316281146L;

	private float radius = 0;

	/**
	 * Constructor.
	 * 
	 * @param radius radius of the collider
	 */
	public DoaCircleCollider(float radius) { this.radius = radius; }

	public float getRadius() { return radius; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DoaVector> getPoints() { return null; }

}
