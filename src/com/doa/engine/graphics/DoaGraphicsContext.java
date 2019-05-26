package com.doa.engine.graphics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
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
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import com.doa.engine.DoaCamera;
import com.doa.engine.DoaObject;
import com.doa.engine.DoaWindow;
import com.doa.engine.task.DoaTaskGuard;
import com.doa.engine.task.DoaTasker;

/**
 * Responsible for wrapping the {@code Graphics2D} class Sun Microsystems
 * provides. All and every method this class provides behaves exactly the same
 * as documented in {@code Graphics}, {@code Graphics2D} and {@code DoaSprites}.
 * Refer to these 3 class documentations for this class' behaviour.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.1
 * @version 2.5
 * @see java.awt.Graphics
 * @see java.awt.Graphics2D
 * @see com.doa.engine.graphics.DoaSprites
 * @see com.doa.engine.graphics.DoaAnimation
 * @see com.doa.engine.graphics.DoaAnimations
 */
public final class DoaGraphicsContext {

	private static final Map<DoaAnimation, DoaTaskGuard> animationGuards = new HashMap<>();

	private Graphics2D g = null;
	private boolean ligtsShouldContribute = true;

	private Deque<AffineTransform> transforms = new ArrayDeque<>();
	private Deque<Composite> composites = new ArrayDeque<>();
	private Deque<Shape> clips = new ArrayDeque<>();

	/**
	 * Constructs a new {@code Graphics} object. This constructor is the default
	 * constructor for a graphics context.
	 * <p>
	 * Since {@code Graphics} is an abstract class, applications cannot call this
	 * constructor directly. Graphics contexts are obtained from other graphics
	 * contexts or are created by calling {@code getGraphics} on a component.
	 * 
	 * @param g Graphics2D object
	 * @see java.awt.Graphics#create()
	 * @see java.awt.Component#getGraphics
	 */
	public DoaGraphicsContext(final Graphics2D g) {
		this.g = g;
	}

	/**
	 * Clears the specified rectangle by filling it with the background color of the
	 * current drawing surface. This operation does not use the current paint mode.
	 * <p>
	 * Beginning with Java&nbsp;1.1, the background color of offscreen images may be
	 * system dependent. Applications should use {@code setColor} followed by
	 * {@code fillRect} to ensure that an offscreen image is cleared to a specific
	 * color.
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
	public void clearRect(final double x, final double y, final double width, final double height) {
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
	 * @see #setClip(double, double, double, double)
	 * @see #setClip(Shape)
	 */
	public void clipRect(final double x, final double y, final double width, final double height) {
		g.clipRect((int) x, (int) y, (int) width, (int) height);
	}

	/**
	 * Copies an area of the component by a distance specified by {@code dx} and
	 * {@code dy}. From the point specified by {@code x} and {@code y}, this method
	 * copies downwards and to the right. To copy an area of the component to the
	 * left or upwards, specify a negative value for {@code dx} or {@code dy}. If a
	 * portion of the source rectangle lies outside the bounds of the component, or
	 * is obscured by another window or component, {@code copyArea} will be unable
	 * to copy the associated pixels. The area that is omitted can be refreshed by
	 * calling the component's {@code paint} method.
	 * 
	 * @param x the <i>x</i> coordinate of the source rectangle.
	 * @param y the <i>y</i> coordinate of the source rectangle.
	 * @param width the width of the source rectangle.
	 * @param height the height of the source rectangle.
	 * @param dx the horizontal distance to copy the pixels.
	 * @param dy the vertical distance to copy the pixels.
	 */
	public void copyArea(final double x, final double y, final double width, final double height, final double dx, final double dy) {
		g.copyArea((int) x, (int) y, (int) width, (int) height, (int) dx, (int) dy);
	}

	/**
	 * Creates a new {@code Graphics} object that is a copy of this {@code Graphics}
	 * object.
	 * 
	 * @return a new graphics context that is a copy of this graphics context.
	 */
	public Graphics create() {
		return g.create();
	}

	/**
	 * Creates a new {@code Graphics} object based on this {@code Graphics} object,
	 * but with a new translation and clip area. The new {@code Graphics} object has
	 * its origin translated to the specified point (<i>x</i>,&nbsp;<i>y</i>). Its
	 * clip area is determined by the intersection of the original clip area with
	 * the specified rectangle. The arguments are all interpreted in the coordinate
	 * system of the original {@code Graphics} object. The new graphics context is
	 * identical to the original, except in two respects:
	 * <ul>
	 * <li>The new graphics context is translated by (<i>x</i>,&nbsp;<i>y</i>). That
	 * is to say, the point ({@code 0},&nbsp;{@code 0}) in the new graphics context
	 * is the same as (<i>x</i>,&nbsp;<i>y</i>) in the original graphics context.
	 * <li>The new graphics context has an additional clipping rectangle, in
	 * addition to whatever (translated) clipping rectangle it inherited from the
	 * original graphics context. The origin of the new clipping rectangle is at
	 * ({@code 0},&nbsp;{@code 0}), and its size is specified by the {@code width}
	 * and {@code height} arguments.
	 * </ul>
	 *
	 * @param x the <i>x</i> coordinate.
	 * @param y the <i>y</i> coordinate.
	 * @param width the width of the clipping rectangle.
	 * @param height the height of the clipping rectangle.
	 * @return a new graphics context.
	 * @see java.awt.Graphics#translate
	 * @see java.awt.Graphics#clipRect
	 */
	public Graphics create(final double x, final double y, final double width, final double height) {
		return g.create((int) x, (int) y, (int) width, (int) height);
	}

