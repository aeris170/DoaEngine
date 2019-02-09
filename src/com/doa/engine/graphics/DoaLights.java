package com.doa.engine.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map.Entry;

import com.doa.engine.DoaObject;

/**
 * Responsible for providing a tool to create and set scene lights for
 * DoaEngine. This class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.1
 * @version 1.1
 */
public final class DoaLights {

	private static Color ambientLightColor = Color.WHITE;

	/**
	 * Constructor.
	 */
	private DoaLights() {}

	/**
	 * Replaces the current ambient light with a new ambient light with the passed
	 * color. When a new ambient light is set, all DoaObjects DoaEngine renders will
	 * be affected by the new ambient light except the DoaObjects with a zOrder of
	 * {@link DoaObject#STATIC_FRONT} and sprites created without DoaSprites class.
	 * Note: If this method has not yet been invoked, the ambient light color will
	 * be {@link Color#WHITE}, meaning rendering will not be affected by ambient
	 * light at all.
	 *
	 * @param newAmbientLightColor color of the new ambient lights
	 */
	public static void ambientLight(final Color newAmbientLightColor) {
		ambientLightColor = newAmbientLightColor;
		DoaSprites.SHADED_SPRITES.clear();
		for (final Entry<String, BufferedImage> entry : DoaSprites.ORIGINAL_SPRITES.entrySet()) {
			applyAmbientLight(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Returns the color of the current ambient light DoaEngine uses to shade
	 * DoaObjects.
	 *
	 * @return the current ambient light color
	 */
	public static Color getAmbientLightColor() {
		return ambientLightColor;
	}

	// TODO FIX THE DYNAMIC LIGHTING CHANGE BUG
	static void applyAmbientLight(final String spriteName, final BufferedImage bf) {
		BufferedImage bfclone = new BufferedImage(bf.getWidth(), bf.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bfclone.createGraphics();
		g2d.drawImage(bf, 0, 0, null);
		g2d.dispose();

		for (int xx = 0; xx < bfclone.getWidth(); xx++) {
			for (int yy = 0; yy < bfclone.getHeight(); yy++) {
				final Color objColor = new Color(bfclone.getRGB(xx, yy), true);
				final int r = objColor.getRed() * ambientLightColor.getRed() / 255;
				final int g = objColor.getGreen() * ambientLightColor.getGreen() / 255;
				final int b = objColor.getBlue() * ambientLightColor.getBlue() / 255;
				final int a = objColor.getAlpha();
				bfclone.setRGB(xx, yy, new Color(r, g, b, a).getRGB());
			}
		}
		DoaSprites.SHADED_SPRITES.put(spriteName, bfclone);
	}
}