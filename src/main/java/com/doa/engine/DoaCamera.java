package com.doa.engine;

import java.io.Serializable;

import com.doa.engine.input.DoaMouse;
import com.doa.engine.log.DoaLogger;
import com.doa.engine.scene.DoaObject;
import com.doa.maths.DoaMath;
import com.doa.maths.DoaVectorF;

/**
 * Represents a camera construct that looks at a certain point in 2D space, this point can be
 * animate. This means {@code DoaCamera} is capable of following {@code DoaObject}s too. This class
 * is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.7
 */
public final class DoaCamera implements Serializable {

	private static final long serialVersionUID = 1363138494050985299L;

	private static final DoaLogger LOGGER = DoaLogger.getInstance();

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
	 * Re-initialises the object to follow, maxX and maxY values of the camera construct. If
	 * {@code objectToFollow == null}, DoaCamera will not follow any object.
	 *
	 * @param objectToFollow object to follow
	 * @param minX camera's min x coordinate bound
	 * @param minY camera's min y coordinate bound
	 * @param maxX camera's max x coordinate bound
	 * @param maxY camera's max y coordinate bound
	 */
	public static void adjustCamera(final DoaObject objectToFollow, final int minX, final int minY, final int maxX, final int maxY) {
		DoaCamera.objectToFollow = objectToFollow;
		isObjectToFollowInitialized = objectToFollow != null;
		DoaCamera.minX = minX;
		DoaCamera.minY = minY;
		DoaCamera.maxX = maxX;
		DoaCamera.maxY = maxY;
		LOGGER.finer(new StringBuilder(256).append("DoaMouse adjusted minX: ").append(minX).append(", minY: ").append(minY).append(", maxX: ").append(maxX).append(", maxY: ")
		        .append(maxY).toString());
	}

	/**
	 * Enables {@code DoaCamera}'s zoom capabilities. MouseWheel is used to control the zoom level. If,
	 * however, mouseWheel isn't wanted, one can directly change {@code DoaMouse.WHEEL}. If
	 * {@code objectToZoom == null}, DoaCamera will use (0, 0) as center of zoom.
	 *
	 * @param objectToZoomInto object to zoom into
	 * @param minZ camera's min z coordinate bound(min zoom)
	 * @param maxZ camera's max z coordinate bound(max zoom)
	 */
	public static void enableMouseZoom(final DoaObject objectToZoomInto, final float minZ, final float maxZ) {
		DoaCamera.objectToZoomInto = objectToZoomInto;
		isMouseZoomingEnabled = objectToZoomInto != null;
		DoaCamera.minZ = minZ;
		DoaCamera.maxZ = maxZ;
		LOGGER.finer(new StringBuilder(256).append("DoaMouse zoom enabled focus: ").append(objectToZoomInto.toString()).append(", minZ: ").append(minZ).append(", maxZ: ")
		        .append(maxZ).toString());
	}

	/**
	 * Resets the current zoom level of {@code DoaCamera} back to 1. Zoom of level 1 means no zoom is
	 * applied.
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

	static void tick(final int width, final int height) {
		if (isObjectToFollowInitialized) {
			final DoaVectorF pos = objectToFollow.getPosition();
			x += (pos.x - x - width / 2.0) * tweenAmountX;
			y += (pos.y - y - height / 2.0) * tweenAmountY;
		}
		if (isMouseZoomingEnabled) {
			z += (DoaMouse.WHEEL - z) * tweenAmountZ;
		}
		x = DoaMath.clamp(x, minX, maxX - width);
		y = DoaMath.clamp(y, minY, maxY - height);
		z = DoaMath.clamp(z, minZ, maxZ);
	}

	public static void setX(final float newX) {
		LOGGER.finest(new StringBuilder(32).append("DoaCamera#x ").append(x).append(" -> ").append(newX).toString());
		x = newX;
	}

	public static void setY(final float newY) {
		LOGGER.finest(new StringBuilder(32).append("DoaCamera#y ").append(y).append(" -> ").append(newY).toString());
		y = newY;
	}

	public static void setZ(final float newZ) {
		LOGGER.finest(new StringBuilder(32).append("DoaCamera#z ").append(z).append(" -> ").append(newZ).toString());
		y = newZ;
	}

	public static void setMinX(final float newMinX) {
		LOGGER.finest(new StringBuilder(32).append("DoaCamera#minX ").append(minX).append(" -> ").append(newMinX).toString());
		minX = newMinX;
	}

	public static void setMinY(final float newMinY) {
		LOGGER.finest(new StringBuilder(32).append("DoaCamera#minY ").append(minY).append(" -> ").append(newMinY).toString());
		minY = newMinY;
	}

	public static void setMinZ(final float newMinZ) {
		LOGGER.finest(new StringBuilder(32).append("DoaCamera#minZ ").append(minZ).append(" -> ").append(newMinZ).toString());
		minZ = newMinZ;
	}

	public static void setMaxX(final float newMaxX) {
		LOGGER.finest(new StringBuilder(32).append("DoaCamera#maxX ").append(maxX).append(" -> ").append(newMaxX).toString());
		maxX = newMaxX;
	}

	public static void setMaxY(final float newMaxY) {
		LOGGER.finest(new StringBuilder(32).append("DoaCamera#maxY ").append(maxY).append(" -> ").append(newMaxY).toString());
		maxY = newMaxY;
	}

	public static void setMaxZ(final float newMaxZ) {
		LOGGER.finest(new StringBuilder(32).append("DoaCamera#maxZ ").append(maxZ).append(" -> ").append(newMaxZ).toString());
		maxY = newMaxZ;
	}

	public static void setTweenAmountX(final float newTweenAmountX) {
		LOGGER.finest(new StringBuilder(32).append("DoaCamera#tweenAmountX ").append(tweenAmountX).append(" -> ").append(newTweenAmountX).toString());
		tweenAmountX = newTweenAmountX;
	}

	public static void setTweenAmountY(final float newTweenAmountY) {
		LOGGER.finest(new StringBuilder(32).append("DoaCamera#tweenAmountY ").append(tweenAmountY).append(" -> ").append(newTweenAmountY).toString());
		tweenAmountY = newTweenAmountY;
	}

	public static void setTweenAmountZ(final float newTweenAmountZ) {
		LOGGER.finest(new StringBuilder(32).append("DoaCamera#tweenAmountZ ").append(tweenAmountZ).append(" -> ").append(newTweenAmountZ).toString());
		tweenAmountZ = newTweenAmountZ;
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