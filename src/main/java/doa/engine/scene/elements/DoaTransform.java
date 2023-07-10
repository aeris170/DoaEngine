package doa.engine.scene.elements;

import java.awt.Polygon;
import java.awt.Shape;

import javax.validation.constraints.NotNull;

import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaComponent;

/**
 * Transform component.
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public final class DoaTransform extends DoaComponent implements DoaEssentialComponent {

	private static final long serialVersionUID = -6234469128708802081L;

	/**
	 * Offset from top left corner of the window.
	 */
	@NotNull
	public DoaVector position = new DoaVector(0, 0);

	/**
	 * In degrees.
	 */
	public float rotation = 0;

	@NotNull
	public DoaVector scale = new DoaVector(1, 1);

	/**
	 * Calculates and returns the smallest axis-aligned bounding box of the
	 * transform. This operation is costly.
	 *
	 * @return the smallest axis-aligned bounding box of this transform
	 */
	public Shape AABB() { return getPreciseBounds().getBounds(); }

	/**
	 * Calculates and returns the quad completely subsuming this transform. The
	 * returned shape is the shape with the smallest area.
	 *
	 * @return bounds of this transform
	 */
	public Shape getPreciseBounds() {
		var tl = new DoaVector(-0.5f * scale.x, 0.5f * scale.y);
		var tr = new DoaVector(0.5f * scale.x, 0.5f * scale.y);
		var bl = new DoaVector(-0.5f * scale.x, -0.5f * scale.y);
		var br = new DoaVector(0.5f * scale.x, -0.5f * scale.y);

		DoaVector.rotateAroundOrigin(tl, rotation % 90, tl);
		DoaVector.rotateAroundOrigin(tr, rotation % 90, tr);
		DoaVector.rotateAroundOrigin(bl, rotation % 90, bl);
		DoaVector.rotateAroundOrigin(br, rotation % 90, br);

		DoaVector.translate(tl, position.x, position.y, tl);
		DoaVector.translate(tr, position.x, position.y, tr);
		DoaVector.translate(bl, position.x, position.y, bl);
		DoaVector.translate(br, position.x, position.y, br);

		return new Polygon(new int[] { (int) tl.x, (int) tr.x, (int) br.x, (int) bl.x }, new int[] { (int) tl.y, (int) tr.y, (int) br.y, (int) bl.y }, 4);
	}
}
