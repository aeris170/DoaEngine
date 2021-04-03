package doa.engine.core;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import doa.engine.Internal;
import doa.engine.graphics.DoaAnimation;
import doa.engine.graphics.DoaLights;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.DoaTransform;
import doa.engine.task.DoaTaskGuard;
import doa.engine.task.DoaTasker;

/**
 * Responsible for wrapping the Graphics2D class Sun Microsystems provides. Some
 * functions are omitted for clarity. All and every method this class provides
 * behaves exactly the same as documented in Graphics, Graphics2D and
 * DoaSprites. Refer to these 3 class documentations for this class' behaviour.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.1
 * @version 3.0
 * @see java.awt.Graphics
 * @see java.awt.Graphics2D
 * @see doa.engine.graphics.DoaSprites
 * @see doa.engine.graphics.DoaAnimation
 * @see doa.engine.graphics.DoaAnimations
 */
public final class DoaGraphicsFunctions {

	private static final Map<DoaAnimation, DoaTaskGuard> animationGuards = new HashMap<>();

	private static Graphics2D g = null;
	private static Dimension referenceResolution;
	private static Dimension actualResolution;
	private static boolean lightsShouldContribute = true;

	/**
	 * Used to store transforms passed to {@link #pushTransform()}
	 */
	private static Deque<AffineTransform> transforms = new ArrayDeque<>();

	/**
	 * Used to store composites passed to {@link #pushComposite()}
	 */
	private static Deque<Composite> composites = new ArrayDeque<>();

	/**
	 * Used to store clips passed to {@link #pushClip()}
	 */
	private static Deque<Shape> clips = new ArrayDeque<>();

	/**
	 * Used to store strokes passed to {@link #pushStroke()}
	 */
	private static Deque<Stroke> strokes = new ArrayDeque<>();

	/**
	 * Used to store colors passed to {@link #pushColor()}
	 */
	private static Deque<Color> colors = new ArrayDeque<>();

	private DoaGraphicsFunctions() {}

	/**
	 * Clears the specified rectangle by filling it with the background color of the
	 * current drawing surface. This operation does not use the current paint mode.
	 * <p>
	 * Beginning with Java&nbsp;1.1, the background color of offscreen images may be
	 * system dependent. Applications should use setColor followed by fillRect to
	 * ensure that an offscreen image is cleared to a specific color.
	 *
	 * @param x the <i>x</i> coordinate of the rectangle to clear.
	 * @param y the <i>y</i> coordinate of the rectangle to clear.
	 * @param width the width of the rectangle to clear.
	 * @param height the height of the rectangle to clear.
	 * @see java.awt.Graphics#fillRect(int, int, int, int)
	 * @see java.awt.Graphics#drawRect
	 * @see java.awt.Graphics#setColor(java.awt.Color)
	 * @see java.awt.Graphics#setPaintMode
	 * @see java.awt.Graphics#setXORMode(java.awt.Color)
	 */
	public static void clearRect(final float x, final float y, final float width, final float height) {
		g.clearRect((int) x, (int) y, (int) width, (int) height);
	}

	/**
	 * Intersects the current clip with the specified rectangle. The resulting
	 * clipping area is the intersection of the current clipping area and the
	 * specified rectangle. If there is no current clipping area, either because the
	 * clip has never been set, or the clip has been cleared using
	 * {@code setClip(null)}, the specified rectangle becomes the new clip. This
	 * method sets the user clip, which is independent of the clipping associated
	 * with device bounds and window visibility. This method can only be used to
	 * make the current clip smaller. To set the current clip larger, use any of the
	 * setClip methods. Rendering operations have no effect outside of the clipping
	 * area.
	 *
	 * @param x the x coordinate of the rectangle to intersect the clip with
	 * @param y the y coordinate of the rectangle to intersect the clip with
	 * @param width the width of the rectangle to intersect the clip with
	 * @param height the height of the rectangle to intersect the clip with
	 * @see #setClip(float, float, float, float)
	 * @see #setClip(Shape)
	 */
	public static void clipRect(final float x, final float y, final float width, final float height) {
		g.clipRect((int) x, (int) y, (int) width, (int) height);
	}

	/**
	 * Disposes of this graphics context and releases any system resources that it
	 * is using. A Graphics object cannot be used after dispose has been called.
	 * <p>
	 * When a Java program runs, a large number of Graphics objects can be created
	 * within a short time frame. Although the finalization process of the garbage
	 * collector also disposes of the same system resources, it is preferable to
	 * manually free the associated resources by calling this method rather than to
	 * rely on a finalization process which may not run to completion for a long
	 * period of time.
	 * <p>
	 * Graphics objects which are provided as arguments to the paint and update
	 * methods of components are automatically released by the system when those
	 * methods return. For efficiency, programmers should call dispose when finished
	 * using a Graphics object only if it was created directly from a component or
	 * another Graphics object.
	 *
	 * @see java.awt.Graphics#finalize
	 * @see java.awt.Component#paint
	 * @see java.awt.Component#update
	 * @see java.awt.Component#getGraphics
	 * @see java.awt.Graphics#create
	 */
	static void dispose() { g.dispose(); }

	public static void drawAnimation(final DoaAnimation anim, final float x, final float y, final float width, final float height) {
		animationGuards.computeIfAbsent(anim, k -> new DoaTaskGuard());
		final DoaTaskGuard guard = animationGuards.get(anim);
		if (guard.get()) {
			DoaTasker.guard(guard, anim.getDelay());
			drawImage(anim.next(), x, y, width, height);
		} else {
			drawImage(anim.current(), x, y, width, height);
		}
	}

	public static void drawAnimation(final DoaAnimation anim, final float x, final float y) {
		animationGuards.computeIfAbsent(anim, k -> new DoaTaskGuard());
		final DoaTaskGuard guard = animationGuards.get(anim);
		if (guard.get()) {
			DoaTasker.guard(guard, anim.getDelay());
			final BufferedImage spr = anim.next();
			drawImage(spr, x, y, spr.getWidth(), spr.getHeight());
		} else {
			final BufferedImage spr = anim.current();
			drawImage(spr, x, y, spr.getWidth(), spr.getHeight());
		}
	}

	/**
	 * Draws the outline of a circular or elliptical arc covering the specified
	 * rectangle.
	 * <p>
	 * The resulting arc begins at startAngle and extends for arcAngle degrees,
	 * using the current color. Angles are interpreted such that 0&nbsp;degrees is
	 * at the 3&nbsp;o'clock position. A positive value indicates a
	 * counter-clockwise rotation while a negative value indicates a clockwise
	 * rotation.
	 * <p>
	 * The center of the arc is the center of the rectangle whose origin is
	 * (<i>x</i>,&nbsp;<i>y</i>) and whose size is specified by the width and height
	 * arguments.
	 * <p>
	 * The resulting arc covers an area <code>width&nbsp;+&nbsp;1</code> pixels wide
	 * by <code>height&nbsp;+&nbsp;1</code> pixels tall.
	 * <p>
	 * The angles are specified relative to the non-square extents of the bounding
	 * rectangle such that 45 degrees always falls on the line from the center of
	 * the ellipse to the upper right corner of the bounding rectangle. As a result,
	 * if the bounding rectangle is noticeably longer in one axis than the other,
	 * the angles to the start and end of the arc segment will be skewed farther
	 * along the longer axis of the bounds.
	 *
	 * @param x the <i>x</i> coordinate of the upper-left corner of the arc to be
	 *        drawn.
	 * @param y the <i>y</i> coordinate of the upper-left corner of the arc to be
	 *        drawn.
	 * @param width the width of the arc to be drawn.
	 * @param height the height of the arc to be drawn.
	 * @param startAngle the beginning angle.
	 * @param arcAngle the angular extent of the arc, relative to the start angle.
	 * @see java.awt.Graphics#fillArc
	 */
	public static void drawArc(final float x, final float y, final float width, final float height, final float startAngle, final float arcAngle) {
		var w = warp(x, y, width, height);
		g.drawArc(w[0], w[1], w[2], w[3], (int) startAngle, (int) arcAngle);
	}

