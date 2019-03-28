package com.doa.ui;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.NotSerializableException;

import com.doa.engine.DoaObject;
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
 * @version 2.3
 */
public abstract class DoaUIComponent extends DoaObject implements DoaUIComponentI {

	private static final long serialVersionUID = -6344812227089227175L;

	private transient Rectangle2D bounds;

	/**
	 * Instantiates a UI component with the specified bounds
	 * 
	 * @param x x coordinate of the top left corner of the UI component
	 * @param y y coordinate of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 */
	public DoaUIComponent(Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height, DoaObject.STATIC_FRONT);
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
		super(position, width, height, DoaObject.STATIC_FRONT);
		recalibrateBounds();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPosition(final DoaVectorF newPosition) {
		super.setPosition(newPosition);
		recalibrateBounds();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWidth(final int width) {
		super.setWidth(width);
		recalibrateBounds();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHeight(final int height) {
		super.setHeight(height);
		recalibrateBounds();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void recalibrateBounds() {
		bounds = new Rectangle2D.Float(position.x, position.y, width, height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle2D getBounds() {
		return bounds;
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
