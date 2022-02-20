package doa.engine.scene.elements.renderers;

import static doa.engine.core.DoaGraphicsFunctions.drawPolygon;
import static doa.engine.core.DoaGraphicsFunctions.fillPolygon;
import static doa.engine.core.DoaGraphicsFunctions.popAll;
import static doa.engine.core.DoaGraphicsFunctions.pushAll;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaVector;

/**
 * Arbitrary shape renderer.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaArbitraryShapeRenderer extends DoaRenderer {

	private static final long serialVersionUID = 5015003299720087228L;

	/**
	 * The shape this renderer will render.
	 */
	protected Polygon polygon = new Polygon();

	/**
	 * The color in which the shape will be rendered. Default value is
	 * {@link Color#PINK}.
	 */
	protected Color color = Color.PINK;

	/**
	 * When true, the shape will be filled, when false, the shape will only be
	 * drawn.
	 */
	protected boolean fill = true;

	public void setPoints(@NotNull List<DoaVector> points) {
		polygon = new Polygon();

		for (int i = 0; i < points.size(); i++) {
			polygon.addPoint((int) points.get(i).x, (int) points.get(i).y);
		}
	}

	public void setColor(@NotNull Color color) { this.color = color; }

	public void setFill(boolean fill) { this.fill = fill; }

	public List<DoaVector> getPoints() {
		List<DoaVector> points = new ArrayList<>();
		for (int i = 0; i < polygon.npoints; i++) {
			points.add(new DoaVector(polygon.xpoints[i], polygon.ypoints[i]));
		}
		return points;
	}

	public Color getColor() { return color; }

	public boolean getFill() { return fill; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render() {
		pushAll();
		DoaGraphicsFunctions.setColor(color);
		if (fill) {
			fillPolygon(polygon);
		} else {
			drawPolygon(polygon);
		}
		popAll();
	}
}
