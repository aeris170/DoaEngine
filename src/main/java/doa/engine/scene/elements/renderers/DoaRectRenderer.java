package doa.engine.scene.elements.renderers;

import static doa.engine.core.DoaGraphicsFunctions.draw;
import static doa.engine.core.DoaGraphicsFunctions.fill;
import static doa.engine.core.DoaGraphicsFunctions.popAll;
import static doa.engine.core.DoaGraphicsFunctions.pushAll;

import java.awt.Color;
import java.awt.Rectangle;

import javax.validation.constraints.NotNull;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.maths.DoaVector;

/**
 * Rectangle shape renderer.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaRectRenderer extends DoaRenderer {

	private static final long serialVersionUID = 3925339975462901087L;

	/**
	 * The rectangle this renderer will render.
	 */
	protected Rectangle rect = new Rectangle();

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

	/**
	 * Sets the offset of the rectangle. The offset is calculated from the owner of
	 * this renderer's transform's position.
	 * 
	 * @param offset offset of this renderer
	 */
	public void setOffset(@NotNull DoaVector offset) {
		rect.x = (int) (offset.x - rect.width / 2);
		rect.y = (int) (offset.y - rect.height / 2);
	}

	/**
	 * Sets the dimensions of this rectangle.
	 * 
	 * @param dimensions dimensions of rectangle
	 */
	public void setDimensions(@NotNull DoaVector dimensions) {
		rect.width = (int) (dimensions.x);
		rect.height = (int) (dimensions.y);
		rect.x = rect.x - rect.width / 2;
		rect.y = rect.y - rect.height / 2;
	}

	public void setColor(@NotNull Color color) { this.color = color; }

	public void setFill(boolean fill) { this.fill = fill; }

	public DoaVector getOffset() { return new DoaVector((float) rect.getCenterX(), (float) rect.getCenterY()); }

	public DoaVector getDimensions() { return new DoaVector(rect.width, rect.height); }

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
			fill(rect);
		} else {
			draw(rect);
		}
		popAll();
	}
}
