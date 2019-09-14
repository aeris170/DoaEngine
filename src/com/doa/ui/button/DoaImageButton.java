package com.doa.ui.button;

import java.awt.image.BufferedImage;

import javax.validation.constraints.NotNull;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.maths.DoaVectorF;

/**
 * A button that can render {@code DoaSprite}s onto itself. When hovered over, renders
 * {@code hoverImage} onto itself. When clicked, renders {@code clickImage} onto itself. Else,
 * renders {@code idleImage}.
 *
 * @author Doga Oruc
 * @since DoaEngine 2.3
 * @version 2.7
 */
public class DoaImageButton extends DoaButton {

	private static final long serialVersionUID = 4793919120591968813L;

	private transient BufferedImage idleImage;
	private transient BufferedImage hoverImage;
	private transient BufferedImage clickImage;
	private transient BufferedImage inactiveImage;

	/**
	 * Instantiates a button with the bounds and action.
	 *
	 * @param x x coordinate of the top left corner of the UI component
	 * @param y y coordinate of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 * @param idleImage sprite that will be rendered onto this button when the button is idle, cannot be
	 *        null
	 */
	public DoaImageButton(final float x, final float y, final int width, final int height, @NotNull final BufferedImage idleImage) {
		super(x, y, width, height);
		this.idleImage = idleImage;
		hoverImage = idleImage;
		clickImage = idleImage;
		inactiveImage = idleImage;
	}

	/**
	 * Instantiates a button with the bounds and action.
	 *
	 * @param position position of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 * @param idleImage sprite that will be rendered onto this button when the button is idle, cannot be
	 *        null
	 */
	public DoaImageButton(final DoaVectorF position, final int width, final int height, @NotNull final BufferedImage idleImage) {
		super(position, width, height);
		this.idleImage = idleImage;
		hoverImage = idleImage;
		clickImage = idleImage;
		inactiveImage = idleImage;
	}

	/**
	 * Instantiates a button with the bounds and action.
	 *
	 * @param x x coordinate of the top left corner of the UI component
	 * @param y y coordinate of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 * @param idleImage sprite that will be rendered onto this button when the button is idle, cannot be
	 *        null
	 * @param hoverImage sprite that will be rendered onto this button when the mouse pointer hovers
	 *        over the button, cannot be null
	 */
	public DoaImageButton(final float x, final float y, final int width, final int height, @NotNull final BufferedImage idleImage, @NotNull final BufferedImage hoverImage) {
		super(x, y, width, height);
		this.idleImage = idleImage;
		this.hoverImage = hoverImage;
		clickImage = hoverImage;
		inactiveImage = hoverImage;
	}

	/**
	 * Instantiates a button with the bounds and action.
	 *
	 * @param position position of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 * @param idleImage sprite that will be rendered onto this button when the button is idle, cannot be
	 *        null
	 * @param hoverImage sprite that will be rendered onto this button when the mouse pointer hovers
	 *        over the button, cannot be null
	 */
	public DoaImageButton(final DoaVectorF position, final int width, final int height, @NotNull final BufferedImage idleImage, @NotNull final BufferedImage hoverImage) {
		super(position, width, height);
		this.idleImage = idleImage;
		this.hoverImage = hoverImage;
		clickImage = hoverImage;
		inactiveImage = hoverImage;
	}

	/**
	 * Instantiates a button with the bounds and action.
	 *
	 * @param x x coordinate of the top left corner of the UI component
	 * @param y y coordinate of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 * @param idleImage sprite that will be rendered onto this button when the button is idle, cannot be
	 *        null
	 * @param hoverImage sprite that will be rendered onto this button when the mouse pointer hovers
	 *        over the button, cannot be null
	 * @param clickImage sprite that will be rendered onto this button when the mouse clicks the button,
	 *        cannot be null
	 */
	public DoaImageButton(final float x, final float y, final int width, final int height, @NotNull final BufferedImage idleImage, @NotNull final BufferedImage hoverImage,
	        @NotNull final BufferedImage clickImage) {
		super(x, y, width, height);
		this.idleImage = idleImage;
		this.hoverImage = hoverImage;
		this.clickImage = clickImage;
		inactiveImage = clickImage;
	}

	/**
	 * Instantiates a button with the bounds and action.
	 *
	 * @param position position of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 * @param idleImage sprite that will be rendered onto this button when the button is idle, cannot be
	 *        null
	 * @param hoverImage sprite that will be rendered onto this button when the mouse pointer hovers
	 *        over the button, cannot be null
	 * @param clickImage sprite that will be rendered onto this button when the mouse clicks the button,
	 *        cannot be null
	 */
	public DoaImageButton(final DoaVectorF position, final int width, final int height, @NotNull final BufferedImage idleImage, @NotNull final BufferedImage hoverImage,
	        @NotNull final BufferedImage clickImage) {
		super(position, width, height);
		this.idleImage = idleImage;
		this.hoverImage = hoverImage;
		this.clickImage = clickImage;
		inactiveImage = clickImage;
	}

	/**
	 * Instantiates a button with the bounds and action.
	 *
	 * @param x x coordinate of the top left corner of the UI component
	 * @param y y coordinate of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 * @param idleImage sprite that will be rendered onto this button when the button is idle, cannot be
	 *        null
	 * @param hoverImage sprite that will be rendered onto this button when the mouse pointer hovers
	 *        over the button, cannot be null
	 * @param clickImage sprite that will be rendered onto this button when the mouse clicks the button,
	 *        cannot be null
	 * @param inactiveImage sprite that will be rendered onto this button when the button is disabled
	 */
	public DoaImageButton(final float x, final float y, final int width, final int height, @NotNull final BufferedImage idleImage, @NotNull final BufferedImage hoverImage,
	        @NotNull final BufferedImage clickImage, @NotNull final BufferedImage inactiveImage) {
		super(x, y, width, height);
		this.idleImage = idleImage;
		this.hoverImage = hoverImage;
		this.clickImage = clickImage;
		this.inactiveImage = inactiveImage;
	}

	/**
	 * Instantiates a button with the bounds and action.
	 *
	 * @param position position of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 * @param idleImage sprite that will be rendered onto this button when the button is idle, cannot be
	 *        null
	 * @param hoverImage sprite that will be rendered onto this button when the mouse pointer hovers
	 *        over the button, cannot be null
	 * @param clickImage sprite that will be rendered onto this button when the mouse clicks the button,
	 *        cannot be null
	 * @param inactiveImage sprite that will be rendered onto this button when the button is disabled
	 */
	public DoaImageButton(final DoaVectorF position, final int width, final int height, @NotNull final BufferedImage idleImage, @NotNull final BufferedImage hoverImage,
	        @NotNull final BufferedImage clickImage, @NotNull final BufferedImage inactiveImage) {
		super(position, width, height);
		this.idleImage = idleImage;
		this.hoverImage = hoverImage;
		this.clickImage = clickImage;
		this.inactiveImage = inactiveImage;
	}

	@Override
	public void render(final DoaGraphicsContext g) {
		if (isEnabled) {
			if (click) {
				g.drawImage(clickImage, position.x, position.y, width, height);
			} else if (hover) {
				g.drawImage(hoverImage, position.x, position.y, width, height);
			} else {
				g.drawImage(idleImage, position.x, position.y, width, height);
			}
		} else {
			g.drawImage(inactiveImage, position.x, position.y, width, height);
		}
	}
}