	/**
	 * Disposes of this graphics context and releases any system resources that it
	 * is using. A {@code Graphics} object cannot be used after {@code dispose} has
	 * been called.
	 * <p>
	 * When a Java program runs, a large number of {@code Graphics} objects can be
	 * created within a short time frame. Although the finalization process of the
	 * garbage collector also disposes of the same system resources, it is
	 * preferable to manually free the associated resources by calling this method
	 * rather than to rely on a finalization process which may not run to completion
	 * for a long period of time.
	 * <p>
	 * Graphics objects which are provided as arguments to the {@code paint} and
	 * {@code update} methods of components are automatically released by the system
	 * when those methods return. For efficiency, programmers should call
	 * {@code dispose} when finished using a {@code Graphics} object only if it was
	 * created directly from a component or another {@code Graphics} object.
	 * 
	 * @see java.awt.Graphics#finalize
	 * @see java.awt.Component#paint
	 * @see java.awt.Component#update
	 * @see java.awt.Component#getGraphics
	 * @see java.awt.Graphics#create
	 */
	public void dispose() {
		g.dispose();
	}

	public void drawAnimation(final DoaAnimation anim, final double x, final double y, final double width, final double height) {
		animationGuards.computeIfAbsent(anim, k -> new DoaTaskGuard());
		DoaTaskGuard guard = animationGuards.get(anim);
		if (guard.get()) {
			DoaTasker.guard(guard, anim.getDelay());
			drawImage(anim.next(), x, y, width, height);
		} else {
			drawImage(anim.current(), x, y, width, height);
		}
	}

	public void drawAnimation(final DoaAnimation anim, final double x, final double y) {
		animationGuards.computeIfAbsent(anim, k -> new DoaTaskGuard());
		DoaTaskGuard guard = animationGuards.get(anim);
		if (guard.get()) {
			DoaTasker.guard(guard, anim.getDelay());
			BufferedImage spr = anim.next();
			drawImage(spr, x, y, spr.getWidth(), spr.getHeight());
		} else {
			BufferedImage spr = anim.current();
			drawImage(spr, x, y, spr.getWidth(), spr.getHeight());
		}
	}

	/**
	 * Draws the outline of a circular or elliptical arc covering the specified
	 * rectangle.
	 * <p>
	 * The resulting arc begins at {@code startAngle} and extends for
	 * {@code arcAngle} degrees, using the current color. Angles are interpreted
	 * such that 0&nbsp;degrees is at the 3&nbsp;o'clock position. A positive value
	 * indicates a counter-clockwise rotation while a negative value indicates a
	 * clockwise rotation.
	 * <p>
	 * The center of the arc is the center of the rectangle whose origin is
	 * (<i>x</i>,&nbsp;<i>y</i>) and whose size is specified by the {@code width}
	 * and {@code height} arguments.
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
	public void drawArc(final double x, final double y, final double width, final double height, final double startAngle, final double arcAngle) {
		g.drawArc((int) x, (int) y, (int) width, (int) height, (int) startAngle, (int) arcAngle);
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
	 * changed, then {@code drawImage} returns {@code true}. Otherwise,
	 * {@code drawImage} returns {@code false} and as more of the image becomes
	 * available or it is time to draw another frame of animation, the process that
	 * loads the image notifies the specified image observer.
	 * 
	 * @param img the specified image to be drawn. This method does nothing if
	 *        {@code img} is null.
	 * @param x the <i>x</i> coordinate.
	 * @param y the <i>y</i> coordinate.
	 * @return {@code false} if the image pixels are still changing; {@code true}
	 *         otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int,
	 *      int, int)
	 */
	public boolean drawImage(final Image img, final double x, final double y) {
		return g.drawImage(img, (int) x, (int) y, null);
	}

	/**
	 * Draws as much of the specified image as is currently available. The image is
	 * drawn with its top-left corner at (<i>x</i>,&nbsp;<i>y</i>) in this graphics
	 * context's coordinate space. Transparent pixels are drawn in the specified
	 * background color.
	 * <p>
	 * This operation is equivalent to filling a rectangle of the width and height
	 * of the specified image with the given color and then drawing the image on top
	 * of it, but possibly more efficient.
	 * <p>
	 * This method returns immediately in all cases, even if the complete image has
	 * not yet been loaded, and it has not been dithered and converted for the
	 * current output device.
	 * <p>
	 * If the image has completely loaded and its pixels are no longer being
	 * changed, then {@code drawImage} returns {@code true}. Otherwise,
	 * {@code drawImage} returns {@code false} and as more of the image becomes
	 * available or it is time to draw another frame of animation, the process that
	 * loads the image notifies the specified image observer.
	 * 
	 * @param img the specified image to be drawn. This method does nothing if
	 *        {@code img} is null.
	 * @param x the <i>x</i> coordinate.
	 * @param y the <i>y</i> coordinate.
	 * @param bgcolor the background color to paint under the non-opaque portions of
	 *        the image.
	 * @return {@code false} if the image pixels are still changing; {@code true}
	 *         otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int,
	 *      int, int)
	 */
	public boolean drawImage(final Image img, final double x, final double y, final Color bgcolor) {
		return g.drawImage(img, (int) x, (int) y, bgcolor, null);
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
	 * If the current output representation is not yet complete, then
	 * {@code drawImage} returns {@code false}. As more of the image becomes
	 * available, the process that loads the image notifies the image observer by
	 * calling its {@code imageUpdate} method.
	 * <p>
	 * A scaled version of an image will not necessarily be available immediately
	 * just because an unscaled version of the image has been constructed for this
	 * output device. Each size of the image may be cached separately and generated
	 * from the original data in a separate image production sequence.
	 * 
	 * @param img the specified image to be drawn. This method does nothing if
	 *        {@code img} is null.
	 * @param x the <i>x</i> coordinate.
	 * @param y the <i>y</i> coordinate.
	 * @param width the width of the rectangle.
	 * @param height the height of the rectangle.
	 * @return {@code false} if the image pixels are still changing; {@code true}
	 *         otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int,
	 *      int, int)
	 */
	public boolean drawImage(final Image img, final double x, final double y, final double width, final double height) {
		return g.drawImage(img, (int) x, (int) y, (int) width, (int) height, null);
	}

