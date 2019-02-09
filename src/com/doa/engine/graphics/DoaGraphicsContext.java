package com.doa.engine.graphics;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
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
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * Responsible for wrapping the {@code Graphics2D} class Sun Microsystems
 * provides. All and every method this class provides behaves exactly the same
 * as documented in {@code Graphics}, {@code Graphics2D} and {@code DoaSprites}.
 * Refer to these 3 class documentations for this class' behaviour.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.1
 * @version 1.1
 * @see java.awt.Graphics
 * @see java.awt.Graphics2D
 * @see com.doa.engine.graphics.DoaSprites
 */
public final class DoaGraphicsContext {

	private Graphics2D g = null;
	private boolean ligtsShouldContribute = true;

	public DoaGraphicsContext(final Graphics2D g) {
		this.g = g;
	}

	public void clearRect(final double x, final double y, final int width, final int height) {
		g.clearRect((int) x, (int) y, width, height);
	}

	public void clipRect(final double x, final double y, final int width, final int height) {
		g.clipRect((int) x, (int) y, width, height);
	}

	public void copyArea(final double x, final double y, final int width, final int height, final double dx, final double dy) {
		g.copyArea((int) x, (int) y, width, height, (int) dx, (int) dy);
	}

	public Graphics create() {
		return g.create();
	}

	public Graphics create(final double x, final double y, final int width, final int height) {
		return g.create((int) x, (int) y, width, height);
	}

	public void dispose() {
		g.dispose();
	}

	public void drawArc(final double x, final double y, final int width, final int height, final double startAngle, final double arcAngle) {
		g.drawArc((int) x, (int) y, width, height, (int) startAngle, (int) arcAngle);
	}

	public boolean drawImage(final Image img, final double x, final double y, final ImageObserver observer) {
		return g.drawImage(img, (int) x, (int) y, observer);
	}

	public boolean drawImage(final Image img, final double x, final double y, final Color bgcolor, final ImageObserver observer) {
		return g.drawImage(img, (int) x, (int) y, bgcolor, observer);
	}

	public boolean drawImage(final Image img, final double x, final double y, final int width, final int height, final ImageObserver observer) {
		return g.drawImage(img, (int) x, (int) y, width, height, observer);
	}

	public boolean drawImage(final Image img, final double x, final double y, final int width, final int height, final Color bgcolor, final ImageObserver observer) {
		return g.drawImage(img, (int) x, (int) y, width, height, bgcolor, observer);
	}

	public boolean drawImage(final Image img, final double dx1, final double dy1, final double dx2, final double dy2, final double sx1, final double sy1,
	        final double sx2, final double sy2, final ImageObserver observer)
	{
		return g.drawImage(img, (int) dx1, (int) dy1, (int) dx2, (int) dy2, (int) sx1, (int) sy1, (int) sx2, (int) sy2, observer);
	}

	public boolean drawImage(final Image img, final double dx1, final double dy1, final double dx2, final double dy2, final double sx1, final double sy1,
	        final double sx2, final double sy2, final Color bgcolor, final ImageObserver observer)
	{
		return g.drawImage(img, (int) dx1, (int) dy1, (int) dx2, (int) dy2, (int) sx1, (int) sy1, (int) sx2, (int) sy2, bgcolor, observer);
	}

