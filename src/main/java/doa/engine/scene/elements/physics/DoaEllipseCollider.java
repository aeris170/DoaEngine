package doa.engine.scene.elements.physics;

import java.util.ArrayList;

import doa.engine.maths.DoaVector;

/**
 * An approximate ellipse collider.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaEllipseCollider extends DoaArbitraryShapeCollider {

	private static final long serialVersionUID = -1770759928969629062L;

	protected float majorAxis;
	protected float minorAxis;

	/**
	 * Constructor
	 * 
	 * @param majorAxis
	 * @param minorAxis
	 */
	public DoaEllipseCollider(float majorAxis, float minorAxis) { this(majorAxis, minorAxis, new DoaVector()); }

	/**
	 * Constructor
	 * 
	 * @param majorAxis
	 * @param minorAxis
	 */
	public DoaEllipseCollider(float majorAxis, float minorAxis, DoaVector offset) {
		super(new ArrayList<>(8));
		float t = 0;
		for (float i = 0; i < 8; i++) {
			t += Math.PI / 4;
			points.add(new DoaVector(majorAxis * (float) Math.cos(t) + offset.x, minorAxis * (float) Math.sin(t) + offset.y));
		}
	}
}
