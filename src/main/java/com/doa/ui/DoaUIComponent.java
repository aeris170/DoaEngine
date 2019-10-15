package com.doa.ui;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.NotSerializableException;

import javax.validation.constraints.NotNull;

import com.doa.engine.scene.DoaObject;
import com.doa.maths.DoaVectorF;

/**
 * {@code DoaUIComponent} is the root of the UI component hierarchy. Every UI component have
 * {@code DoaUIComponent} as a superclass. All UI components implement the methods of this class.
 * When a UI component gets mutated(have its size, position, velocity changed), its bounds are
 * re-calculated automatically.
 *
 * @author Doga Oruc
 * @since DoaEngine 2.3
 * @version 2.7
 */
public abstract class DoaUIComponent extends DoaObject {

	private static final long serialVersionUID = -6344812227089227175L;

	/**
	 * Whether or not this component will be rendered.
	 */
	protected boolean isVisible = true;

	/**
	 * Whether or not this component is interactive.
	 */
	protected boolean isEnabled = true;

	private transient Rectangle2D bounds;
	private DoaUIContainer parent;

	/**
	 * Instantiates a UI component with the specified bounds
	 *
	 * @param x x coordinate of the top left corner of the UI component
	 * @param y y coordinate of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 */
	public DoaUIComponent(final float x, final float y, final int width, final int height) {
		super(x, y, width, height);
		recalibrateBounds();
	}

	/**
	 * Instantiates a UI component with the specified bounds
	 *
	 * @param position position of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 */
	public DoaUIComponent(@NotNull final DoaVectorF position, final int width, final int height) {
		super(position, width, height);
		recalibrateBounds();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle2D getBounds() {
		return bounds;
	}

	void setParent(@NotNull final DoaUIContainer parent) {
		this.parent = parent;
	}

	/**
	 * Returns the container that holds this component.
	 *
	 * @return parent of this component, or null if parent == null;
	 */
	public DoaUIContainer getParent() {
		return parent;
	}

	/**
	 * Shows this component.
	 */
	public void show() {
		isVisible = true;
	}

	/**
	 * Hides this component.
	 */
	public void hide() {
		isVisible = false;
	}

	/**
	 * Enables interaction with this component.
	 */
	public void enable() {
		isEnabled = true;
	}

	/**
	 * Disables interaction with this component.
	 */
	public void disable() {
		isEnabled = false;
	}

	/**
	 * Return whether this component is visible or not.
	 *
	 * @return true if and only if, the component will be ticked and rendered on the next frame
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Return whether this component is enabled or not.
	 *
	 * @return true if and only if, the component will be able to react to and absorb mouse events.
	 */
	public boolean isEnabled() {
		return isEnabled;
	}

	public void recalibrateBounds() {
		bounds = new Rectangle2D.Float(position.x, position.y, width, height);
	}

	@SuppressWarnings({ "static-method", "unused" })
	private void writeObject(final java.io.ObjectOutputStream stream) throws IOException {
		throw new NotSerializableException("DoaUIComponent Serialization Disallowed");
	}

	@SuppressWarnings({ "static-method", "unused" })
	private void readObject(final java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		throw new NotSerializableException("DoaUIComponent Serialization Disallowed");
	}
}