	/**
	 * Draws as much of the specified image as is currently available. The image is
	 * drawn with its top-left corner at (<i>x</i>,&nbsp;<i>y</i>) in this graphics
	 * context's coordinate space. Transparent pixels in the image do not affect
	 * whatever pixels are already there.
	 * <p>
	 * This method returns immediately in all cases, even if the complete image has
	 * not yet been loaded, and it has not been dithered and converted for the
	 * current output device.
	 * <p>
	 * If the image has completely loaded and its pixels are no longer being
	 * changed, then drawImage returns true. Otherwise, drawImage returns false and
	 * as more of the image becomes available or it is time to draw another frame of
	 * animation, the process that loads the image notifies the specified image
	 * observer.
	 *
	 * @param img the specified image to be drawn. This method does nothing if img
	 *        is null.
	 * @param x the <i>x</i> coordinate.
	 * @param y the <i>y</i> coordinate.
	 * @return false if the image pixels are still changing; true otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int,
	 *      int, int)
	 */
	public static boolean drawImage(final Image img, final float x, final float y) {
		var w = warp(x, y);
		return g.drawImage(img, w[0], w[1], null);
	}

	/**
	 * Draws as much of the specified image as has already been scaled to fit inside
	 * the specified rectangle.
	 * <p>
	 * The image is drawn inside the specified rectangle of this graphics context's
	 * coordinate space, and is scaled if necessary. Transparent pixels do not
	 * affect whatever pixels are already there.
	 * <p>
	 * This method returns immediately in all cases, even if the entire image has
	 * not yet been scaled, dithered, and converted for the current output device.
	 * If the current output representation is not yet complete, then drawImage
	 * returns false. As more of the image becomes available, the process that loads
	 * the image notifies the image observer by calling its imageUpdate method.
	 * <p>
	 * A scaled version of an image will not necessarily be available immediately
	 * just because an unscaled version of the image has been constructed for this
	 * output device. Each size of the image may be cached separately and generated
	 * from the original data in a separate image production sequence.
	 *
	 * @param img the specified image to be drawn. This method does nothing if img
	 *        is null.
	 * @param x the <i>x</i> coordinate.
	 * @param y the <i>y</i> coordinate.
	 * @param width the width of the rectangle.
	 * @param height the height of the rectangle.
	 * @return false if the image pixels are still changing; true otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int,
	 *      int, int)
	 */
	public static boolean drawImage(final Image img, final float x, final float y, final float width, final float height) {
		var w = warp(x, y, width, height);
		return g.drawImage(img, w[0], w[1], w[2], w[3], null);
	}

	/**
	 * Draws as much of the specified area of the specified image as is currently
	 * available, scaling it on the fly to fit inside the specified area of the
	 * destination drawable surface. Transparent pixels do not affect whatever
	 * pixels are already there.
	 * <p>
	 * This method returns immediately in all cases, even if the image area to be
	 * drawn has not yet been scaled, dithered, and converted for the current output
	 * device. If the current output representation is not yet complete then
	 * drawImage returns false. As more of the image becomes available, the process
	 * that loads the image notifies the specified image observer.
	 * <p>
	 * This method always uses the unscaled version of the image to render the
	 * scaled rectangle and performs the required scaling on the fly. It does not
	 * use a cached, scaled version of the image for this operation. Scaling of the
	 * image from source to destination is performed such that the first coordinate
	 * of the source rectangle is mapped to the first coordinate of the destination
	 * rectangle, and the second source coordinate is mapped to the second
	 * destination coordinate. The subimage is scaled and flipped as needed to
	 * preserve those mappings.
	 *
	 * @param img the specified image to be drawn. This method does nothing if img
	 *        is null.
	 * @param dx1 the <i>x</i> coordinate of the first corner of the destination
	 *        rectangle.
	 * @param dy1 the <i>y</i> coordinate of the first corner of the destination
	 *        rectangle.
	 * @param dx2 the <i>x</i> coordinate of the second corner of the destination
	 *        rectangle.
	 * @param dy2 the <i>y</i> coordinate of the second corner of the destination
	 *        rectangle.
	 * @param sx1 the <i>x</i> coordinate of the first corner of the source
	 *        rectangle.
	 * @param sy1 the <i>y</i> coordinate of the first corner of the source
	 *        rectangle.
	 * @param sx2 the <i>x</i> coordinate of the second corner of the source
	 *        rectangle.
	 * @param sy2 the <i>y</i> coordinate of the second corner of the source
	 *        rectangle.
	 * @return false if the image pixels are still changing; true otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int,
	 *      int, int)
	 * @since 1.1
	 */
	public static boolean drawImage(final Image img, final float dx1, final float dy1, final float dx2, final float dy2, final float sx1, final float sy1, final float sx2, final float sy2) {
		var d = warp(dx1, dy1, dx2, dy2);
		var s = warp(sx1, sy1, sx2, sy2);
		return g.drawImage(img, d[0], d[1], d[2], d[3], s[0], s[1], s[2], s[3], null);
	}

	/**
	 * Draws a line, using the current color, between the points
	 * <code>(x1,&nbsp;y1)</code> and <code>(x2,&nbsp;y2)</code> in this graphics
	 * context's coordinate system.
	 *
	 * @param x1 the first point's <i>x</i> coordinate.
	 * @param y1 the first point's <i>y</i> coordinate.
	 * @param x2 the second point's <i>x</i> coordinate.
	 * @param y2 the second point's <i>y</i> coordinate.
	 */
	public static void drawLine(final float x1, final float y1, final float x2, final float y2) {
		var w = warp(x1, y1, x2, y2);
		g.drawLine(w[0], w[1], w[2], w[3]);
	}

	/**
	 * Draws the outline of an oval. The result is a circle or ellipse that fits
	 * within the rectangle specified by the x, y, width, and height arguments.
	 * <p>
	 * The oval covers an area that is <code>width&nbsp;+&nbsp;1</code> pixels wide
	 * and <code>height&nbsp;+&nbsp;1</code> pixels tall.
	 *
	 * @param x the <i>x</i> coordinate of the upper left corner of the oval to be
	 *        drawn.
	 * @param y the <i>y</i> coordinate of the upper left corner of the oval to be
	 *        drawn.
	 * @param width the width of the oval to be drawn.
	 * @param height the height of the oval to be drawn.
	 * @see java.awt.Graphics#fillOval
	 */
	public static void drawOval(final float x, final float y, final float width, final float height) {
		var w = warp(x, y, width, height);
		g.drawOval(w[0], w[1], w[2], w[3]);
	}

	/**
	 * Draws a closed polygon defined by arrays of <i>x</i> and <i>y</i>
	 * coordinates. Each pair of (<i>x</i>,&nbsp;<i>y</i>) coordinates defines a
	 * point.
	 * <p>
	 * This method draws the polygon defined by nPoint line segments, where the
	 * first <code>nPoint&nbsp;-&nbsp;1</code> line segments are line segments from
	 * <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code> to
	 * <code>(xPoints[i],&nbsp;yPoints[i])</code>, for
	 * 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;nPoints. The figure is automatically
	 * closed by drawing a line connecting the final point to the first point, if
	 * those points are different.
	 *
	 * @param xPoints a an array of x coordinates.
	 * @param yPoints a an array of y coordinates.
	 * @param nPoints a the total number of points.
	 * @see java.awt.Graphics#fillPolygon
	 * @see java.awt.Graphics#drawPolyline
	 */
	public static void drawPolygon(final float[] xPoints, final float[] yPoints, final int nPoints) {
		g.drawPolygon(warpX(xPoints), warpY(yPoints), nPoints);
	}

