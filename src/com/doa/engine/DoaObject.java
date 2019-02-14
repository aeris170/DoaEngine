package com.doa.engine;

import java.awt.Shape;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.maths.DoaVectorF;

/**
 * Blueprint of all objects that are going to be processed by {@code DoaEngine},
 * all objects that will get updated at every logical frame and will get drawn
 * on the screen must sub-class this class, then it is ensured by
 * {@code DoaEngine} that it will be updated and will be drawn on the screen.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.0
 */
public abstract class DoaObject {

	/**
	 * {@code DoaObject}s with a zOrder of {@link DoaObject#STATIC_BACK} will be
	 * drawn first to the screen. Because they are drawn first, they will naturally
	 * be behind all other {@code DoaObject}s with higher zOrders. Use this zOrder
	 * if your background is inanimate.
	 */
	public static final int STATIC_BACK = -1;

	/**
	 * {@code DoaObject}s with a zOrder of {@link DoaObject#BACK} will be drawn
	 * second to the screen. Because they are drawn second, they will naturally be
	 * behind all other {@code DoaObject}s with higher zOrders except
	 * {@code DoaObject}s with a zOrder of {@link DoaObject#STATIC_BACK}. Use this
	 * zOrder for animate background/map images.
	 */
	public static final int BACK = 0;

	/**
	 * {@code DoaObject}s with a zOrder of {@link DoaObject#GAME_OBJECTS} will be
	 * drawn in front of {@code DoaObject}s with a zOrder of {@link DoaObject#BACK}
	 * but behind of {@code DoaObject}s with a zOrder of {@link DoaObject#FRONT}.
	 * This value is the default zOrder of a {@code DoaObject}. Use this zOrder for
	 * your player, enemies, blocks, items etc. etc.
	 */
	public static final int GAME_OBJECTS = 1;

	/**
	 * {@code DoaObject}s with a zOrder of {@link DoaObject#FRONT} will be drawn in
	 * front of all other {@code DoaObject}s except {@code DoaObject}s with a zOrder
	 * of {@link DoaObject#STATIC_FRONT}. Use this zOrder to prioritise certain
	 * {@code DoaObject}s over others, like if you want your player to be never
	 * rendered behind enemies.
	 */
	public static final int FRONT = 2;

	/**
	 * {@code DoaObject}s with a zOrder of {@link DoaObject#STATIC_FRONT} will be
	 * drawn in front of all other {@code DoaObject}s, and will not be move relative
	 * to {@code DoaCamera}. Use this zOrder to render HUDS, menus, and certain
	 * on-screen effects like blood splattering.
	 */
	public static final int STATIC_FRONT = 3;

	/**
	 * Null Object of {@code DoaObject}. See the Null Object design pattern for more
	 * info.
	 */
	@SuppressWarnings("synthetic-access")
	public static final DoaObject NULL = new NullDoaObject();

	/**
	 * position of {@code DoaObject}
	 */
	protected DoaVectorF position = new DoaVectorF();

	/**
	 * width of {@code DoaObject}
	 */
	protected Integer width = 0;

	/**
	 * height of {@code DoaObject}
	 */
	protected Integer height = 0;

	/**
	 * velocity of {@code DoaObject}
	 */
	protected DoaVectorF velocity = new DoaVectorF();

	/**
	 * zOrder of {@code DoaObject}, default is {@link DoaObject#GAME_OBJECTS}
	 */
	protected Integer zOrder = GAME_OBJECTS;

	/**
	 * Constructor. Constructs a {@code DoaObject} at the specified x and y
	 * coordinates with a width and height of 0.
	 *
	 * @param x x coordinate of {@code DoaObject}
	 * @param y y coordinate of {@code DoaObject}
	 */
	public DoaObject(final Float x, final Float y) {
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
	public DoaObject(final Float x, final Float y, final Integer width, final Integer height) {
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
	public DoaObject(final DoaVectorF position, final Integer width, final Integer height) {
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
	public DoaObject(final Float x, final Float y, final Integer zOrder) {
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
	 * @deprecated Since DoaEngine v1.1 it is ill-advised to use this method. This
	 *             method only exists for backwards compatibility. Use
	 *             {@link DoaHandler#instantiateDoaObject(Class, Object...)} for
	 *             instantiating objects instead.
	 *             <p>
	 *             <s> Finalises this {@code DoaObject}'s instantiation by adding it
	 *             to {@code DoaHandler}. This method should only be called once,
	 *             STRICTLY after ALL constructor related tasks are finished.
	 *             Otherwise, {@code DoaEngine} might run into a
	 *             NullPointerException. </s>
	 *             </p>
	 * @see java.lang.NullPointerException
	 */
	@Deprecated
	public void finalise() {
		DoaHandler.add(this);
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
	 * {@code DoaObject} because of how useful it is for collision detection.
	 *
	 * @return the bounding shape of {@code DoaObject}
	 */
	public abstract Shape getBounds();

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

	/**
	 * Exists due to DoaObject being an abstract class. See Null Object design
	 * pattern for more info.
	 *
	 * @author Doga Oruc
	 * @since DoaEngine 1.2
	 * @version 1.2
	 */
	private static class NullDoaObject extends DoaObject {

		private NullDoaObject() {
			super(0f, 0f, 0, 0);
		}

		@Override
		public void tick() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void render(final DoaGraphicsContext g) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Shape getBounds() {
			throw new UnsupportedOperationException();
		}
	}
}