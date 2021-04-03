package doa.engine.scene.elements.physics;

import java.util.List;

import doa.engine.maths.DoaVector;

/**
 * A collider of arbitrary convex shape.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaArbitraryShapeCollider extends DoaCollider {

	private static final long serialVersionUID = 7937279848107433232L;

	/**
	 * List of points from which the shape of this component will be derived.
	 */
	protected List<DoaVector> points;

	/**
	 * Constructor. Colinear points are allowed but may lead poor performance. The
	 * physics engine will produce a convex hull out of the points. Please see
	 * {@link DoaCollider#getPoints()} for more details.
	 * 
	 * @param points the list of points of this collider
	 */
	public DoaArbitraryShapeCollider(List<DoaVector> points) { this.points = points; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DoaVector> getPoints() { return points; }

}