	/**
	 * Draws a sequence of connected lines defined by arrays of <i>x</i> and
	 * <i>y</i> coordinates. Each pair of (<i>x</i>,&nbsp;<i>y</i>) coordinates
	 * defines a point. The figure is not closed if the first point differs from the
	 * last point.
	 *
	 * @param xPoints an array of <i>x</i> points
	 * @param yPoints an array of <i>y</i> points
	 * @param nPoints the total number of points
	 * @see java.awt.Graphics#drawPolygon(int[], int[], int)
	 * @since 1.1
	 */
	public static void drawPolyline(final float[] xPoints, final float[] yPoints, final int nPoints) {
		g.drawPolyline(warpX(xPoints), warpY(yPoints), nPoints);
	}

	/**
	 * Draws an outlined round-cornered rectangle using this graphics context's
	 * current color. The left and right edges of the rectangle are at x and
	 * <code>x&nbsp;+&nbsp;width</code>, respectively. The top and bottom edges of
	 * the rectangle are at y and <code>y&nbsp;+&nbsp;height</code>.
	 *
	 * @param x the <i>x</i> coordinate of the rectangle to be drawn.
	 * @param y the <i>y</i> coordinate of the rectangle to be drawn.
	 * @param width the width of the rectangle to be drawn.
	 * @param height the height of the rectangle to be drawn.
	 * @param arcWidth the horizontal diameter of the arc at the four corners.
	 * @param arcHeight the vertical diameter of the arc at the four corners.
	 * @see java.awt.Graphics#fillRoundRect
	 */
	public static void drawRoundRect(final float x, final float y, final float width, final float height, final float arcWidth, final float arcHeight) {
		var w = warp(x, y, width, height);
		g.drawRoundRect(w[0], w[1], w[2], w[3], (int) arcWidth, (int) arcHeight);
	}

	/**
	 * Fills a circular or elliptical arc covering the specified rectangle.
	 * <p>
	 * The resulting arc begins at startAngle and extends for arcAngle degrees.
	 * Angles are interpreted such that 0&nbsp;degrees is at the 3&nbsp;o'clock
	 * position. A positive value indicates a counter-clockwise rotation while a
	 * negative value indicates a clockwise rotation.
	 * <p>
	 * The center of the arc is the center of the rectangle whose origin is
	 * (<i>x</i>,&nbsp;<i>y</i>) and whose size is specified by the width and height
	 * arguments.
	 * <p>
	 * The resulting arc covers an area <code>width&nbsp;+&nbsp;1</code> pixels wide
	 * by <code>height&nbsp;+&nbsp;1</code> pixels tall.
	 * <p>
	 * The angles are specified relative to the non-square extents of the bounding
	 * rectangle such that 45 degrees always falls on the line from the center of
	 * the ellipse to the upper right corner of the bounding rectangle. As a result,
	 * if the bounding rectangle is noticeably longer in one axis than the other,
	 * the angles to the start and end of the arc segment will be skewed farther
	 * along the longer axis of the bounds.
	 *
	 * @param x the <i>x</i> coordinate of the upper-left corner of the arc to be
	 *        filled.
	 * @param y the <i>y</i> coordinate of the upper-left corner of the arc to be
	 *        filled.
	 * @param width the width of the arc to be filled.
	 * @param height the height of the arc to be filled.
	 * @param startAngle the beginning angle.
	 * @param arcAngle the angular extent of the arc, relative to the start angle.
	 * @see java.awt.Graphics#drawArc
	 */
	public static void fillArc(final float x, final float y, final float width, final float height, final float startAngle, final float arcAngle) {
		var w = warp(x, y, width, height);
		g.fillArc(w[0], w[1], w[2], w[3], (int) startAngle, (int) arcAngle);
	}

	/**
	 * Fills an oval bounded by the specified rectangle with the current color.
	 *
	 * @param x the <i>x</i> coordinate of the upper left corner of the oval to be
	 *        filled.
	 * @param y the <i>y</i> coordinate of the upper left corner of the oval to be
	 *        filled.
	 * @param width the width of the oval to be filled.
	 * @param height the height of the oval to be filled.
	 * @see java.awt.Graphics#drawOval
	 */
	public static void fillOval(final float x, final float y, final float width, final float height) {
		var w = warp(x, y, width, height);
		g.fillOval(w[0], w[1], w[2], w[3]);
	}

	/**
	 * Fills a closed polygon defined by arrays of <i>x</i> and <i>y</i>
	 * coordinates.
	 * <p>
	 * This method draws the polygon defined by nPoint line segments, where the
	 * first <code>nPoint&nbsp;-&nbsp;1</code> line segments are line segments from
	 * <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code> to
	 * <code>(xPoints[i],&nbsp;yPoints[i])</code>, for
	 * 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;nPoints. The figure is automatically
	 * closed by drawing a line connecting the final point to the first point, if
	 * those points are different.
	 * <p>
	 * The area inside the polygon is defined using an even-odd fill rule, also
	 * known as the alternating rule.
	 *
	 * @param xPoints a an array of x coordinates.
	 * @param yPoints a an array of y coordinates.
	 * @param nPoints a the total number of points.
	 * @see java.awt.Graphics#drawPolygon(int[], int[], int)
	 */
	// TODO
	public static void fillPolygon(final float[] xPoints, final float[] yPoints, final int nPoints) { g.fillPolygon(warpX(xPoints), warpY(yPoints), nPoints); }

	/**
	 * Fills the specified rectangle. The left and right edges of the rectangle are
	 * at x and <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>. The top and bottom
	 * edges are at y and <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>. The
	 * resulting rectangle covers an area width pixels wide by height pixels tall.
	 * The rectangle is filled using the graphics context's current color.
	 *
	 * @param x the <i>x</i> coordinate of the rectangle to be filled.
	 * @param y the <i>y</i> coordinate of the rectangle to be filled.
	 * @param width the width of the rectangle to be filled.
	 * @param height the height of the rectangle to be filled.
	 * @see java.awt.Graphics#clearRect
	 * @see java.awt.Graphics#drawRect
	 */
	public static void fillRect(final float x, final float y, final float width, final float height) {
		var w = warp(x, y, width, height);
		g.fillRect(w[0], w[1], w[2], w[3]);
	}

	/**
	 * Fills the specified rounded corner rectangle with the current color. The left
	 * and right edges of the rectangle are at x and
	 * <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>, respectively. The top and
	 * bottom edges of the rectangle are at y and
	 * <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>.
	 *
	 * @param x the <i>x</i> coordinate of the rectangle to be filled.
	 * @param y the <i>y</i> coordinate of the rectangle to be filled.
	 * @param width the width of the rectangle to be filled.
	 * @param height the height of the rectangle to be filled.
	 * @param arcWidth the horizontal diameter of the arc at the four corners.
	 * @param arcHeight the vertical diameter of the arc at the four corners.
	 * @see java.awt.Graphics#drawRoundRect
	 */
	public static void fillRoundRect(final float x, final float y, final float width, final float height, final float arcWidth, final float arcHeight) {
		var w = warp(x, y, width, height);
		g.fillRoundRect(w[0], w[1], w[2], w[3], (int) arcWidth, (int) arcHeight);
	}

	/**
	 * Gets the current clipping area. This method returns the user clip, which is
	 * independent of the clipping associated with device bounds and window
	 * visibility. If no clip has previously been set, or if the clip has been
	 * cleared using {@code setClip(null)}, this method returns null.
	 *
	 * @return a Shape object representing the current clipping area, or null if no
	 *         clip is set.
	 * @see java.awt.Graphics#getClipBounds
	 * @see java.awt.Graphics#clipRect
	 * @see java.awt.Graphics#setClip(int, int, int, int)
	 * @see java.awt.Graphics#setClip(Shape)
	 * @since 1.1
	 */
	public static Shape getClip() { return g.getClip(); }

