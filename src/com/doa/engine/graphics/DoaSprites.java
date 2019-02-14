package com.doa.engine.graphics;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Responsible for providing a Factory to create Sprites for {@code DoaEngine}
 * to display. This class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.1
 * @version 1.1
 */
public final class DoaSprites {

	/**
	 * Collection that maps {@code String}s(spriteNames) to
	 * {@code BufferedImage}s(sprites).
	 */
	public static final Map<String, BufferedImage> ORIGINAL_SPRITES = new HashMap<>();

	/**
	 * Collection that maps {@code String}s(spriteNames) to
	 * {@code BufferedImage}s(sprites) that are shaded according to the lights the
	 * scene has.
	 */
	public static final Map<String, BufferedImage> SHADED_SPRITES = new HashMap<>();

	/**
	 * Constructor.
	 */
	private DoaSprites() {}

	/**
	 * Creates a sprite, adds the created sprite
	 * to{@code DoaSprites.ORIGINAL_SPRITES}, and returns the newly created sprite.
	 *
	 * @param spriteName name of the sprite that will be created
	 * @param spriteFile path to the sprite on the disk
	 * @return the sprite in {@code DoaSprites.ORIGINAL_SPRITES} which has a name of
	 *         spriteName
	 * @throws IOException if sprite cannot be loaded by {@code DoaEngine}
	 */
	public static BufferedImage createSprite(final String spriteName, final String spriteFile) throws IOException {
		final BufferedImage bf = ImageIO.read(DoaSprites.class.getResourceAsStream(spriteFile));
		ORIGINAL_SPRITES.put(spriteName, bf);
		DoaLights.applyAmbientLight(spriteName, bf);
		return bf;
	}

	/**
	 * Creates a sprite from a sprite-sheet by cropping the sprite-sheet using the
	 * boundaries parameter, adds the created sprite to
	 * {@code DoaSprites.ORIGINAL_SPRITES}, and returns the newly created sprite.
	 *
	 * @param spriteName name of the sprite that will be created
	 * @param spriteFile path to the sprite-sheet from which the new sprite will be
	 *        cropped from
	 * @param boundaries bounding box of the sprite inside the sprite-sheet, i.e.
	 *        where the sprite is inside the sprite-sheet
	 * @return the sprite in {@code DoaSprites.ORIGINAL_SPRITES} which has a name of
	 *         spriteName
	 * @throws IOException if sprite-sheet cannot be loaded by {@code DoaEngine}
	 */
	public static BufferedImage createSpriteFromSpriteSheet(final String spriteName, final String spriteFile, final Rectangle boundaries) throws IOException {
		final BufferedImage bf = ImageIO.read(DoaSprites.class.getResourceAsStream(spriteFile)).getSubimage(boundaries.x, boundaries.y, boundaries.width,
		        boundaries.height);
		ORIGINAL_SPRITES.put(spriteName, bf);
		DoaLights.applyAmbientLight(spriteName, bf);
		return bf;
	}

	/**
	 * Conveniency method. This method exists solely to reduce the amount of typing
	 * required while trying to retrieve a sprite from {@code DoaSprites} class.
	 * Calling this method is equivalent to calling:
	 * {@code DoaSprites.SHADED_SPRITES.get(spriteName)};
	 *
	 * @param spriteName name of the sprite that is trying to be retrieved
	 * @return the sprite in {@code DoaSprites.ORIGINAL_SPRITES} which has a name of
	 *         spriteName
	 */
	public static BufferedImage get(final String spriteName) {
		return SHADED_SPRITES.get(spriteName);
	}
}