package doa.engine.scene.elements.renderers;

import static doa.engine.core.DoaGraphicsFunctions.drawImage;

import java.awt.image.BufferedImage;

import javax.validation.constraints.NotNull;

import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaVector;

/**
 * Sprite renderer. Renders sprites created by
 * {@link DoaSprites#createSprite(String, String)} of
 * {@link DoaSprites#createSpriteFromSpriteSheet(String, String, java.awt.Rectangle)}.
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaSpriteRenderer extends DoaRenderer {

	private static final long serialVersionUID = -2050653015243664076L;

	/**
	 * The sprite this renderer will render.
	 */
	protected transient BufferedImage sprite = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

	/**
	 * Offset of the rendered sprite. Offset is calculated from top left of the
	 * transform's position.
	 */
	protected DoaVector offset = new DoaVector(0, 0);

	/**
	 * Dimensions of the rendered sprite, in pixels.
	 */
	protected DoaVector dimensions = new DoaVector(0, 0);

	/**
	 * Sets the sprite to render. Will update the dimensions to the dimensions of
	 * the passed sprite.
	 *
	 * @param sprite new sprite to render
	 */
	public void setSprite(@NotNull BufferedImage sprite) { setSprite(sprite, true); }

	/**
	 * Sets the sprite to render.
	 *
	 * @param sprite new sprite to render
	 * @param useDimensions if true, dimensions are updated to the dimensions of the
	 *        passed sprite
	 */
	public void setSprite(@NotNull BufferedImage sprite, boolean useDimensions) {
		this.sprite = sprite;
		if (useDimensions) {
			dimensions.x = sprite.getWidth();
			dimensions.y = sprite.getHeight();
		}
	}

	public void setOffset(@NotNull DoaVector offset) { this.offset = offset; }

	public void setDimensions(@NotNull DoaVector dimensions) { this.dimensions = dimensions; }

	public BufferedImage getSprite() { return sprite; }

	public DoaVector getOffset() { return offset; }

	public DoaVector getDimensions() { return dimensions; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render() { drawImage(sprite, offset.x, offset.y, dimensions.x, dimensions.y); }
}