	/**
	 * Returns the bounding rectangle of the current clipping area. This method
	 * refers to the user clip, which is independent of the clipping associated with
	 * device bounds and window visibility. If no clip has previously been set, or
	 * if the clip has been cleared using {@code setClip(null)}, this method returns
	 * null. The coordinates in the rectangle are relative to the coordinate system
	 * origin of this graphics context.
	 *
	 * @return the bounding rectangle of the current clipping area, or null if no
	 *         clip is set.
	 * @see java.awt.Graphics#getClip
	 * @see java.awt.Graphics#clipRect
	 * @see java.awt.Graphics#setClip(int, int, int, int)
	 * @see java.awt.Graphics#setClip(Shape)
	 * @since 1.1
	 */
	public static Rectangle getClipBounds() { return g.getClipBounds(); }

	/**
	 * Gets this graphics context's current color.
	 *
	 * @return this graphics context's current color.
	 * @see java.awt.Color
	 * @see java.awt.Graphics#setColor(Color)
	 */
	public static Color getColor() { return g.getColor(); }

	/**
	 * Gets the current font.
	 *
	 * @return this graphics context's current font.
	 * @see java.awt.Font
	 * @see java.awt.Graphics#setFont(Font)
	 */
	public static Font getFont() { return g.getFont(); }

	/**
	 * Gets the font metrics for the specified font.
	 *
	 * @return the font metrics for the specified font.
	 * @param f the specified font
	 * @see java.awt.Graphics#getFont
	 * @see java.awt.FontMetrics
	 * @see java.awt.Graphics#getFontMetrics()
	 */
	public static FontMetrics getFontMetrics(final Font f) { return g.getFontMetrics(f); }

	/**
	 * Sets the current clipping area to an arbitrary clip shape. Not all objects
	 * that implement the Shape interface can be used to set the clip. The only
	 * Shape objects that are guaranteed to be supported are Shape objects that are
	 * obtained via the getClip method and via Rectangle objects. This method sets
	 * the user clip, which is independent of the clipping associated with device
	 * bounds and window visibility.
	 *
	 * @param clip the Shape to use to set the clip
	 * @see java.awt.Graphics#getClip()
	 * @see java.awt.Graphics#clipRect
	 * @see java.awt.Graphics#setClip(int, int, int, int)
	 * @since 1.1
	 */
	public static void setClip(final Shape clip) { g.setClip(clip); }

	/**
	 * Sets the current clip to the rectangle specified by the given coordinates.
	 * This method sets the user clip, which is independent of the clipping
	 * associated with device bounds and window visibility. Rendering operations
	 * have no effect outside of the clipping area.
	 *
	 * @param x the <i>x</i> coordinate of the new clip rectangle.
	 * @param y the <i>y</i> coordinate of the new clip rectangle.
	 * @param width the width of the new clip rectangle.
	 * @param height the height of the new clip rectangle.
	 * @see java.awt.Graphics#clipRect
	 * @see java.awt.Graphics#setClip(Shape)
	 * @see java.awt.Graphics#getClip
	 * @since 1.1
	 */
	public static void setClip(final float x, final float y, final float width, final float height) {
		var w = warp(x, y, width, height);
		g.setClip(w[0], w[1], w[2], w[3]);
	}

	/**
	 * Sets this graphics context's current color to the specified color. All
	 * subsequent graphics operations using this graphics context use this specified
	 * color.
	 *
	 * @param c the new rendering color.
	 * @see java.awt.Color
	 * @see java.awt.Graphics#getColor
	 */
	public static void setColor(final Color c) {
		if (lightsShouldContribute) {
			final Color ambientLightColor = DoaLights.getAmbientLightColor();
			final int red = c.getRed() * ambientLightColor.getRed() / 255;
			final int green = c.getGreen() * ambientLightColor.getGreen() / 255;
			final int blue = c.getBlue() * ambientLightColor.getBlue() / 255;
			final int alpha = c.getAlpha();
			g.setColor(new Color(red, green, blue, alpha));
		} else {
			g.setColor(c);
		}
	}

	/**
	 * Sets this graphics context's font to the specified font. All subsequent text
	 * operations using this graphics context use this font. A null argument is
	 * silently ignored.
	 *
	 * @param font the font.
	 * @see java.awt.Graphics#getFont
	 * @see java.awt.Graphics#drawString(java.lang.String, int, int)
	 * @see java.awt.Graphics#drawBytes(byte[], int, int, int, int)
	 * @see java.awt.Graphics#drawChars(char[], int, int, int, int)
	 */
	public static void setFont(final Font font) { g.setFont(font); }

	/**
	 * Sets the paint mode of this graphics context to overwrite the destination
	 * with this graphics context's current color. This sets the logical pixel
	 * operation function to the paint or overwrite mode. All subsequent rendering
	 * operations will overwrite the destination with the current color.
	 */
	public static void setPaintMode() { g.setPaintMode(); }

	/**
	 * Sets the paint mode of this graphics context to alternate between this
	 * graphics context's current color and the new specified color. This specifies
	 * that logical pixel operations are performed in the XOR mode, which alternates
	 * pixels between the current color and a specified XOR color.
	 * <p>
	 * When drawing operations are performed, pixels which are the current color are
	 * changed to the specified color, and vice versa.
	 * <p>
	 * Pixels that are of colors other than those two colors are changed in an
	 * unpredictable but reversible manner; if the same figure is drawn twice, then
	 * all pixels are restored to their original values.
	 *
	 * @param c1 the XOR alternation color
	 */
	public static void setXORMode(final Color c1) { g.setXORMode(c1); }

	public static void addRenderingHints(final Map<?, ?> hints) { g.addRenderingHints(hints); }

	public static void clip(final Shape s) { g.clip(s); }

	public static void draw(final Shape s) { g.draw(s); }

	/**
	 * Draws the text given by the specified string, using this graphics context's
	 * current font and color. The baseline of the leftmost character is at position
	 * (<i>x</i>,&nbsp;<i>y</i>) in this graphics context's coordinate system.
	 *
	 * @param str the string to be drawn.
	 * @param x the <i>x</i> coordinate.
	 * @param y the <i>y</i> coordinate.
	 * @throws NullPointerException if str is null.
	 * @see java.awt.Graphics#drawBytes
	 * @see java.awt.Graphics#drawChars
	 */
	public static void drawString(final String str, final float x, final float y) {
		var w = warp(x, y);
		g.drawString(str, w[0], w[1]);
	}

	/**
	 * Draws the outline of the specified rectangle. The left and right edges of the
	 * rectangle are at x and <code>x&nbsp;+&nbsp;width</code>. The top and bottom
	 * edges are at y and <code>y&nbsp;+&nbsp;height</code>. The rectangle is drawn
	 * using the graphics context's current color.
	 *
	 * @param x the <i>x</i> coordinate of the rectangle to be drawn.
	 * @param y the <i>y</i> coordinate of the rectangle to be drawn.
	 * @param width the width of the rectangle to be drawn.
	 * @param height the height of the rectangle to be drawn.
	 * @see java.awt.Graphics#fillRect
	 * @see java.awt.Graphics#clearRect
	 */
	public static void drawRect(final float x, final float y, final float width, final float height) {
		var w = warp(x, y, width, height);
		g.drawRect(w[0], w[1], w[2], w[3]);
	}

	public static void fill(final Shape s) { g.fill(s); }

	/**
	 * Returns the background color used for clearing a region.
	 *
	 * @return the current {@code Graphics2D Color}, which defines the background
	 *         color.
	 * @see #setBackground
	 */
	public static Color getBackground() { return g.getBackground(); }