	/**
	 * Draws as much of the specified image as has already been scaled to fit inside
	 * the specified rectangle.
	 * <p>
	 * The image is drawn inside the specified rectangle of this graphics context's
	 * coordinate space, and is scaled if necessary. Transparent pixels are drawn in
	 * the specified background color. This operation is equivalent to filling a
	 * rectangle of the width and height of the specified image with the given color
	 * and then drawing the image on top of it, but possibly more efficient.
	 * <p>
	 * This method returns immediately in all cases, even if the entire image has
	 * not yet been scaled, dithered, and converted for the current output device.
	 * If the current output representation is not yet complete then
	 * {@code drawImage} returns {@code false}. As more of the image becomes
	 * available, the process that loads the image notifies the specified image
	 * observer.
	 * <p>
	 * A scaled version of an image will not necessarily be available immediately
	 * just because an unscaled version of the image has been constructed for this
	 * output device. Each size of the image may be cached separately and generated
	 * from the original data in a separate image production sequence.
	 * 
	 * @param img the specified image to be drawn. This method does nothing if
	 *        {@code img} is null.
	 * @param x the <i>x</i> coordinate.
	 * @param y the <i>y</i> coordinate.
	 * @param width the width of the rectangle.
	 * @param height the height of the rectangle.
	 * @param bgcolor the background color to paint under the non-opaque portions of
	 *        the image.
	 * @return {@code false} if the image pixels are still changing; {@code true}
	 *         otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int,
	 *      int, int)
	 */
	public boolean drawImage(final Image img, final double x, final double y, final double width, final double height, final Color bgcolor) {
		return g.drawImage(img, (int) x, (int) y, (int) width, (int) height, bgcolor, null);
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
	 * {@code drawImage} returns {@code false}. As more of the image becomes
	 * available, the process that loads the image notifies the specified image
	 * observer.
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
	 * @param img the specified image to be drawn. This method does nothing if
	 *        {@code img} is null.
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
	 * @return {@code false} if the image pixels are still changing; {@code true}
	 *         otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int,
	 *      int, int)
	 * @since 1.1
	 */
	public boolean drawImage(final Image img, final double dx1, final double dy1, final double dx2, final double dy2, final double sx1, final double sy1,
	        final double sx2, final double sy2)
	{
		return g.drawImage(img, (int) dx1, (int) dy1, (int) dx2, (int) dy2, (int) sx1, (int) sy1, (int) sx2, (int) sy2, null);
	}