	public void drawLine(final double x1, final double y1, final double x2, final double y2) {
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	public void drawOval(final double x, final double y, final int width, final int height) {
		g.drawOval((int) x, (int) y, width, height);
	}

	public void drawPolygon(final double[] xPoints, final double[] yPoints, final int nPoints) {
		g.drawPolygon(doubleArrToIntArr(xPoints), doubleArrToIntArr(yPoints), nPoints);
	}

	public void drawPolyline(final double[] xPoints, final double[] yPoints, final int nPoints) {
		g.drawPolyline(doubleArrToIntArr(xPoints), doubleArrToIntArr(yPoints), nPoints);
	}

	public void drawRoundRect(final double x, final double y, final int width, final int height, final int arcWidth, final int arcHeight) {
		g.drawRoundRect((int) x, (int) y, width, height, arcWidth, arcHeight);
	}

	public void fillArc(final double x, final double y, final int width, final int height, final double startAngle, final double arcAngle) {
		g.fillArc((int) x, (int) y, width, height, (int) startAngle, (int) arcAngle);
	}

	public void fillOval(final double x, final double y, final int width, final int height) {
		g.fillOval((int) x, (int) y, width, height);
	}

	public void fillPolygon(final double[] xPoints, final double[] yPoints, final int nPoints) {
		g.fillPolygon(doubleArrToIntArr(xPoints), doubleArrToIntArr(yPoints), nPoints);
	}

	public void fillRect(final double x, final double y, final int width, final int height) {
		g.fillRect((int) x, (int) y, width, height);
	}

	public void fillRoundRect(final double x, final double y, final int width, final int height, final int arcWidth, final int arcHeight) {
		g.fillRoundRect((int) x, (int) y, width, height, arcWidth, arcHeight);
	}

	public Shape getClip() {
		return g.getClip();
	}

	public Rectangle getClipBounds() {
		return g.getClipBounds();
	}

	public Color getColor() {
		return g.getColor();
	}

	public Font getFont() {
		return g.getFont();
	}

	public FontMetrics getFontMetrics(final Font f) {
		return g.getFontMetrics(f);
	}

	public void setClip(final Shape clip) {
		g.setClip(clip);
	}

	public void setClip(final double x, final double y, final int width, final int height) {
		g.setClip((int) x, (int) y, width, height);
	}

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

	public void setFont(final Font font) {
		g.setFont(font);
	}

	public void setPaintMode() {
		g.setPaintMode();
	}

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

	public boolean drawImage(final Image img, final AffineTransform xform, final ImageObserver obs) {
		return g.drawImage(img, xform, obs);
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

	public void drawString(final String str, final double x, final double y) {
		g.drawString(str, (int) x, (int) y);
	}

	public void drawString(final AttributedCharacterIterator iterator, final double x, final double y) {
		g.drawString(iterator, (int) x, (int) y);
	}

	public void drawRect(final double x, final double y, final int width, final int height) {
		g.drawRect((int) x, (int) y, width, height);
	}

	public void fill(final Shape s) {
		g.fill(s);
	}

	public Color getBackground() {
		return g.getBackground();
	}

	public Composite getComposite() {
		return g.getComposite();
	}

	public GraphicsConfiguration getDeviceConfiguration() {
		return g.getDeviceConfiguration();
	}

	public FontRenderContext getFontRenderContext() {
		return g.getFontRenderContext();
	}

	public Paint getPaint() {
		return g.getPaint();
	}

	public Object getRenderingHint(final Key hintKey) {
		return g.getRenderingHint(hintKey);
	}

	public RenderingHints getRenderingHints() {
		return g.getRenderingHints();
	}

	public Stroke getStroke() {
		return g.getStroke();
	}

	public AffineTransform getTransform() {
		return g.getTransform();
	}

	public boolean hit(final Rectangle rect, final Shape s, final boolean onStroke) {
		return g.hit(rect, s, onStroke);
	}

	public void rotate(final double theta) {
		g.rotate(theta);
	}

	public void rotate(final double theta, final double x, final double y) {
		g.rotate(theta, x, y);
	}

	public void scale(final double sx, final double sy) {
		g.scale(sx, sy);
	}

	public void setBackground(final Color color) {
		g.setBackground(color);
	}

	public void setComposite(final Composite comp) {
		g.setComposite(comp);
	}

	public void setPaint(final Paint paint) {
		g.setPaint(paint);
	}

	public void setRenderingHint(final Key hintKey, final Object hintValue) {
		g.setRenderingHint(hintKey, hintValue);
	}

	public void setRenderingHints(final Map<?, ?> hints) {
		g.setRenderingHints(hints);
	}

	public void setStroke(final Stroke s) {
		g.setStroke(s);
	}

	public void setTransform(final AffineTransform Tx) {
		g.setTransform(Tx);
	}

	public void shear(final double shx, final double shy) {
		g.shear(shx, shy);
	}

	public void transform(final AffineTransform Tx) {
		g.transform(Tx);
	}

	public void translate(final double tx, final double ty) {
		g.translate(tx, ty);
	}

	@Override
	public String toString() {
		return getClass().getName() + "[font=" + getFont() + ",color=" + getColor() + "]";
	}

	public boolean hitClip(final double x, final double y, final int width, final int height) {
		return g.hitClip((int) x, (int) y, width, height);
	}

	public FontMetrics getFontMetrics() {
		return g.getFontMetrics();
	}

	public Rectangle getClipBounds(final Rectangle r) {
		return g.getClipBounds(r);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		g.finalize();
	}

	public void fillPolygon(final Polygon p) {
		g.fillPolygon(p);
	}

	public void fill3DRect(final double x, final double y, final int width, final int height, final boolean raised) {
		g.fill3DRect((int) x, (int) y, width, height, raised);
	}

	public void drawPolygon(final Polygon p) {
		g.drawPolygon(p);
	}

	public void drawChars(final char[] data, final int offset, final int length, final double x, final double y) {
		g.drawChars(data, offset, length, (int) x, (int) y);
	}

	public void drawBytes(final byte[] data, final int offset, final int length, final double x, final double y) {
		g.drawBytes(data, offset, length, (int) x, (int) y);
	}

	public void draw3DRect(final double x, final double y, final int width, final int height, final boolean raised) {
		g.draw3DRect((int) x, (int) y, width, height, raised);
	}

	public void turnOnLightContribution() {
		ligtsShouldContribute = true;
	}

	public void turnOffLightContribution() {
		ligtsShouldContribute = false;
	}

	private static int[] doubleArrToIntArr(final double[] arrToBeConverted) {
		final int[] rv = new int[arrToBeConverted.length];
		for (int i = 0; i < arrToBeConverted.length; i++) {
			rv[i] = (int) arrToBeConverted[i];
		}
		return rv;
	}
}