	/**
	 * Returns the current Composite in the Graphics2D context.
	 *
	 * @return the current {@code Graphics2D Composite}, which defines a compositing
	 *         style.
	 * @see #setComposite
	 */
	public static Composite getComposite() { return g.getComposite(); }

	/**
	 * Returns the device configuration associated with this Graphics2D.
	 *
	 * @return the device configuration of this Graphics2D.
	 */
	public static GraphicsConfiguration getDeviceConfiguration() { return g.getDeviceConfiguration(); }

	/**
	 * Get the rendering context of the Font within this Graphics2D context. The
	 * FontRenderContext encapsulates application hints such as anti-aliasing and
	 * fractional metrics, as well as target device specific information such as
	 * dots-per-inch. This information should be provided by the application when
	 * using objects that perform typographical formatting, such as Font and
	 * TextLayout. This information should also be provided by applications that
	 * perform their own layout and need accurate measurements of various
	 * characteristics of glyphs such as advance and line height when various
	 * rendering hints have been applied to the text rendering.
	 *
	 * @return a reference to an instance of FontRenderContext.
	 * @see java.awt.font.FontRenderContext
	 * @see java.awt.Font#createGlyphVector
	 * @see java.awt.font.TextLayout
	 * @since 1.2
	 */
	public static FontRenderContext getFontRenderContext() { return g.getFontRenderContext(); }

	/**
	 * Returns the current Paint of the Graphics2D context.
	 *
	 * @return the current {@code Graphics2D Paint}, which defines a color or
	 *         pattern.
	 * @see #setPaint
	 * @see java.awt.Graphics#setColor
	 */
	public static Paint getPaint() { return g.getPaint(); }

	/**
	 * Returns the value of a single preference for the rendering algorithms. Hint
	 * categories include controls for rendering quality and overall time/quality
	 * trade-off in the rendering process. Refer to the RenderingHints class for
	 * definitions of some common keys and values.
	 *
	 * @param hintKey the key corresponding to the hint to get.
	 * @return an object representing the value for the specified hint key. Some of
	 *         the keys and their associated values are defined in the
	 *         RenderingHints class.
	 * @see RenderingHints
	 * @see #setRenderingHint(RenderingHints.Key, Object)
	 */
	public static Object getRenderingHint(final Key hintKey) { return g.getRenderingHint(hintKey); }

	/**
	 * Gets the preferences for the rendering algorithms. Hint categories include
	 * controls for rendering quality and overall time/quality trade-off in the
	 * rendering process. Returns all of the hint key/value pairs that were ever
	 * specified in one operation. Refer to the RenderingHints class for definitions
	 * of some common keys and values.
	 *
	 * @return a reference to an instance of RenderingHints that contains the
	 *         current preferences.
	 * @see RenderingHints
	 * @see #setRenderingHints(Map)
	 */
	public static RenderingHints getRenderingHints() { return g.getRenderingHints(); }

	/**
	 * Returns the current Stroke in the Graphics2D context.
	 *
	 * @return the current {@code Graphics2D Stroke}, which defines the line style.
	 * @see #setStroke
	 */
	public static Stroke getStroke() { return g.getStroke(); }

	/**
	 * Returns a copy of the current Transform in the Graphics2D context.
	 *
	 * @return the current AffineTransform in the Graphics2D context.
	 * @see #transform
	 * @see #setTransform
	 */
	public static AffineTransform getTransform() { return g.getTransform(); }

	/**
	 * Concatenates the current Graphics2D Transform with a rotation transform.
	 * Subsequent rendering is rotated by the specified radians relative to the
	 * previous origin. This is equivalent to calling {@code transform(R)}, where R
	 * is an AffineTransform represented by the following matrix:
	 *
	 * <pre>
	 *          [   cos(theta)    -sin(theta)    0   ]
	 *          [   sin(theta)     cos(theta)    0   ]
	 *          [       0              0         1   ]
	 * </pre>
	 *
	 * Rotating with a positive angle theta rotates points on the positive x axis
	 * toward the positive y axis.
	 *
	 * @param theta the angle of rotation in radians
	 */
	public static void rotate(final float theta) { g.rotate(theta); }

	/**
	 * Concatenates the current Graphics2D Transform with a translated rotation
	 * transform. Subsequent rendering is transformed by a transform which is
	 * constructed by translating to the specified location, rotating by the
	 * specified radians, and translating back by the same amount as the original
	 * translation. This is equivalent to the following sequence of calls:
	 *
	 * <pre>
	 * translate(x, y);
	 * rotate(theta);
	 * translate(-x, -y);
	 * </pre>
	 *
	 * Rotating with a positive angle theta rotates points on the positive x axis
	 * toward the positive y axis.
	 *
	 * @param theta the angle of rotation in radians
	 * @param x the x coordinate of the origin of the rotation
	 * @param y the y coordinate of the origin of the rotation
	 */
	public static void rotate(final float theta, final float x, final float y) {
		var w = warp(x, y);
		g.rotate(theta, w[0], w[1]);
	}

	/**
	 * Concatenates the current Graphics2D Transform with a scaling transformation
	 * Subsequent rendering is resized according to the specified scaling factors
	 * relative to the previous scaling. This is equivalent to calling
	 * {@code transform(S)}, where S is an AffineTransform represented by the
	 * following matrix:
	 *
	 * <pre>
	 *          [   sx   0    0   ]
	 *          [   0    sy   0   ]
	 *          [   0    0    1   ]
	 * </pre>
	 *
	 * @param sx the amount by which X coordinates in subsequent rendering
	 *        operations are multiplied relative to previous rendering operations.
	 * @param sy the amount by which Y coordinates in subsequent rendering
	 *        operations are multiplied relative to previous rendering operations.
	 */
	public static void scale(final float sx, final float sy) { g.scale(sx, sy); }

	public static void setBackground(final Color color) { g.setBackground(color); }

	/**
	 * Sets the Composite for the Graphics2D context. The Composite is used in all
	 * drawing methods such as drawImage, drawString, draw, and fill. It specifies
	 * how new pixels are to be combined with the existing pixels on the graphics
	 * device during the rendering process.
	 * <p>
	 * If this Graphics2D context is drawing to a Component on the display screen
	 * and the Composite is a custom object rather than an instance of the
	 * AlphaComposite class, and if there is a security manager, its checkPermission
	 * method is called with an {@code AWTPermission("readDisplayPixels")}
	 * permission.
	 *
	 * @param comp the Composite object to be used for rendering
	 * @throws SecurityException if a custom Composite object is being used to
	 *         render to the screen and a security manager is set and its
	 *         checkPermission method does not allow the operation.
	 * @see java.awt.Graphics#setXORMode
	 * @see java.awt.Graphics#setPaintMode
	 * @see #getComposite
	 * @see AlphaComposite
	 * @see SecurityManager#checkPermission
	 * @see java.awt.AWTPermission
	 */
	public static void setComposite(final Composite comp) { g.setComposite(comp); }

	/**
	 * Sets the Paint attribute for the Graphics2D context. Calling this method with
	 * a {@code null Paint} object does not have any effect on the current Paint
	 * attribute of this Graphics2D.
	 *
	 * @param paint the Paint object to be used to generate color during the
	 *        rendering process, or null
	 * @see java.awt.Graphics#setColor
	 * @see #getPaint
	 * @see GradientPaint
	 * @see TexturePaint
	 */
	public static void setPaint(final Paint paint) { g.setPaint(paint); }

