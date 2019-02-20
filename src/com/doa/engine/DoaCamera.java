package com.doa.engine;

import java.io.Serializable;

import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaMath;
import com.doa.maths.DoaVectorF;

/**
 * Represents a camera construct that looks at a certain point in 2D space, this
 * point can be animate. This means {@code DoaCamera} is capable of following
 * {@code DoaObject}s too. This class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.1.2
 */
public final class DoaCamera implements Serializable {

	private static final long serialVersionUID = 1363138494050985299L;

	private static DoaObject objectToFollow;
	private static float x = 0;
	private static float y = 0;
	private static float z = 1;
	private static float minX = 0;
	private static float minY = 0;
	private static float minZ = 1;
	private static float maxX = 0;
	private static float maxY = 0;
	private static float maxZ = 1;
	private static boolean isObjectToFollowInitialized = false;
	private static boolean isMouseZoomingEnabled = false;

	/**
	 * Constructor.
	 */
	private DoaCamera() {}

	/**
	 * Re-initialises the object to follow, maxX and maxY values of the camera
	 * construct. If {@code objectToFollow == null}, DoaEngine will throw a
	 * NullPointerException.
	 *
	 * @param objectToFollow object to follow
	 * @param minX camera's min x coordinate bound
	 * @param minY camera's min y coordinate bound
	 * @param maxX camera's max x coordinate bound
	 * @param maxY camera's max y coordinate bound
	 * @see NullPointerException
	 */
	public static void adjustCamera(final DoaObject objectToFollow, final int minX, final int minY, final int maxX, final int maxY) {
		isObjectToFollowInitialized = true;
		DoaCamera.objectToFollow = objectToFollow;
		DoaCamera.minX = minX;
		DoaCamera.minY = minY;
		DoaCamera.maxX = maxX;
		DoaCamera.maxY = maxY;
	}

	/**
	 * Enables {@code DoaCamera}'s zoom capabilities. MouseWheel is used to control
	 * the zoom level. If, however, mouseWheel isn't wanted, one can directly change
	 * {@code DoaMouse.WHEEL}.
	 *
	 * @param minZ camera's min z coordinate bound(min zoom)
	 * @param maxZ camera's max z coordinate bound(max zoom)
	 */
	public static void enableMouseZoom(final float minZ, final float maxZ) {
		isMouseZoomingEnabled = true;
		DoaCamera.minZ = minZ;
		DoaCamera.maxZ = maxZ;
	}

	/**
	 * Resets the current zoom level of {@code DoaCamera} back to 1. Zoom of level 1
	 * means no zoom is applied.
	 */
	public static void resetMouseZoom() {
		z = 1;
	}

	/**
	 * Disables {@code DoaCamera}'s zoom capabilities.
	 */
	public static void disableMouseZoom() {
		isMouseZoomingEnabled = false;
	}

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except {@code DoaEngine}. If done otherwise,
	 * {@code DoaEngine} provides no guarantees on the consistency of the internal
	 * state of the Camera construct.
	 */
	public static void tick() {
		if (isObjectToFollowInitialized) {
			final DoaVectorF pos = objectToFollow.getPosition();
			x += (pos.x - x - DoaWindow.WINDOW_WIDTH / 2.0) * 0.05f;
			y += (pos.y - y - DoaWindow.WINDOW_HEIGHT / 2.0) * 0.05f;
		}
		if (isMouseZoomingEnabled) {
			// TODO MOVE THIS TO DoaMouse, because of the pipeline it doens't work. To do
			// this, create a getter for the booleans. TOO MUCH WORK!!!! Please, fork and
			// fix.
			DoaMath.clamp(DoaMouse.WHEEL, minZ, maxZ);
			z += (DoaMouse.WHEEL - z) * 0.025f;
		}
		x = DoaMath.clamp(x, minX, maxX - DoaWindow.WINDOW_WIDTH);
		y = DoaMath.clamp(y, minY, maxY - DoaWindow.WINDOW_HEIGHT);
		z = DoaMath.clamp(z, minZ, maxZ);
	}

	public static DoaObject getObjectToFollow() {
		return objectToFollow;
	}

	public static float getX() {
		return x;
	}

	public static float getY() {
		return y;
	}

	public static float getZ() {
		return z;
	}

	public static float getMinX() {
		return minX;
	}

	public static float getMinY() {
		return minY;
	}

	public static float getMinZ() {
		return minZ;
	}

	public static float getMaxX() {
		return maxX;
	}

	public static float getMaxY() {
		return maxY;
	}

	public static float getMaxZ() {
		return maxZ;
	}
}