	/**
	 * Draws as much of the specified area of the specified image as is currently
	 * available, scaling it on the fly to fit inside the specified area of the
	 * destination drawable surface.
	 * <p>
	 * Transparent pixels are drawn in the specified background color. This
	 * operation is equivalent to filling a rectangle of the width and height of the
	 * specified image with the given color and then drawing the image on top of it,
	 * but possibly more efficient.
	 * <p>
	 * This method returns immediately in all cases, even if the image area to be
	 * drawn has not yet been scaled, dithered, and converted for the current output
	 * device. If the current output representation is not yet complete then
	 * {@code drawImage} returns {@code false}. As more of the image becomes
	 * available, the process that loads the image notifies the specified image
	 * observer.
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
	 * @param img the specified image to be drawn. This method does nothing if
	 *        {@code img} is null.
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
	 * @param bgcolor the background color to paint under the non-opaque portions of
	 *        the image.
	 * @return {@code false} if the image pixels are still changing; {@code true}
	 *         otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int,
	 *      int, int)
	 * @since 1.1
	 */
	public boolean drawImage(final Image img, final double dx1, final double dy1, final double dx2, final double dy2, final double sx1, final double sy1,
	        final double sx2, final double sy2, final Color bgcolor)
	{
		return g.drawImage(img, (int) dx1, (int) dy1, (int) dx2, (int) dy2, (int) sx1, (int) sy1, (int) sx2, (int) sy2, bgcolor, null);
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
	public void drawLine(final double x1, final double y1, final double x2, final double y2) {
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	/**
	 * Draws the outline of an oval. The result is a circle or ellipse that fits
	 * within the rectangle specified by the {@code x}, {@code y}, {@code width},
	 * and {@code height} arguments.
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
	public void drawOval(final double x, final double y, final double width, final double height) {
		g.drawOval((int) x, (int) y, (int) width, (int) height);
	}

	/**
	 * Draws a closed polygon defined by arrays of <i>x</i> and <i>y</i>
	 * coordinates. Each pair of (<i>x</i>,&nbsp;<i>y</i>) coordinates defines a
	 * point.
	 * <p>
	 * This method draws the polygon defined by {@code nPoint} line segments, where
	 * the first <code>nPoint&nbsp;-&nbsp;1</code> line segments are line segments
	 * from <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code>
	 * to <code>(xPoints[i],&nbsp;yPoints[i])</code>, for
	 * 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;{@code nPoints}. The figure is
	 * automatically closed by drawing a line connecting the final point to the
	 * first point, if those points are different.
	 * 
	 * @param xPoints a an array of {@code x} coordinates.
	 * @param yPoints a an array of {@code y} coordinates.
	 * @param nPoints a the total number of points.
	 * @see java.awt.Graphics#fillPolygon
	 * @see java.awt.Graphics#drawPolyline
	 */
	public void drawPolygon(final double[] xPoints, final double[] yPoints, final int nPoints) {
		g.drawPolygon(doubleArrToIntArr(xPoints), doubleArrToIntArr(yPoints), nPoints);
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
	public void drawPolyline(final double[] xPoints, final double[] yPoints, final int nPoints) {
		g.drawPolyline(doubleArrToIntArr(xPoints), doubleArrToIntArr(yPoints), nPoints);
	}

	/**
	 * Draws an outlined round-cornered rectangle using this graphics context's
	 * current color. The left and right edges of the rectangle are at {@code x} and
	 * <code>x&nbsp;+&nbsp;width</code>, respectively. The top and bottom edges of
	 * the rectangle are at {@code y} and <code>y&nbsp;+&nbsp;height</code>.
	 * 
	 * @param x the <i>x</i> coordinate of the rectangle to be drawn.
	 * @param y the <i>y</i> coordinate of the rectangle to be drawn.
	 * @param width the width of the rectangle to be drawn.
	 * @param height the height of the rectangle to be drawn.
	 * @param arcWidth the horizontal diameter of the arc at the four corners.
	 * @param arcHeight the vertical diameter of the arc at the four corners.
	 * @see java.awt.Graphics#fillRoundRect
	 */
	public void drawRoundRect(final double x, final double y, final double width, final double height, final double arcWidth, final double arcHeight) {
		g.drawRoundRect((int) x, (int) y, (int) width, (int) height, (int) arcWidth, (int) arcHeight);
	}

	/**
	 * Fills a circular or elliptical arc covering the specified rectangle.
	 * <p>
	 * The resulting arc begins at {@code startAngle} and extends for
	 * {@code arcAngle} degrees. Angles are interpreted such that 0&nbsp;degrees is
	 * at the 3&nbsp;o'clock position. A positive value indicates a
	 * counter-clockwise rotation while a negative value indicates a clockwise
	 * rotation.
	 * <p>
	 * The center of the arc is the center of the rectangle whose origin is
	 * (<i>x</i>,&nbsp;<i>y</i>) and whose size is specified by the {@code width}
	 * and {@code height} arguments.
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
	public void fillArc(final double x, final double y, final double width, final double height, final double startAngle, final double arcAngle) {
		g.fillArc((int) x, (int) y, (int) width, (int) height, (int) startAngle, (int) arcAngle);
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
	public void fillOval(final double x, final double y, final double width, final double height) {
		g.fillOval((int) x, (int) y, (int) width, (int) height);
	}

	/**
	 * Fills a closed polygon defined by arrays of <i>x</i> and <i>y</i>
	 * coordinates.
	 * <p>
	 * This method draws the polygon defined by {@code nPoint} line segments, where
	 * the first <code>nPoint&nbsp;-&nbsp;1</code> line segments are line segments
	 * from <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code>
	 * to <code>(xPoints[i],&nbsp;yPoints[i])</code>, for
	 * 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;{@code nPoints}. The figure is
	 * automatically closed by drawing a line connecting the final point to the
	 * first point, if those points are different.
	 * <p>
	 * The area inside the polygon is defined using an even-odd fill rule, also
	 * known as the alternating rule.
	 * 
	 * @param xPoints a an array of {@code x} coordinates.
	 * @param yPoints a an array of {@code y} coordinates.
	 * @param nPoints a the total number of points.
	 * @see java.awt.Graphics#drawPolygon(int[], int[], int)
	 */
	public void fillPolygon(final double[] xPoints, final double[] yPoints, final int nPoints) {
		g.fillPolygon(doubleArrToIntArr(xPoints), doubleArrToIntArr(yPoints), nPoints);
	}

	/**
	 * Fills the specified rectangle. The left and right edges of the rectangle are
	 * at {@code x} and <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>. The top and
	 * bottom edges are at {@code y} and
	 * <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>. The resulting rectangle
	 * covers an area {@code width} pixels wide by {@code height} pixels tall. The
	 * rectangle is filled using the graphics context's current color.
	 * 
	 * @param x the <i>x</i> coordinate of the rectangle to be filled.
	 * @param y the <i>y</i> coordinate of the rectangle to be filled.
	 * @param width the width of the rectangle to be filled.
	 * @param height the height of the rectangle to be filled.
	 * @see java.awt.Graphics#clearRect
	 * @see java.awt.Graphics#drawRect
	 */
	public void fillRect(final double x, final double y, final double width, final double height) {
		g.fillRect((int) x, (int) y, (int) width, (int) height);
	}

	/**
	 * Fills the specified rounded corner rectangle with the current color. The left
	 * and right edges of the rectangle are at {@code x} and
	 * <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>, respectively. The top and
	 * bottom edges of the rectangle are at {@code y} and
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
	public void fillRoundRect(final double x, final double y, final double width, final double height, final double arcWidth, final double arcHeight) {
		g.fillRoundRect((int) x, (int) y, (int) width, (int) height, (int) arcWidth, (int) arcHeight);
	}

	/**
	 * Gets the current clipping area. This method returns the user clip, which is
	 * independent of the clipping associated with device bounds and window
	 * visibility. If no clip has previously been set, or if the clip has been
	 * cleared using {@code setClip(null)}, this method returns {@code null}.
	 * 
	 * @return a {@code Shape} object representing the current clipping area, or
	 *         {@code null} if no clip is set.
	 * @see java.awt.Graphics#getClipBounds
	 * @see java.awt.Graphics#clipRect
	 * @see java.awt.Graphics#setClip(int, int, int, int)
	 * @see java.awt.Graphics#setClip(Shape)
	 * @since 1.1
	 */
	public Shape getClip() {
		return g.getClip();
	}

	/**
	 * Returns the bounding rectangle of the current clipping area. This method
	 * refers to the user clip, which is independent of the clipping associated with
	 * device bounds and window visibility. If no clip has previously been set, or
	 * if the clip has been cleared using {@code setClip(null)}, this method returns
	 * {@code null}. The coordinates in the rectangle are relative to the coordinate
	 * system origin of this graphics context.
	 * 
	 * @return the bounding rectangle of the current clipping area, or {@code null}
	 *         if no clip is set.
	 * @see java.awt.Graphics#getClip
	 * @see java.awt.Graphics#clipRect
	 * @see java.awt.Graphics#setClip(int, int, int, int)
	 * @see java.awt.Graphics#setClip(Shape)
	 * @since 1.1
	 */
	public Rectangle getClipBounds() {
		return g.getClipBounds();
	}

	/**
	 * Gets this graphics context's current color.
	 * 
	 * @return this graphics context's current color.
	 * @see java.awt.Color
	 * @see java.awt.Graphics#setColor(Color)
	 */
	public Color getColor() {
		return g.getColor();
	}

	/**
	 * Gets the current font.
	 * 
	 * @return this graphics context's current font.
	 * @see java.awt.Font
	 * @see java.awt.Graphics#setFont(Font)
	 */
	public Font getFont() {
		return g.getFont();
	}

	/**
	 * Gets the font metrics for the specified font.
	 * 
	 * @return the font metrics for the specified font.
	 * @param f the specified font
	 * @see java.awt.Graphics#getFont
	 * @see java.awt.FontMetrics
	 * @see java.awt.Graphics#getFontMetrics()
	 */
	public FontMetrics getFontMetrics(final Font f) {
		return g.getFontMetrics(f);
	}

	/**
	 * Sets the current clipping area to an arbitrary clip shape. Not all objects
	 * that implement the {@code Shape} interface can be used to set the clip. The
	 * only {@code Shape} objects that are guaranteed to be supported are
	 * {@code Shape} objects that are obtained via the {@code getClip} method and
	 * via {@code Rectangle} objects. This method sets the user clip, which is
	 * independent of the clipping associated with device bounds and window
	 * visibility.
	 * 
	 * @param clip the {@code Shape} to use to set the clip
	 * @see java.awt.Graphics#getClip()
	 * @see java.awt.Graphics#clipRect
	 * @see java.awt.Graphics#setClip(int, int, int, int)
	 * @since 1.1
	 */
	public void setClip(final Shape clip) {
		g.setClip(clip);
	}

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
	public void setClip(final double x, final double y, final double width, final double height) {
		g.setClip((int) x, (int) y, (int) width, (int) height);
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
	public void setColor(final Color c) {
		if (ligtsShouldContribute) {
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
	public void setFont(final Font font) {
		g.setFont(font);
	}

	/**
	 * Sets the paint mode of this graphics context to overwrite the destination
	 * with this graphics context's current color. This sets the logical pixel
	 * operation function to the paint or overwrite mode. All subsequent rendering
	 * operations will overwrite the destination with the current color.
	 */
	public void setPaintMode() {
		g.setPaintMode();
	}

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
	public void setXORMode(final Color c1) {
		g.setXORMode(c1);
	}

	public void addRenderingHints(final Map<?, ?> hints) {
		g.addRenderingHints(hints);
	}

	public void clip(final Shape s) {
		g.clip(s);
	}

	public void draw(final Shape s) {
		g.draw(s);
	}

	public void drawGlyphVector(final GlyphVector g, final double x, final double y) {
		this.g.drawGlyphVector(g, (int) x, (int) y);
	}

	public boolean drawImage(final Image img, final AffineTransform xform) {
		return g.drawImage(img, xform, null);
	}

	public void drawImage(final BufferedImage img, final BufferedImageOp op, final double x, final double y) {
		g.drawImage(img, op, (int) x, (int) y);
	}

	public void drawRenderableImage(final RenderableImage img, final AffineTransform xform) {
		g.drawRenderableImage(img, xform);
	}

	public void drawRenderedImage(final RenderedImage img, final AffineTransform xform) {
		g.drawRenderedImage(img, xform);
	}

	/**
	 * Draws the text given by the specified string, using this graphics context's
	 * current font and color. The baseline of the leftmost character is at position
	 * (<i>x</i>,&nbsp;<i>y</i>) in this graphics context's coordinate system.
	 * 
	 * @param str the string to be drawn.
	 * @param x the <i>x</i> coordinate.
	 * @param y the <i>y</i> coordinate.
	 * @throws NullPointerException if {@code str} is {@code null}.
	 * @see java.awt.Graphics#drawBytes
	 * @see java.awt.Graphics#drawChars
	 */
	public void drawString(final String str, final double x, final double y) {
		g.drawString(str, (int) x, (int) y);
	}

	/**
	 * Renders the text of the specified iterator applying its attributes in
	 * accordance with the specification of the {@link java.awt.font.TextAttribute
	 * TextAttribute} class.
	 * <p>
	 * The baseline of the leftmost character is at position
	 * (<i>x</i>,&nbsp;<i>y</i>) in this graphics context's coordinate system.
	 * 
	 * @param iterator the iterator whose text is to be drawn
	 * @param x the <i>x</i> coordinate.
	 * @param y the <i>y</i> coordinate.
	 * @throws NullPointerException if {@code iterator} is {@code null}.
	 * @see java.awt.Graphics#drawBytes
	 * @see java.awt.Graphics#drawChars
	 */
	public void drawString(final AttributedCharacterIterator iterator, final double x, final double y) {
		g.drawString(iterator, (int) x, (int) y);
	}

	/**
	 * Draws the outline of the specified rectangle. The left and right edges of the
	 * rectangle are at {@code x} and <code>x&nbsp;+&nbsp;width</code>. The top and
	 * bottom edges are at {@code y} and <code>y&nbsp;+&nbsp;height</code>. The
	 * rectangle is drawn using the graphics context's current color.
	 * 
	 * @param x the <i>x</i> coordinate of the rectangle to be drawn.
	 * @param y the <i>y</i> coordinate of the rectangle to be drawn.
	 * @param width the width of the rectangle to be drawn.
	 * @param height the height of the rectangle to be drawn.
	 * @see java.awt.Graphics#fillRect
	 * @see java.awt.Graphics#clearRect
	 */
	public void drawRect(final double x, final double y, final double width, final double height) {
		g.drawRect((int) x, (int) y, (int) width, (int) height);
	}

	public void fill(final Shape s) {
		g.fill(s);
	}

	/**
	 * Returns the background color used for clearing a region.
	 * 
	 * @return the current {@code Graphics2D Color}, which defines the background
	 *         color.
	 * @see #setBackground
	 */
	public Color getBackground() {
		return g.getBackground();
	}

	/**
	 * Returns the current {@code Composite} in the {@code Graphics2D} context.
	 * 
	 * @return the current {@code Graphics2D Composite}, which defines a compositing
	 *         style.
	 * @see #setComposite
	 */
	public Composite getComposite() {
		return g.getComposite();
	}

	/**
	 * Returns the device configuration associated with this {@code Graphics2D}.
	 * 
	 * @return the device configuration of this {@code Graphics2D}.
	 */
	public GraphicsConfiguration getDeviceConfiguration() {
		return g.getDeviceConfiguration();
	}

	/**
	 * Get the rendering context of the {@code Font} within this {@code Graphics2D}
	 * context. The {@link FontRenderContext} encapsulates application hints such as
	 * anti-aliasing and fractional metrics, as well as target device specific
	 * information such as dots-per-inch. This information should be provided by the
	 * application when using objects that perform typographical formatting, such as
	 * {@code Font} and {@code TextLayout}. This information should also be provided
	 * by applications that perform their own layout and need accurate measurements
	 * of various characteristics of glyphs such as advance and line height when
	 * various rendering hints have been applied to the text rendering.
	 *
	 * @return a reference to an instance of FontRenderContext.
	 * @see java.awt.font.FontRenderContext
	 * @see java.awt.Font#createGlyphVector
	 * @see java.awt.font.TextLayout
	 * @since 1.2
	 */
	public FontRenderContext getFontRenderContext() {
		return g.getFontRenderContext();
	}

	/**
	 * Returns the current {@code Paint} of the {@code Graphics2D} context.
	 * 
	 * @return the current {@code Graphics2D Paint}, which defines a color or
	 *         pattern.
	 * @see #setPaint
	 * @see java.awt.Graphics#setColor
	 */
	public Paint getPaint() {
		return g.getPaint();
	}

	/**
	 * Returns the value of a single preference for the rendering algorithms. Hint
	 * categories include controls for rendering quality and overall time/quality
	 * trade-off in the rendering process. Refer to the {@code RenderingHints} class
	 * for definitions of some common keys and values.
	 * 
	 * @param hintKey the key corresponding to the hint to get.
	 * @return an object representing the value for the specified hint key. Some of
	 *         the keys and their associated values are defined in the
	 *         {@code RenderingHints} class.
	 * @see RenderingHints
	 * @see #setRenderingHint(RenderingHints.Key, Object)
	 */
	public Object getRenderingHint(final Key hintKey) {
		return g.getRenderingHint(hintKey);
	}

	/**
	 * Gets the preferences for the rendering algorithms. Hint categories include
	 * controls for rendering quality and overall time/quality trade-off in the
	 * rendering process. Returns all of the hint key/value pairs that were ever
	 * specified in one operation. Refer to the {@code RenderingHints} class for
	 * definitions of some common keys and values.
	 * 
	 * @return a reference to an instance of {@code RenderingHints} that contains
	 *         the current preferences.
	 * @see RenderingHints
	 * @see #setRenderingHints(Map)
	 */
	public RenderingHints getRenderingHints() {
		return g.getRenderingHints();
	}

	/**
	 * Returns the current {@code Stroke} in the {@code Graphics2D} context.
	 * 
	 * @return the current {@code Graphics2D Stroke}, which defines the line style.
	 * @see #setStroke
	 */
	public Stroke getStroke() {
		return g.getStroke();
	}

	/**
	 * Returns a copy of the current {@code Transform} in the {@code Graphics2D}
	 * context.
	 * 
	 * @return the current {@code AffineTransform} in the {@code Graphics2D}
	 *         context.
	 * @see #transform
	 * @see #setTransform
	 */
	public AffineTransform getTransform() {
		return g.getTransform();
	}

	/**
	 * Checks whether or not the specified {@code Shape} intersects the specified
	 * {@link Rectangle}, which is in device space. If {@code onStroke} is false,
	 * this method checks whether or not the interior of the specified {@code Shape}
	 * intersects the specified {@code Rectangle}. If {@code onStroke} is
	 * {@code true}, this method checks whether or not the {@code Stroke} of the
	 * specified {@code Shape} outline intersects the specified {@code Rectangle}.
	 * The rendering attributes taken into account include the {@code Clip},
	 * {@code Transform}, and {@code Stroke} attributes.
	 * 
	 * @param rect the area in device space to check for a hit
	 * @param s the {@code Shape} to check for a hit
	 * @param onStroke flag used to choose between testing the stroked or the filled
	 *        shape. If the flag is {@code true}, the {@code Stroke} outline is
	 *        tested. If the flag is {@code false}, the filled {@code Shape} is
	 *        tested.
	 * @return {@code true} if there is a hit; {@code false} otherwise.
	 * @see #setStroke
	 * @see #fill
	 * @see #draw
	 * @see #transform
	 * @see #setTransform
	 * @see #clip
	 * @see #setClip
	 */
	public boolean hit(final Rectangle rect, final Shape s, final boolean onStroke) {
		return g.hit(rect, s, onStroke);
	}

	/**
	 * Concatenates the current {@code Graphics2D} {@code Transform} with a rotation
	 * transform. Subsequent rendering is rotated by the specified radians relative
	 * to the previous origin. This is equivalent to calling {@code transform(R)},
	 * where R is an {@code AffineTransform} represented by the following matrix:
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
	public void rotate(final double theta) {
		g.rotate(theta);
	}

	/**
	 * Concatenates the current {@code Graphics2D} {@code Transform} with a
	 * translated rotation transform. Subsequent rendering is transformed by a
	 * transform which is constructed by translating to the specified location,
	 * rotating by the specified radians, and translating back by the same amount as
	 * the original translation. This is equivalent to the following sequence of
	 * calls:
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
	public void rotate(final double theta, final double x, final double y) {
		g.rotate(theta, x, y);
	}

	/**
	 * Concatenates the current {@code Graphics2D} {@code Transform} with a scaling
	 * transformation Subsequent rendering is resized according to the specified
	 * scaling factors relative to the previous scaling. This is equivalent to
	 * calling {@code transform(S)}, where S is an {@code AffineTransform}
	 * represented by the following matrix:
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
	public void scale(final double sx, final double sy) {
		g.scale(sx, sy);
	}

	public void setBackground(final Color color) {
		g.setBackground(color);
	}

	/**
	 * Sets the {@code Composite} for the {@code Graphics2D} context. The
	 * {@code Composite} is used in all drawing methods such as {@code drawImage},
	 * {@code drawString}, {@code draw}, and {@code fill}. It specifies how new
	 * pixels are to be combined with the existing pixels on the graphics device
	 * during the rendering process.
	 * <p>
	 * If this {@code Graphics2D} context is drawing to a {@code Component} on the
	 * display screen and the {@code Composite} is a custom object rather than an
	 * instance of the {@code AlphaComposite} class, and if there is a security
	 * manager, its {@code checkPermission} method is called with an
	 * {@code AWTPermission("readDisplayPixels")} permission.
	 * 
	 * @param comp the {@code Composite} object to be used for rendering
	 * @throws SecurityException if a custom {@code Composite} object is being used
	 *         to render to the screen and a security manager is set and its
	 *         {@code checkPermission} method does not allow the operation.
	 * @see java.awt.Graphics#setXORMode
	 * @see java.awt.Graphics#setPaintMode
	 * @see #getComposite
	 * @see AlphaComposite
	 * @see SecurityManager#checkPermission
	 * @see java.awt.AWTPermission
	 */
	public void setComposite(final Composite comp) {
		g.setComposite(comp);
	}

	/**
	 * Sets the {@code Paint} attribute for the {@code Graphics2D} context. Calling
	 * this method with a {@code null Paint} object does not have any effect on the
	 * current {@code Paint} attribute of this {@code Graphics2D}.
	 * 
	 * @param paint the {@code Paint} object to be used to generate color during the
	 *        rendering process, or {@code null}
	 * @see java.awt.Graphics#setColor
	 * @see #getPaint
	 * @see GradientPaint
	 * @see TexturePaint
	 */
	public void setPaint(final Paint paint) {
		g.setPaint(paint);
	}

	/**
	 * Sets the value of a single preference for the rendering algorithms. Hint
	 * categories include controls for rendering quality and overall time/quality
	 * trade-off in the rendering process. Refer to the {@code RenderingHints} class
	 * for definitions of some common keys and values.
	 * 
	 * @param hintKey the key of the hint to be set.
	 * @param hintValue the value indicating preferences for the specified hint
	 *        category.
	 * @see #getRenderingHint(RenderingHints.Key)
	 * @see RenderingHints
	 */
	public void setRenderingHint(final Key hintKey, final Object hintValue) {
		g.setRenderingHint(hintKey, hintValue);
	}

	/**
	 * Replaces the values of all preferences for the rendering algorithms with the
	 * specified {@code hints}. The existing values for all rendering hints are
	 * discarded and the new set of known hints and values are initialized from the
	 * specified {@link Map} object. Hint categories include controls for rendering
	 * quality and overall time/quality trade-off in the rendering process. Refer to
	 * the {@code RenderingHints} class for definitions of some common keys and
	 * values.
	 * 
	 * @param hints the rendering hints to be set
	 * @see #getRenderingHints
	 * @see RenderingHints
	 */
	public void setRenderingHints(final Map<?, ?> hints) {
		g.setRenderingHints(hints);
	}

	/**
	 * Sets the {@code Stroke} for the {@code Graphics2D} context.
	 * 
	 * @param s the {@code Stroke} object to be used to stroke a {@code Shape}
	 *        during the rendering process
	 * @see BasicStroke
	 * @see #getStroke
	 */
	public void setStroke(final Stroke s) {
		g.setStroke(s);
	}

	/**
	 * Overwrites the Transform in the {@code Graphics2D} context. WARNING: This
	 * method should <b>never</b> be used to apply a new coordinate transform on top
	 * of an existing transform because the {@code Graphics2D} might already have a
	 * transform that is needed for other purposes, such as rendering Swing
	 * components or applying a scaling transformation to adjust for the resolution
	 * of a printer.
	 * <p>
	 * To add a coordinate transform, use the {@code transform}, {@code rotate},
	 * {@code scale}, or {@code shear} methods. The {@code setTransform} method is
	 * intended only for restoring the original {@code Graphics2D} transform after
	 * rendering, as shown in this example:
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
	 * @param Tx the {@code AffineTransform} that was retrieved from the
	 *        {@code getTransform} method
	 * @see #transform
	 * @see #getTransform
	 * @see AffineTransform
	 */
	public void setTransform(final AffineTransform Tx) {
		g.setTransform(Tx);
	}

	/**
	 * Concatenates the current {@code Graphics2D} {@code Transform} with a shearing
	 * transform. Subsequent renderings are sheared by the specified multiplier
	 * relative to the previous position. This is equivalent to calling
	 * {@code transform(SH)}, where SH is an {@code AffineTransform} represented by
	 * the following matrix:
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
	public void shear(final double shx, final double shy) {
		g.shear(shx, shy);
	}

	/**
	 * Composes an {@code AffineTransform} object with the {@code Transform} in this
	 * {@code Graphics2D} according to the rule last-specified-first-applied. If the
	 * current {@code Transform} is Cx, the result of composition with Tx is a new
	 * {@code Transform} Cx'. Cx' becomes the current {@code Transform} for this
	 * {@code Graphics2D}. Transforming a point p by the updated {@code Transform}
	 * Cx' is equivalent to first transforming p by Tx and then transforming the
	 * result by the original {@code Transform} Cx. In other words, Cx'(p) =
	 * Cx(Tx(p)). A copy of the Tx is made, if necessary, so further modifications
	 * to Tx do not affect rendering.
	 * 
	 * @param Tx the {@code AffineTransform} object to be composed with the current
	 *        {@code Transform}
	 * @see #setTransform
	 * @see AffineTransform
	 */
	public void transform(final AffineTransform Tx) {
		g.transform(Tx);
	}

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
	public void translate(final double tx, final double ty) {
		g.translate(tx, ty);
	}

	/**
	 * Returns a {@code String} object representing this {@code Graphics} object's
	 * value.
	 * 
	 * @return a string representation of this graphics context.
	 */
	@Override
	public String toString() {
		return getClass().getName() + "[font=" + getFont() + ",color=" + getColor() + "]";
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
	 * @return {@code true} if the specified rectangle intersects the bounds of the
	 *         current clip; {@code false} otherwise.
	 */
	public boolean hitClip(final double x, final double y, final double width, final double height) {
		return g.hitClip((int) x, (int) y, (int) width, (int) height);
	}

	/**
	 * Gets the font metrics of the current font.
	 * 
	 * @return the font metrics of this graphics context's current font.
	 * @see java.awt.Graphics#getFont
	 * @see java.awt.FontMetrics
	 * @see java.awt.Graphics#getFontMetrics(Font)
	 */
	public FontMetrics getFontMetrics() {
		return g.getFontMetrics();
	}

	/**
	 * Returns the bounding rectangle of the current clipping area. The coordinates
	 * in the rectangle are relative to the coordinate system origin of this
	 * graphics context. This method differs from {@link #getClipBounds()
	 * getClipBounds} in that an existing rectangle is used instead of allocating a
	 * new one. This method refers to the user clip, which is independent of the
	 * clipping associated with device bounds and window visibility. If no clip has
	 * previously been set, or if the clip has been cleared using
	 * {@code setClip(null)}, this method returns the specified {@code Rectangle}.
	 * 
	 * @param r the rectangle where the current clipping area is copied to. Any
	 *        current values in this rectangle are overwritten.
	 * @return the bounding rectangle of the current clipping area.
	 */
	public Rectangle getClipBounds(final Rectangle r) {
		return g.getClipBounds(r);
	}

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
	public void fillPolygon(final Polygon p) {
		g.fillPolygon(p);
	}

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
	public void fill3DRect(final double x, final double y, final double width, final double height, final boolean raised) {
		g.fill3DRect((int) x, (int) y, (int) width, (int) height, raised);
	}

	/**
	 * Draws the outline of a polygon defined by the specified {@code Polygon}
	 * object.
	 * 
	 * @param p the polygon to draw.
	 * @see java.awt.Graphics#fillPolygon
	 * @see java.awt.Graphics#drawPolyline
	 */
	public void drawPolygon(final Polygon p) {
		g.drawPolygon(p);
	}

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
	 * @throws NullPointerException if {@code data} is {@code null}.
	 * @throws IndexOutOfBoundsException if {@code offset} or {@code length} is less
	 *         than zero, or {@code offset+length} is greater than the length of the
	 *         {@code data} array.
	 * @see java.awt.Graphics#drawBytes
	 * @see java.awt.Graphics#drawString
	 */
	public void drawChars(final char[] data, final int offset, final int length, final double x, final double y) {
		g.drawChars(data, offset, length, (int) x, (int) y);
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
	 * @throws NullPointerException if {@code data} is {@code null}.
	 * @throws IndexOutOfBoundsException if {@code offset} or {@code length} is less
	 *         than zero, or {@code offset+length} is greater than the length of the
	 *         {@code data} array.
	 * @see java.awt.Graphics#drawChars
	 * @see java.awt.Graphics#drawString
	 */
	public void drawBytes(final byte[] data, final int offset, final int length, final double x, final double y) {
		g.drawBytes(data, offset, length, (int) x, (int) y);
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
	public void draw3DRect(final double x, final double y, final double width, final double height, final boolean raised) {
		g.draw3DRect((int) x, (int) y, (int) width, (int) height, raised);
	}

	public void turnOnLightContribution() {
		ligtsShouldContribute = true;
	}

	public void turnOffLightContribution() {
		ligtsShouldContribute = false;
	}

	public void zoomToLookAt() {
		if (DoaCamera.isMouseZoomingEnabled()) {
			final DoaObject centerOfZoom = DoaCamera.getObjectToZoomInto();
			if (centerOfZoom != null) {
				g.translate(DoaCamera.getX() + centerOfZoom.getPosition().x + centerOfZoom.getWidth() / 2.0,
				        DoaCamera.getY() + centerOfZoom.getPosition().y + centerOfZoom.getHeight() / 2.0);
				g.scale(DoaCamera.getZ(), DoaCamera.getZ());
				g.translate(-DoaCamera.getX() - centerOfZoom.getPosition().x - centerOfZoom.getWidth() / 2.0,
				        -DoaCamera.getY() - centerOfZoom.getPosition().y - centerOfZoom.getHeight() / 2.0);
			} else {
				g.translate(DoaCamera.getX() + DoaWindow.WINDOW_WIDTH / 2.0, DoaCamera.getY() + DoaWindow.WINDOW_HEIGHT / 2.0);
				g.scale(DoaCamera.getZ(), DoaCamera.getZ());
				g.translate(-DoaCamera.getX() - DoaWindow.WINDOW_WIDTH / 2.0, -DoaCamera.getY() - DoaWindow.WINDOW_HEIGHT / 2.0);
			}
		}
	}

	public void resetTransform() {
		g.setTransform(new AffineTransform());
	}

	public void pushTransform() {
		transforms.push(getTransform());
	}

	public void popTransform() {
		setTransform(transforms.pop());
	}

	public void pushComposite() {
		composites.push(getComposite());
	}

	public void popComposite() {
		setComposite(composites.pop());
	}

	public void pushClip() {
		clips.push(getClip());
	}

	public void popClip() {
		setClip(clips.pop());
	}

	private static int[] doubleArrToIntArr(final double[] arrToBeConverted) {
		final int[] rv = new int[arrToBeConverted.length];
		for (int i = 0; i < arrToBeConverted.length; i++) {
			rv[i] = (int) arrToBeConverted[i];
		}
		return rv;
	}
}