	/**
	 * Sets the value of a single preference for the rendering algorithms. Hint
	 * categories include controls for rendering quality and overall time/quality
	 * trade-off in the rendering process. Refer to the RenderingHints class for
	 * definitions of some common keys and values.
	 *
	 * @param hintKey the key of the hint to be set.
	 * @param hintValue the value indicating preferences for the specified hint
	 *        category.
	 * @see #getRenderingHint(RenderingHints.Key)
	 * @see RenderingHints
	 */
	public static void setRenderingHint(final Key hintKey, final Object hintValue) { g.setRenderingHint(hintKey, hintValue); }

	/**
	 * Replaces the values of all preferences for the rendering algorithms with the
	 * specified hints. The existing values for all rendering hints are discarded
	 * and the new set of known hints and values are initialized from the specified
	 * Map object. Hint categories include controls for rendering quality and
	 * overall time/quality trade-off in the rendering process. Refer to the
	 * RenderingHints class for definitions of some common keys and values.
	 *
	 * @param hints the rendering hints to be set
	 * @see #getRenderingHints
	 * @see RenderingHints
	 */
	public static void setRenderingHints(final Map<?, ?> hints) { g.setRenderingHints(hints); }

	/**
	 * Sets the Stroke for the Graphics2D context.
	 *
	 * @param s the Stroke object to be used to stroke a Shape during the rendering
	 *        process
	 * @see BasicStroke
	 * @see #getStroke
	 */
	public static void setStroke(final Stroke s) { g.setStroke(s); }

	/**
	 * Overwrites the Transform in the Graphics2D context. WARNING: This method
	 * should <b>never</b> be used to apply a new coordinate transform on top of an
	 * existing transform because the Graphics2D might already have a transform that
	 * is needed for other purposes, such as rendering Swing components or applying
	 * a scaling transformation to adjust for the resolution of a printer.
	 * <p>
	 * To add a coordinate transform, use the transform, rotate, scale, or shear
	 * methods. The setTransform method is intended only for restoring the original
	 * Graphics2D transform after rendering, as shown in this example:
	 *
	 * <pre>
	 * // Get the current transform
	 * AffineTransform saveAT = g2.getTransform();
	 * // Perform transformation
	 * g2d.transform(...);
	 * // Render
	 * g2d.draw(...);
	 * // Restore original transform
	 * g2d.setTransform(saveAT);
	 * </pre>
	 *
	 * @param Tx the AffineTransform that was retrieved from the getTransform method
	 * @see #transform
	 * @see #getTransform
	 * @see AffineTransform
	 */
	public static void setTransform(final AffineTransform Tx) { g.setTransform(Tx); }

	/**
	 * Concatenates the current Graphics2D Transform with a shearing transform.
	 * Subsequent renderings are sheared by the specified multiplier relative to the
	 * previous position. This is equivalent to calling {@code transform(SH)}, where
	 * SH is an AffineTransform represented by the following matrix:
	 *
	 * <pre>
	 *          [   1   shx   0   ]
	 *          [  shy   1    0   ]
	 *          [   0    0    1   ]
	 * </pre>
	 *
	 * @param shx the multiplier by which coordinates are shifted in the positive X
	 *        axis direction as a function of their Y coordinate
	 * @param shy the multiplier by which coordinates are shifted in the positive Y
	 *        axis direction as a function of their X coordinate
	 */
	public static void shear(final float shx, final float shy) { g.shear(shx, shy); }

	/**
	 * Composes an AffineTransform object with the Transform in this Graphics2D
	 * according to the rule last-specified-first-applied. If the current Transform
	 * is Cx, the result of composition with Tx is a new Transform Cx'. Cx' becomes
	 * the current Transform for this Graphics2D. Transforming a point p by the
	 * updated Transform Cx' is equivalent to first transforming p by Tx and then
	 * transforming the result by the original Transform Cx. In other words, Cx'(p)
	 * = Cx(Tx(p)). A copy of the Tx is made, if necessary, so further modifications
	 * to Tx do not affect rendering.
	 *
	 * @param Tx the AffineTransform object to be composed with the current
	 *        Transform
	 * @see #setTransform
	 * @see AffineTransform
	 */
	public static void transform(final AffineTransform Tx) { g.transform(Tx); }

	/**
	 * Translates the origin of the graphics context to the point
	 * (<i>x</i>,&nbsp;<i>y</i>) in the current coordinate system. Modifies this
	 * graphics context so that its new origin corresponds to the point
	 * (<i>x</i>,&nbsp;<i>y</i>) in this graphics context's original coordinate
	 * system. All coordinates used in subsequent rendering operations on this
	 * graphics context will be relative to this new origin.
	 *
	 * @param tx the <i>x</i> coordinate.
	 * @param ty the <i>y</i> coordinate.
	 */
	public static void translate(final float tx, final float ty) {
		var w = warp(tx, ty);
		g.translate(w[0], w[1]);
	}

	/**
	 * Returns a String object representing this Graphics object's value.
	 *
	 * @return a string representation of this graphics context.
	 */
	@Override
	public String toString() {
		return new StringBuilder(256).append("DoaGraphicsContext [\n\t{\n\t\tfont:").append(getFont()).append(",\n\t\tcolor:").append(getColor()).append(
		        "\n\t}\n]").toString();
	}

	/**
	 * Returns true if the specified rectangular area might intersect the current
	 * clipping area. The coordinates of the specified rectangular area are in the
	 * user coordinate space and are relative to the coordinate system origin of
	 * this graphics context. This method may use an algorithm that calculates a
	 * result quickly but which sometimes might return true even if the specified
	 * rectangular area does not intersect the clipping area. The specific algorithm
	 * employed may thus trade off accuracy for speed, but it will never return
	 * false unless it can guarantee that the specified rectangular area does not
	 * intersect the current clipping area. The clipping area used by this method
	 * can represent the intersection of the user clip as specified through the clip
	 * methods of this graphics context as well as the clipping associated with the
	 * device or image bounds and window visibility.
	 *
	 * @param x the x coordinate of the rectangle to test against the clip
	 * @param y the y coordinate of the rectangle to test against the clip
	 * @param width the width of the rectangle to test against the clip
	 * @param height the height of the rectangle to test against the clip
	 * @return true if the specified rectangle intersects the bounds of the current
	 *         clip; false otherwise.
	 */
	public static boolean hitClip(final float x, final float y, final float width, final float height) {
		var w = warp(x, y, width, height);
		return g.hitClip(w[0], w[1], w[2], w[3]);
	}

	/**
	 * Gets the font metrics of the current font.
	 *
	 * @return the font metrics of this graphics context's current font.
	 * @see java.awt.Graphics#getFont
	 * @see java.awt.FontMetrics
	 * @see java.awt.Graphics#getFontMetrics(Font)
	 */
	public static FontMetrics getFontMetrics() { return g.getFontMetrics(); }

	/**
	 * Returns the bounding rectangle of the current clipping area. The coordinates
	 * in the rectangle are relative to the coordinate system origin of this
	 * graphics context. This method differs from {@link #getClipBounds()
	 * getClipBounds} in that an existing rectangle is used instead of allocating a
	 * new one. This method refers to the user clip, which is independent of the
	 * clipping associated with device bounds and window visibility. If no clip has
	 * previously been set, or if the clip has been cleared using
	 * {@code setClip(null)}, this method returns the specified Rectangle.
	 *
	 * @param r the rectangle where the current clipping area is copied to. Any
	 *        current values in this rectangle are overwritten.
	 * @return the bounding rectangle of the current clipping area.
	 */
	public static Rectangle getClipBounds(final Rectangle r) { return g.getClipBounds(r); }

	/**
	 * Fills the polygon defined by the specified Polygon object with the graphics
	 * context's current color.
	 * <p>
	 * The area inside the polygon is defined using an even-odd fill rule, also
	 * known as the alternating rule.
	 *
	 * @param p the polygon to fill.
	 * @see java.awt.Graphics#drawPolygon(int[], int[], int)
	 */
	public static void fillPolygon(final Polygon p) { g.fillPolygon(p); }

