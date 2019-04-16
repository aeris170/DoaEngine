package com.doa.ui;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.NotSerializableException;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.maths.DoaVectorF;

/**
 * {@code DoaUIComponent} is the root of the UI component hierarchy. Every UI
 * component have {@code DoaUIComponent} as a superclass. All UI components
 * implement the methods of this class. When a UI component gets mutated(have
 * its size, position, velocity changed), its bounds are re-calculated
 * automatically.
 * 
 * @author Doga Oruc
 * @since DoaEngine 2.3
 * @version 2.3.2
 */
public abstract class DoaUIComponent extends DoaObject {

	private static final long serialVersionUID = -6344812227089227175L;

	/**
	 * Whether or not this component will be rendered.
	 */
	protected boolean isVisible;

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
	public DoaUIComponent(Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height, 999);
		recalibrateBounds();
	}

	/**
	 * Instantiates a UI component with the specified bounds
	 * 
	 * @param position position of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 */
	public DoaUIComponent(DoaVectorF position, Integer width, Integer height) {
		super(position, width, height, 999);
		recalibrateBounds();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tick() {
		recalibrateBounds();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(DoaGraphicsContext g) {
		if (!isVisible) {
			return;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle2D getBounds() {
		return bounds;
	}

	/**
	 * Sets the parent of this component.
	 * 
	 * @param parent the container which will hold this component
	 */
	public void setParent(DoaUIContainer parent) {
		if (parent != null) {
			parent.remove(this);
		}
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
	 * Return whether this component is visible or not.
	 * 
	 * @return true if and only if, the component will be ticked and rendered on the
	 *         next frame
	 */
	public boolean isVisible() {
		return isVisible;
	}

	private void recalibrateBounds() {
		bounds = new Rectangle2D.Float(position.x, position.y, width, height);
	}

	@SuppressWarnings({ "static-method", "unused" })
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
		throw new NotSerializableException("DoaUIComponent Serialization Disallowed");
	}

	@SuppressWarnings({ "static-method", "unused" })
	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		throw new NotSerializableException("DoaUIComponent Serialization Disallowed");
	}
}