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
 * @version 2.1.4
 */
public final class DoaCamera implements Serializable {

	private static final long serialVersionUID = 1363138494050985299L;

	private static DoaObject objectToFollow;
	private static DoaObject objectToZoomInto;
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
	private static float tweenAmountX = 0.005f;
	private static float tweenAmountY = 0.005f;
	private static float tweenAmountZ = 0.025f;

	/**
	 * Constructor.
	 */
	private DoaCamera() {}

	/**
	 * Re-initialises the object to follow, maxX and maxY values of the camera
	 * construct. If {@code objectToFollow == null}, DoaCamera will not follow any
	 * object.
	 *
	 * @param objectToFollow object to follow
	 * @param minX camera's min x coordinate bound
	 * @param minY camera's min y coordinate bound
	 * @param maxX camera's max x coordinate bound
	 * @param maxY camera's max y coordinate bound
	 */
	public static void adjustCamera(final DoaObject objectToFollow, final int minX, final int minY, final int maxX, final int maxY) {
		if (objectToFollow != null) {
			isObjectToFollowInitialized = true;
			DoaCamera.objectToFollow = objectToFollow;
		} else {
			isObjectToFollowInitialized = false;
		}
		DoaCamera.minX = minX;
		DoaCamera.minY = minY;
		DoaCamera.maxX = maxX;
		DoaCamera.maxY = maxY;
	}

	/**
	 * Enables {@code DoaCamera}'s zoom capabilities. MouseWheel is used to control
	 * the zoom level. If, however, mouseWheel isn't wanted, one can directly change
	 * {@code DoaMouse.WHEEL}. If {@code objectToZoom == null}, DoaCamera will use
	 * (0, 0) as center of zoom.
	 * 
	 * @param objectToZoomInto object to zoom into
	 * @param minZ camera's min z coordinate bound(min zoom)
	 * @param maxZ camera's max z coordinate bound(max zoom)
	 */
	public static void enableMouseZoom(final DoaObject objectToZoomInto, final float minZ, final float maxZ) {
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
			x += (pos.x - x - DoaWindow.WINDOW_WIDTH / 2.0) * tweenAmountX;
			y += (pos.y - y - DoaWindow.WINDOW_HEIGHT / 2.0) * tweenAmountY;
		}
		if (isMouseZoomingEnabled) {
			z += (DoaMouse.WHEEL - z) * tweenAmountZ;
		}
		x = DoaMath.clamp(x, minX, maxX - DoaWindow.WINDOW_WIDTH);
		y = DoaMath.clamp(y, minY, maxY - DoaWindow.WINDOW_HEIGHT);
		z = DoaMath.clamp(z, minZ, maxZ);
	}

	public static void setX(float newX) {
		x = newX;
	}

	public static void setY(float newY) {
		y = newY;
	}

	public static void setZ(float newZ) {
		y = newZ;
	}

	public static void setMinX(float newMinX) {
		minX = newMinX;
	}

	public static void setMinY(float newMinY) {
		minY = newMinY;
	}

	public static void setMinZ(float newMinZ) {
		minZ = newMinZ;
	}

	public static void setMaxX(float newMaxX) {
		maxX = newMaxX;
	}

	public static void setMaxY(float newMaxY) {
		maxY = newMaxY;
	}

	public static void setMaxZ(float newMaxZ) {
		maxY = newMaxZ;
	}

	public static void setTweenAmountX(float tweenAmountX) {
		DoaCamera.tweenAmountX = tweenAmountX;
	}

	public static void setTweenAmountY(float tweenAmountY) {
		DoaCamera.tweenAmountY = tweenAmountY;
	}

	public static void setTweenAmountZ(float tweenAmountZ) {
		DoaCamera.tweenAmountZ = tweenAmountZ;
	}

	public static DoaObject getObjectToFollow() {
		return objectToFollow;
	}

	public static DoaObject getObjectToZoomInto() {
		return objectToZoomInto;
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

	public static float getTweenAmountX() {
		return tweenAmountX;
	}

	public static float getTweenAmountY() {
		return tweenAmountY;
	}

	public static float getTweenAmountZ() {
		return tweenAmountZ;
	}

	public static boolean isObjectToFollowInitialized() {
		return isObjectToFollowInitialized;
	}

	public static boolean isMouseZoomingEnabled() {
		return isMouseZoomingEnabled;
	}
}