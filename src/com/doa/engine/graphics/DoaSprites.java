package com.doa.engine.graphics;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;

import com.doa.engine.DoaEngine;
import com.doa.engine.log.DoaLogger;
import com.doa.engine.log.LogLevel;

/**
 * Responsible for providing a Factory to create Sprites for {@code DoaEngine} to display. This
 * class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.1
 * @version 2.7
 */
public final class DoaSprites {

	private static final DoaLogger LOGGER = DoaLogger.getInstance();

	/**
	 * Collection that maps sprite names to sprites.
	 */
	public static final Map<String, BufferedImage> ORIGINAL_SPRITES = new HashMap<>();

	/**
	 * Collection that maps sprite names to sprites that are shaded according to the lights the scene
	 * has.
	 */
	public static final Map<String, BufferedImage> SHADED_SPRITES = new HashMap<>();

	private DoaSprites() {}

	/**
	 * Creates a sprite, adds the created sprite to{@code DoaSprites.ORIGINAL_SPRITES}, and returns the
	 * newly created sprite.
	 *
	 * @param spriteName unique name of the sprite that will be created
	 * @param spriteFile path to the sprite on the disk
	 * @return the sprite in {@code DoaSprites.ORIGINAL_SPRITES} whose name is spriteName
	 * @throws IOException if sprite cannot be loaded by {@code DoaEngine}
	 */
	public static BufferedImage createSprite(@NotNull final String spriteName, @NotNull final String spriteFile) throws IOException {
		final BufferedImage sp = ImageIO.read(DoaSprites.class.getResourceAsStream(spriteFile));
		ORIGINAL_SPRITES.put(spriteName, sp);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(128).append(spriteName).append(" sprite instantiated."));
		} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine("DoaSprite instantiated.");
		}
		DoaLights.applyAmbientLight(spriteName, sp);
		return toCompat(sp);
	}

	/**
	 * Creates a sprite from a sprite-sheet by cropping the sprite-sheet using the boundaries parameter,
	 * adds the created sprite to {@code DoaSprites.ORIGINAL_SPRITES}, and returns the newly created
	 * sprite.
	 *
	 * @param spriteName unique name of the sprite that will be created
	 * @param spriteFile path to the sprite-sheet from which the new sprite will be cropped from
	 * @param boundaries bounding box of the sprite inside the sprite-sheet, i.e. where the sprite is
	 *        inside the sprite-sheet
	 * @return the sprite in {@code DoaSprites.ORIGINAL_SPRITES} whose name is spriteName
	 * @throws IOException if sprite-sheet cannot be loaded by {@code DoaEngine}
	 */
	public static BufferedImage createSpriteFromSpriteSheet(@NotNull final String spriteName, @NotNull final String spriteFile, @NotNull final Rectangle boundaries)
	        throws IOException
	{
		final BufferedImage sp = ImageIO.read(DoaSprites.class.getResourceAsStream(spriteFile)).getSubimage(boundaries.x, boundaries.y, boundaries.width, boundaries.height);
		ORIGINAL_SPRITES.put(spriteName, sp);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(128).append(spriteName).append(" sprite instantiated."));
		} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine("DoaSprite instantiated.");
		}
		DoaLights.applyAmbientLight(spriteName, sp);
		return toCompat(sp);
	}

	/**
	 * Conveniency method. This method exists solely to reduce the amount of typing required while
	 * trying to retrieve a sprite from {@code DoaSprites} class. Calling this method is equivalent to
	 * calling: {@code DoaSprites.SHADED_SPRITES.get(spriteName)};
	 *
	 * @param spriteName name of the sprite that is trying to be retrieved
	 * @return the sprite in {@code DoaSprites.SHADED_SPRITES} whose name is spriteName
	 */
	public static BufferedImage get(final String spriteName) {
		return SHADED_SPRITES.get(spriteName);
	}

	/**
	 * Deep copies a {@code DoaSprite}
	 *
	 * @param sprite {@code DoaSprite} to deep copy
	 * @return the deep copy of the passed {@code DoaSprites}
	 */
	public static BufferedImage deepCopyDoaSprite(final BufferedImage sprite) {
		return toCompat(sprite);
	}

	/**
	 * Creates a scaled version of a {@code DoaSprite}.A new {@code DoaSprite} will be returned as a
	 * result of this call. The returned sprite is not stored neither inside
	 * {@code DoaSprites.ORIGINAL_SPRITES}, nor {@code DoaSprites.SHADED_SPRITES}.
	 *
	 * @param sprite {@code DoaSprite} to scale
	 * @param width width of the scaled {@code DoaSprite}
	 * @param height height of the scaled {@code DoaSprite}
	 * @return the scaled instance of the passed {@code DoaSprite}
	 */
	public static BufferedImage scale(@NotNull final BufferedImage sprite, final int width, final int height) {
		if (sprite == null) {
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.SEVERE) >= 0) {
				LOGGER.severe("DoaSprites cannot resize a sprite that is null.");
			}
			throw new IllegalArgumentException("sprite == null");
		}
		if (width < 0) {
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.SEVERE) >= 0) {
				LOGGER.severe("DoaSprites cannot shrink a sprite beyond 0 width.");
			}
			throw new IllegalArgumentException("width < 0");
		}
		if (height < 0) {
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.SEVERE) >= 0) {
				LOGGER.severe("DoaSprites cannot shrink a sprite beyond 0 height.");
			}
			throw new IllegalArgumentException("height < 0");
		}
		final Image i = sprite.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		if (i instanceof BufferedImage) {
			return (BufferedImage) i;
		}
		final BufferedImage bimage = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(i, 0, 0, null);
		bGr.dispose();
		return toCompat(bimage);
	}

	private static BufferedImage toCompat(final BufferedImage sp) {
		// obtain the current system graphical settings
		final GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		/* if image is already compatible and optimized for current system settings, simply return it */
		if (sp.getColorModel().equals(gfxConfig.getColorModel())) {
			return sp;
		}

		// image is not optimized, so create a new image that is
		final BufferedImage newImage = gfxConfig.createCompatibleImage(sp.getWidth(), sp.getHeight(), sp.getTransparency());

		// get the graphics context of the new image to draw the old image on
		final Graphics2D g2d = newImage.createGraphics();

		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(sp, 0, 0, null);
		g2d.dispose();

		// return the new optimized image
		return newImage;
	}
}