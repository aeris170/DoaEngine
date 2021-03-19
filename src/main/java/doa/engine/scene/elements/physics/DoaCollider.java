package doa.engine.scene.elements.physics;

import java.io.Serializable;
import java.util.List;

import doa.engine.maths.DoaVector;

/**
 * Contract of a physics collider.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public interface DoaCollider extends Serializable {

	/**
	 * Returns the list of points the physics engine will use to form the shape of a
	 * rigidbody. The returned list of points will be used to create a convex hull.
	 * Colinear points are allowed but discouraged since they lead to poor stacking
	 * behavior.
	 * 
	 * @return the points of the convex hull of the rigidbody
	 */
	List<DoaVector> getPoints();
}
