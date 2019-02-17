package com.doa.engine;

import java.io.Serializable;

import com.doa.maths.DoaMath;
import com.doa.maths.DoaVectorF;

/**
 * Represents a camera construct that looks at a certain point in 2D space, this
 * point can be animate. This means {@code DoaCamera} is capable of following
 * {@code DoaObject}s too. This class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.1
 */
public final class DoaCamera implements Serializable {

	private static final long serialVersionUID = 1363138494050985299L;

	private static DoaObject objectToFollow;
	private static float x = 0;
	private static float y = 0;
	private static float maxX = 0;
	private static float maxY = 0;
	private static float minX = 0;
	private static float minY = 0;

	/**
	 * Constructor.
	 */
	private DoaCamera() {}

	/**
	 * Re-initialises the object to follow, maxX and maxY values of the camera
	 * construct.
	 *
	 * @param objectToFollow object to follow
	 * @param minX camera's min x coordinate bound
	 * @param minY camera's min y coordinate bound
	 * @param maxX camera's max x coordinate bound
	 * @param maxY camera's max y coordinate bound
	 */
	public static void adjustCamera(final DoaObject objectToFollow, final int minX, final int minY, final int maxX, final int maxY) {
		DoaCamera.objectToFollow = objectToFollow;
		DoaCamera.minX = minX;
		DoaCamera.minY = minY;
		DoaCamera.maxX = maxX;
		DoaCamera.maxY = maxY;
	}

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except {@code DoaEngine}. If done otherwise,
	 * {@code DoaEngine} provides no guarantees on the consistency of the internal
	 * state of the Camera construct.
	 */
	public static void tick() {
		if (objectToFollow != null) {
			final DoaVectorF pos = objectToFollow.getPosition();
			x += (pos.x - x - DoaWindow.WINDOW_WIDTH / 2.0) * 0.05f;
			y += (pos.y - y - DoaWindow.WINDOW_HEIGHT / 2.0) * 0.05f;
		}
		x = DoaMath.clamp(x, minX, maxX - DoaWindow.WINDOW_WIDTH);
		y = DoaMath.clamp(y, minY, maxY - DoaWindow.WINDOW_HEIGHT);
	}

	public static float getX() {
		return x;
	}

	public static float getY() {
		return y;
	}

	public static float getMaxX() {
		return maxX;
	}

	public static float getMaxY() {
		return maxY;
	}
}