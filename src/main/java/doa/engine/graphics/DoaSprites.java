package doa.engine.graphics;

import static doa.engine.core.DoaGraphicsFunctions.areLightsContributing;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;

import doa.engine.log.DoaLogger;
import doa.engine.log.LogLevel;

/**
 * Responsible for providing a Factory to create sprites for DoaEngine to
 * display. This class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.1
 * @version 3.0
 */
public final class DoaSprites {

	private static final DoaLogger LOGGER = DoaLogger.getInstance();

	/**
	 * Collection that maps sprite names to sprites.
	 */
	public static final Map<String, BufferedImage> ORIGINAL_SPRITES = new HashMap<>();

	/**
	 * Collection that maps sprite names to sprites that are shaded according to the
	 * lights the scene has.
	 */
	public static final Map<String, BufferedImage> SHADED_SPRITES = new HashMap<>();

	private DoaSprites() {}

	/**
	 * Creates a sprite, adds the created sprite to
	 * {@link DoaSprites#ORIGINAL_SPRITES}, and returns the newly created sprite.
	 *
	 * @param spriteName unique name of the sprite that will be created
	 * @param spriteFile path to the sprite on the disk
	 * @return the newly created sprite in {@link DoaSprites#ORIGINAL_SPRITES} whose
	 *         name is spriteName
	 * @throws IOException if sprite cannot be loaded by DoaEngine
	 */
	public static BufferedImage createSprite(@NotNull final String spriteName, @NotNull final String spriteFile) throws IOException {
		final BufferedImage sp = ImageIO.read(DoaSprites.class.getResourceAsStream(spriteFile));
		ORIGINAL_SPRITES.put(spriteName, sp);
		if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(128).append(spriteName).append(" sprite instantiated."));
		} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine("DoaSprite instantiated.");
		}
		SHADED_SPRITES.put(spriteName, DoaLights.applyAmbientLight(sp));
		return toCompat(sp);
	}

	/**
	 * Creates a sprite from a sprite-sheet by cropping the sprite-sheet using the
	 * boundaries parameter, adds the created sprite to
	 * {@link DoaSprites#ORIGINAL_SPRITES}, and returns the newly created sprite.
	 *
	 * @param spriteName unique name of the sprite that will be created
	 * @param spriteFile path to the sprite-sheet from which the new sprite will be
	 *        cropped from
	 * @param boundaries bounding box of the sprite inside the sprite-sheet, i.e.
	 *        where the sprite is inside the sprite-sheet
	 * @return the newly created sprite in {@link DoaSprites#ORIGINAL_SPRITES} whose
	 *         name is spriteName
	 * @throws IOException if sprite-sheet cannot be loaded by DoaEngine
	 */
	public static BufferedImage createSpriteFromSpriteSheet(@NotNull final String spriteName, @NotNull final String spriteFile, @NotNull final Rectangle boundaries) throws IOException {
		final BufferedImage sp = ImageIO.read(DoaSprites.class.getResourceAsStream(spriteFile)).getSubimage(boundaries.x,
		        boundaries.y,
		        boundaries.width,
		        boundaries.height);
		ORIGINAL_SPRITES.put(spriteName, sp);
		if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(128).append(spriteName).append(" sprite instantiated."));
		} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine("DoaSprite instantiated.");
		}
		SHADED_SPRITES.put(spriteName, DoaLights.applyAmbientLight(sp));
		return toCompat(sp);
	}

	/**
	 * Conveniency method. This method exists solely to reduce the amount of typing
	 * required while trying to retrieve a sprite from DoaSprites class. Calling
	 * this method is equivalent to calling:
	 * DoaSprites.SHADED_SPRITES.get(spriteName);
	 *
	 * @param spriteName name of the sprite that is trying to be retrieved
	 * @return the sprite in {@link DoaSprites#SHADED_SPRITES} whose name is
	 *         spriteName
	 */
	public static BufferedImage getSprite(@NotNull final String spriteName) {
		if (areLightsContributing()) { return SHADED_SPRITES.get(spriteName); }
		return ORIGINAL_SPRITES.get(spriteName);
	}
	
	/**
	 * Conveniency method. This method exists solely to reduce the amount of typing
	 * required while trying to retrieve a sprite from DoaSprites class. Calling
	 * this method is equivalent to calling:
	 * DoaSprites.SHADED_SPRITES.get(spriteName);
	 *
	 * @param spriteName name of the sprite that is trying to be retrieved
	 * @return the sprite in {@link DoaSprites#SHADED_SPRITES} whose name is
	 *         spriteName
	 */
	public static String getSpriteName(@NotNull final BufferedImage sprite) {
	    for(Entry<String, BufferedImage> entry : ORIGINAL_SPRITES.entrySet()) {
	        if(entry.getValue() == sprite) {
	            return entry.getKey();
	        }
	    }
	    LOGGER.warning("Queried sprite is not registered to DoaSprites, returned empty string!");
	    return "";
	}

	/**
	 * Deep copies a DoaSprite
	 *
	 * @param sprite DoaSprite to deep copy
	 * @return the deep copy of the passed DoaSprites
	 */
	public static BufferedImage deepCopyDoaSprite(@NotNull final BufferedImage sprite) { return toCompat(sprite); }

	/**
	 * Creates a scaled version of a DoaSprite.A new DoaSprite will be returned as a
	 * result of this call. The returned sprite is not stored neither in
	 * {@link DoaSprites#ORIGINAL_SPRITES}, nor {@link DoaSprites#SHADED_SPRITES}.
	 *
	 * @param sprite DoaSprite to scale
	 * @param width width of the scaled DoaSprite
	 * @param height height of the scaled DoaSprite
	 * @return the scaled instance of the passed DoaSprite
	 */
	public static BufferedImage scale(@NotNull final BufferedImage sprite, final int width, final int height) {
		if (width < 0) {
			LOGGER.severe("DoaSprites cannot shrink a sprite beyond 0 width.");
			throw new IllegalArgumentException("width < 0");
		}
		if (height < 0) {
			LOGGER.severe("DoaSprites cannot shrink a sprite beyond 0 height.");
			throw new IllegalArgumentException("height < 0");
		}
		final Image i = sprite.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		if (i instanceof BufferedImage) { return (BufferedImage) i; }
		final BufferedImage bimage = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(i, 0, 0, null);
		bGr.dispose();
		return toCompat(bimage);
	}

	private static BufferedImage toCompat(final BufferedImage sp) {
		// obtain the current system graphical settings
		final GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		/*
		 * if image is already compatible and optimized for current system settings,
		 * simply return it
		 */
		if (sp.getColorModel().equals(gfxConfig.getColorModel())) { return sp; }

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