	/**
	 * Paints a 3-D highlighted rectangle filled with the current color. The edges
	 * of the rectangle will be highlighted so that it appears as if the edges were
	 * beveled and lit from the upper left corner. The colors used for the
	 * highlighting effect will be determined from the current color.
	 *
	 * @param x the <i>x</i> coordinate of the rectangle to be filled.
	 * @param y the <i>y</i> coordinate of the rectangle to be filled.
	 * @param width the width of the rectangle to be filled.
	 * @param height the height of the rectangle to be filled.
	 * @param raised a boolean value that determines whether the rectangle appears
	 *        to be raised above the surface or etched into the surface.
	 * @see java.awt.Graphics#draw3DRect
	 */
	public static void fill3DRect(final float x, final float y, final float width, final float height, final boolean raised) {
		var w = warp(x, y, width, height);
		g.fill3DRect(w[0], w[1], w[2], w[3], raised);
	}

	/**
	 * Draws the outline of a polygon defined by the specified Polygon object.
	 *
	 * @param p the polygon to draw.
	 * @see java.awt.Graphics#fillPolygon
	 * @see java.awt.Graphics#drawPolyline
	 */
	public static void drawPolygon(final Polygon p) { g.drawPolygon(p); }

	/**
	 * Draws the text given by the specified character array, using this graphics
	 * context's current font and color. The baseline of the first character is at
	 * position (<i>x</i>,&nbsp;<i>y</i>) in this graphics context's coordinate
	 * system.
	 *
	 * @param data the array of characters to be drawn
	 * @param offset the start offset in the data
	 * @param length the number of characters to be drawn
	 * @param x the <i>x</i> coordinate of the baseline of the text
	 * @param y the <i>y</i> coordinate of the baseline of the text
	 * @throws NullPointerException if data is null.
	 * @throws IndexOutOfBoundsException if offset or length is less than zero, or
	 *         offset+length is greater than the length of the data array.
	 * @see java.awt.Graphics#drawBytes
	 * @see java.awt.Graphics#drawString
	 */
	public static void drawChars(final char[] data, final int offset, final int length, final float x, final float y) {
		var w = warp(x, y);
		g.drawChars(data, offset, length, w[0], w[1]);
	}

	/**
	 * Draws the text given by the specified byte array, using this graphics
	 * context's current font and color. The baseline of the first character is at
	 * position (<i>x</i>,&nbsp;<i>y</i>) in this graphics context's coordinate
	 * system.
	 * <p>
	 * Use of this method is not recommended as each byte is interpreted as a
	 * Unicode code point in the range 0 to 255, and so can only be used to draw
	 * Latin characters in that range.
	 *
	 * @param data the data to be drawn
	 * @param offset the start offset in the data
	 * @param length the number of bytes that are drawn
	 * @param x the <i>x</i> coordinate of the baseline of the text
	 * @param y the <i>y</i> coordinate of the baseline of the text
	 * @throws NullPointerException if data is null.
	 * @throws IndexOutOfBoundsException if offset or length is less than zero, or
	 *         offset+length is greater than the length of the data array.
	 * @see java.awt.Graphics#drawChars
	 * @see java.awt.Graphics#drawString
	 */
	public static void drawBytes(final byte[] data, final int offset, final int length, final float x, final float y) {
		var w = warp(x, y);
		g.drawBytes(data, offset, length, w[0], w[1]);
	}

	/**
	 * Draws a 3-D highlighted outline of the specified rectangle. The edges of the
	 * rectangle are highlighted so that they appear to be beveled and lit from the
	 * upper left corner.
	 * <p>
	 * The colors used for the highlighting effect are determined based on the
	 * current color. The resulting rectangle covers an area that is
	 * <code>width&nbsp;+&nbsp;1</code> pixels wide by
	 * <code>height&nbsp;+&nbsp;1</code> pixels tall.
	 *
	 * @param x the <i>x</i> coordinate of the rectangle to be drawn.
	 * @param y the <i>y</i> coordinate of the rectangle to be drawn.
	 * @param width the width of the rectangle to be drawn.
	 * @param height the height of the rectangle to be drawn.
	 * @param raised a boolean that determines whether the rectangle appears to be
	 *        raised above the surface or sunk into the surface.
	 * @see java.awt.Graphics#fill3DRect
	 */
	public static void draw3DRect(final float x, final float y, final float width, final float height, final boolean raised) {
		var w = warp(x, y, width, height);
		g.draw3DRect(w[0], w[1], w[2], w[3], raised);
	}

	/**
	 * @hidden
	 */
	@Internal
	public static void turnOnLightContribution() { lightsShouldContribute = true; }

	/**
	 * @hidden
	 */
	@Internal
	public static void turnOffLightContribution() { lightsShouldContribute = false; }

	/**
	 * @hidden
	 */
	@Internal
	public static boolean areLightsContributing() { return lightsShouldContribute; }

	/**
	 * @hidden
	 */
	@Internal
	public static void zoomToLookAt() {
		if (DoaCamera.isMouseZoomingEnabled()) {
			final DoaObject centerOfZoom = DoaCamera.getObjectToZoomInto();
			if (centerOfZoom != null) {
				final DoaTransform transform = centerOfZoom.transform;
				g.translate(DoaCamera.getX() + transform.position.x, DoaCamera.getY() + transform.position.y);
				g.scale(DoaCamera.getZ(), DoaCamera.getZ());
				g.translate(-DoaCamera.getX() - transform.position.x, -DoaCamera.getY() - transform.position.y);
			} else {
				final int width = actualResolution.width;
				final int height = actualResolution.height;
				g.translate(DoaCamera.getX() + width / 2.0, DoaCamera.getY() + height / 2.0);
				g.scale(DoaCamera.getZ(), DoaCamera.getZ());
				g.translate(-DoaCamera.getX() - width / 2.0, -DoaCamera.getY() - height / 2.0);
			}
		}
	}

	/**
	 * Calls each of the reset methods available at once.
	 */
	public static void resetAll() {
		resetTransform();
		resetClip();
		resetComposite();
		resetStroke();
		resetColor();
	}

	/**
	 * Calls each of the push methods available at once.
	 */
	public static void pushAll() {
		pushTransform();
		pushClip();
		pushComposite();
		pushStroke();
		pushColor();
	}

	/**
	 * Calls each of the pop methods available at once.
	 */
	public static void popAll() {
		popTransform();
		popClip();
		popComposite();
		popStroke();
		popColor();
	}

	/**
	 * Sets the transform applied to the context to unit transform. A call to this
	 * method is equivalent to {@code g.setTransform(new AffineTransform())}.
	 */
	public static void resetTransform() { g.setTransform(new AffineTransform()); }

	/**
	 * Pushes the current transform into a stack of transforms. Informally, saving
	 * it. A subsequent call to {@link #popTransform()} will restore the last
	 * transform pushed into the said stack.
	 * 
	 * <pre>
	 * 		Transform oldTranform = g.getTransform();
	 * 		...
	 * 		...
	 *		g.setTransfom(oldTransform);
	 * </pre>
	 * 
	 * is equivalent to
	 * 
	 * <pre>
	 * 		g.pushTransform();
	 * 		...
	 * 		...
	 *		g.popTransfom();
	 * </pre>
	 */
	public static void pushTransform() { transforms.push(getTransform()); }

	/**
	 * Pops the top-most transform from the stack of transforms. Informally,
	 * restoring it. A call to this method will restore the last transform pushed
	 * into the said stack. The popped transform will replace the current transform.
	 * 
	 * <pre>
	 * 		Transform oldTranform = g.getTransform();
	 * 		...
	 * 		...
	 *		g.setTransfom(oldTransform);
	 * </pre>
	 * 
	 * is equivalent to
	 * 
	 * <pre>
	 * 		g.pushTransform();
	 * 		...
	 * 		...
	 *		g.popTransfom();
	 * </pre>
	 */
	public static void popTransform() { setTransform(transforms.pop()); }

