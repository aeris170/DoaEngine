package com.doa.engine;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.maths.DoaVectorF;
import com.doa.maths.DoaVectorI;

/**
 * Blueprint of all objects that are going to be processed by {@code DoaEngine},
 * all objects that will get updated at every logical frame and will get drawn
 * on the screen must sub-class this class, then it is ensured by
 * {@code DoaEngine} that it will be updated and will be drawn on the screen.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.6.1
 */
public abstract class DoaObject implements Serializable {

	private static final long serialVersionUID = -3293257583358544377L;

	/**
	 * position of {@code DoaObject}
	 */
	protected DoaVectorF position = new DoaVectorF();

	/**
	 * width of {@code DoaObject}
	 */
	protected int width = 0;

	/**
	 * height of {@code DoaObject}
	 */
	protected int height = 0;

	/**
	 * velocity of {@code DoaObject}
	 */
	protected DoaVectorF velocity = new DoaVectorF();

	/**
	 * zOrder of {@code DoaObject}, default is 0.
	 */
	private int zOrder = 0;

	/**
	 * True if camera's transformation will not be concatenated to this DoaObject's
	 * transform. False if otherwise.
	 */
	private boolean isFixed = false;

	/**
	 * Constructor. Constructs a {@code DoaObject} at the specified x and y
	 * coordinates with a width and height of 0.
	 *
	 * @param x x coordinate of {@code DoaObject}
	 * @param y y coordinate of {@code DoaObject}
	 */
	public DoaObject(final float x, final float y) {
		this(x, y, 0, 0);
	}

	/**
	 * Constructor. Constructs a {@code DoaObject} at the specified x and y
	 * coordinates with a width and height of 0.
	 *
	 * @param position position of {@code DoaObject}
	 */
	public DoaObject(final DoaVectorF position) {
		this(position, 0, 0);
	}

	/**
	 * Constructor. Constructs a {@code DoaObject} at the specified x and y
	 * coordinates and with the given width and height values.
	 *
	 * @param x x coordinate of {@code DoaObject}
	 * @param y y coordinate of {@code DoaObject}
	 * @param width width of {@code DoaObject}
	 * @param height height of {@code DoaObject}
	 */
	public DoaObject(final float x, final float y, final int width, final int height) {
		position.x = x;
		position.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Constructor. Constructs a {@code DoaObject} at the specified x and y
	 * coordinates and with the given width and height values.
	 *
	 * @param position position of {@code DoaObject}
	 * @param width width of {@code DoaObject}
	 * @param height height of {@code DoaObject}
	 */
	public DoaObject(final DoaVectorF position, final int width, final int height) {
		this.position = position;
		this.width = width;
		this.height = height;
	}

	/**
	 * Constructor. Constructs a {@code DoaObject} at the specified x and y
	 * coordinates with a width and height of 0 and with the given zOrder.
	 *
	 * @param x x coordinate of {@code DoaObject}
	 * @param y y coordinate of {@code DoaObject}
	 * @param zOrder zOrder of {@code DoaObject}
	 */
	public DoaObject(final float x, final float y, final int zOrder) {
		this(x, y, 0, 0, zOrder);
	}

	/**
	 * Constructor. Constructs a {@code DoaObject} at the specified x and y
	 * coordinates with a width and height of 0 and with the given zOrder.
	 *
	 * @param position position of {@code DoaObject}
	 * @param zOrder zOrder of {@code DoaObject}
	 */
	public DoaObject(final DoaVectorF position, final int zOrder) {
		this(position, 0, 0, zOrder);
	}

	/**
	 * Constructor. Constructs a {@code DoaObject} at the specified x and y
	 * coordinates and with the given width, height and zOrder values.
	 *
	 * @param x x coordinate of {@code DoaObject}
	 * @param y y coordinate of {@code DoaObject}
	 * @param width width of {@code DoaObject}
	 * @param height height of {@code DoaObject}
	 * @param zOrder zOrder of {@code DoaObject}
	 */
	public DoaObject(final float x, final float y, final int width, final int height, final int zOrder) {
		position.x = x;
		position.y = y;
		this.width = width;
		this.height = height;
		this.zOrder = zOrder;
	}

	/**
	 * Constructor. Constructs a {@code DoaObject} at the specified x and y
	 * coordinates and with the given width, height and zOrder values.
	 *
	 * @param position position of {@code DoaObject}
	 * @param width width of {@code DoaObject}
	 * @param height height of {@code DoaObject}
	 * @param zOrder zOrder of {@code DoaObject}
	 */
	public DoaObject(final DoaVectorF position, final int width, final int height, final int zOrder) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.zOrder = zOrder;
	}

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except {@code DoaEngine}. If done otherwise,
	 * {@code DoaEngine} provides no guarantees on the consistency of the internal
	 * state of this {@code DoaObject}. All updates to a {@code DoaObject}'s
	 * internal state must be executed inside this method. When done so,
	 * {@code DoaEngine} guarantees the object's new internal state will reflect to
	 * object's drawing on the screen
	 */
	public abstract void tick();

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except {@code DoaEngine}. If done otherwise,
	 * {@code DoaEngine} provides no guarantees on the quality and consistency of
	 * rendering. All draw calls regarding a {@code DoaObject}'s internal state must
	 * be executed inside this method. This method is always executed on time.
	 *
	 * @param g the graphics context of {@code DoaEngine}
	 */
	public abstract void render(DoaGraphicsContext g);

	/**
	 * Conveniency method to retrieve a {@code DoaObject}'s bounds, added to
	 * {@code DoaObject} because of how useful it is for collision detection. By default, returns the smallest bounding box for this object.
	 *
	 * @return the bounding shape of {@code DoaObject}
	 */
	public Shape getBounds() {
		return new Rectangle2D.Float(position.x, position.y, width, height);
	}

	public DoaVectorF getPosition() {
		return position;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public DoaVectorF getVelocity() {
		return velocity;
	}

	public int getzOrder() {
		return zOrder;
	}

	public DoaVectorI getSize() {
		return new DoaVectorI(width, height);
	}

	public boolean isFixed() {
		return isFixed;
	}

	public void setPosition(final DoaVectorF newPosition) {
		position = newPosition;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public void setVelocity(final DoaVectorF newVelocity) {
		velocity = newVelocity;
	}

	public void setzOrder(final int zOrder) {
		DoaHandler.remove(this);
		this.zOrder = zOrder;
		DoaHandler.add(this);
	}

	public void setSize(DoaVectorI size) {
		width = size.x;
		height = size.y;
	}

	public void setFixed(boolean isFixed) {
		this.isFixed = isFixed;
	}
}