	/**
	 * Sets the applied composite to the default composite. A call to this method is
	 * equivalent to
	 * {@code g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER))}.
	 */
	public static void resetComposite() { g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER)); }

	/**
	 * Pushes the current composite into a stack composites. Informally, saving it.
	 * A subsequent call to {@link #popComposite()} will restore the last composite
	 * pushed into the said stack.
	 * 
	 * <pre>
	 * 		Composite oldComposite = g.getComposite();
	 * 		...
	 * 		...
	 *		g.setTransfom(oldComposite);
	 * </pre>
	 * 
	 * is equivalent to
	 * 
	 * <pre>
	 * 		g.pushComposite();
	 * 		...
	 * 		...
	 *		g.popComposite();
	 * </pre>
	 */
	public static void pushComposite() { composites.push(getComposite()); }

	/**
	 * Pops the top-most composite from the composites stack. Informally, restoring
	 * it. A call to this method will restore the last composite pushed into the
	 * said stack. The popped composite will replace the current composite.
	 * 
	 * <pre>
	 * 		Composite oldComposite = g.getComposite();
	 * 		...
	 * 		...
	 *		g.setTransfom(oldComposite);
	 * </pre>
	 * 
	 * is equivalent to
	 * 
	 * <pre>
	 * 		g.pushComposite();
	 * 		...
	 * 		...
	 *		g.popComposite();
	 * </pre>
	 */
	public static void popComposite() { setComposite(composites.pop()); }

	/**
	 * Sets the applied clip to the default clip. A call to this method is
	 * equivalent to {@code g.setClip(0, 0, RESOLUTION.width, RESOLUTION.height)}.
	 */
	public static void resetClip() { g.setClip(0, 0, actualResolution.width, actualResolution.height); }

	/**
	 * Pushes the current clip into a stack clips. Informally, saving it. A
	 * subsequent call to {@link #popClip()} will restore the last clip pushed into
	 * the said stack.
	 * 
	 * <pre>
	 * 		Clip oldClip = g.getClip();
	 * 		...
	 * 		...
	 *		g.setClip(oldClip);
	 * </pre>
	 * 
	 * is equivalent to
	 * 
	 * <pre>
	 * 		g.pushClip();
	 * 		...
	 * 		...
	 *		g.popClip();
	 * </pre>
	 */
	public static void pushClip() { clips.push(getClip()); }

	/**
	 * Pops the top-most clip from the stack of clips. Informally, restoring it. A
	 * call to this method will restore the last clip pushed into the said stack.
	 * The popped clip will replace the current clip.
	 * 
	 * <pre>
	 * 		Clip oldClip = g.getClip();
	 * 		...
	 * 		...
	 *		g.setClip(oldClip);
	 * </pre>
	 * 
	 * is equivalent to
	 * 
	 * <pre>
	 * 		g.pushClip();
	 * 		...
	 * 		...
	 *		g.popClip();
	 * </pre>
	 */
	public static void popClip() { setClip(clips.pop()); }

	/**
	 * Sets the applied stroke to the default stroke. A call to this method is
	 * equivalent to {@code g.setStroke(new BasicStroke(1))}.
	 */
	public static void resetStroke() { g.setStroke(new BasicStroke(1)); }

	/**
	 * Pushes the current stroke into a stack of strokes. Informally, saving it. A
	 * subsequent call to {@link #popStroke()} will restore the last stroke pushed
	 * into the said stack.
	 * 
	 * <pre>
	 * 		Stroke oldStroke = g.getStroke();
	 * 		...
	 * 		...
	 *		g.setStroke(oldStroke);
	 * </pre>
	 * 
	 * is equivalent to
	 * 
	 * <pre>
	 * 		g.pushStroke();
	 * 		...
	 * 		...
	 *		g.popStroke();
	 * </pre>
	 */
	public static void pushStroke() { strokes.push(getStroke()); }

	/**
	 * Pops the top-most stroke from the strokes stack. Informally, restoring it. A
	 * call to this method will restore the last stroke pushed into the said stack.
	 * The popped stroke will replace the current stroke.
	 * 
	 * <pre>
	 * 		Stroke oldStroke = g.getStroke();
	 * 		...
	 * 		...
	 *		g.setStroke(oldStroke);
	 * </pre>
	 * 
	 * is equivalent to
	 * 
	 * <pre>
	 * 		g.pushStroke();
	 * 		...
	 * 		...
	 *		g.popStroke();
	 * </pre>
	 */
	public static void popStroke() { setStroke(strokes.pop()); }

	/**
	 * Sets the applied color to black. A call to this method is equivalent to
	 * {@code setColor(Color.BLACK)}.
	 */
	public static void resetColor() { setColor(Color.BLACK); }

	/**
	 * Pushes the current color into a stack of colors. Informally, saving it. A
	 * subsequent call to {@link #popColor()} will restore the last color pushed
	 * into the said stack.
	 * 
	 * <pre>
	 * 		Color oldColor = getColor();
	 * 		...
	 * 		...
	 *		setColor(oldColor);
	 * </pre>
	 * 
	 * is equivalent to
	 * 
	 * <pre>
	 * 		pushColor();
	 * 		...
	 * 		...
	 *		popColor();
	 * </pre>
	 */
	public static void pushColor() { colors.push(getColor()); }

	/**
	 * Pops the top-most color from the colors stack. Informally, restoring it. A
	 * call to this method will restore the last colors pushed into the said stack.
	 * The popped color will replace the current color.
	 * 
	 * <pre>
	 * 		Color oldColor = getColor();
	 * 		...
	 * 		...
	 *		setColor(oldColor);
	 * </pre>
	 * 
	 * is equivalent to
	 * 
	 * <pre>
	 * 		pushColor();
	 * 		...
	 * 		...
	 *		popColor();
	 * </pre>
	 */
	public static void popColor() { setColor(colors.pop()); }

	static void set(Graphics2D drawGraphics, Dimension refResolution, Dimension actResolution) {
		g = drawGraphics;
		referenceResolution = refResolution;
		actualResolution = actResolution;
	}

	private static int[] warpX(float[] xs) {
		var rv = new int[xs.length];
		var xsint = floatArrToIntArr(xs);
		for (int i = 0; i < xsint.length; i++) {
			rv[i] = xsint[i] / referenceResolution.width * actualResolution.width;
		}
		return rv;
	}

	private static int[] warpY(float[] ys) {
		var rv = new int[ys.length];
		var ysint = floatArrToIntArr(ys);
		for (int i = 0; i < ysint.length; i++) {
			rv[i] = ysint[i] / referenceResolution.height * actualResolution.height;
		}
		return rv;
	}

	private static int[] warp(float x, float y) {
		var rv = new int[4];
		rv[0] = (int) (x / referenceResolution.width * actualResolution.width);
		rv[1] = (int) (y / referenceResolution.height * actualResolution.height);
		return rv;
	}

	private static int[] warp(float x, float y, float w, float h) {
		var rv = new int[4];
		rv[0] = (int) (x / referenceResolution.width * actualResolution.width);
		rv[1] = (int) (y / referenceResolution.height * actualResolution.height);
		rv[2] = (int) (w / referenceResolution.width * actualResolution.width);
		rv[3] = (int) (h / referenceResolution.height * actualResolution.height);
		return rv;
	}

	private static int[] floatArrToIntArr(final float[] arrToBeConverted) {
		final int[] rv = new int[arrToBeConverted.length];
		for (int i = 0; i < arrToBeConverted.length; i++) {
			rv[i] = (int) arrToBeConverted[i];
		}
		return rv;
	}
}
