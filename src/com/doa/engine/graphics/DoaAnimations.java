package com.doa.engine.graphics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * Responsible for providing a Factory to create Animations for
 * {@code DoaEngine} to display. This class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 2.2
 * @version 2.2
 */
public final class DoaAnimations {

	/**
	 * Collection that maps animation names to animations.
	 */
	public static final Map<String, DoaAnimation> ORIGINAL_ANIMATIONS = new HashMap<>();

	/**
	 * Collection that maps animation names to animations that are shaded according
	 * to the lights the scene has.
	 */
	public static final Map<String, DoaAnimation> SHADED_ANIMATIONS = new HashMap<>();

	/**
	 * Constructor.
	 */
	private DoaAnimations() {}

	/**
	 * Creates an animation from specifically .gif files, adds the created animation
	 * to{@code DoaAnimations.ORIGINAL_ANIMATIONS}, and returns the newly created
	 * animation.
	 *
	 * @param animationName name of the animation that will be created
	 * @param animationFile path to the animation(.gif) on the disk
	 * @param delay delay between each frame of animation in milliseconds
	 * @return the animation in {@code DoaSprites.ORIGINAL_ANIMATIONS} whose name is
	 *         animationName
	 * @throws IOException if sprite cannot be loaded by {@code DoaEngine}
	 */
	public static DoaAnimation createAnimation(final String animationName, final String animationFile, final long delay) throws IOException {
		ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
		ImageInputStream in = ImageIO.createImageInputStream(DoaSprites.class.getResourceAsStream(animationFile));
		reader.setInput(in);
		List<DoaSprite> frames = new ArrayList<>();
		for (int i = 0, count = reader.getNumImages(true); i < count; i++) {
			frames.add(new DoaSprite(reader.read(i)));
		}
		DoaAnimation anim = new DoaAnimation(frames, delay);
		ORIGINAL_ANIMATIONS.put(animationName, anim);
		DoaLights.applyAmbientLight(animationName, anim);
		return anim;
	}

	/**
	 * Conveniency method. This method exists solely to reduce the amount of typing
	 * required while trying to retrieve an animation from {@code DoaSprites} class.
	 * Calling this method is equivalent to calling:
	 * {@code DoaSprites.SHADED_ANIMATIONS.get(animationName)};
	 *
	 * @param animationName name of the animation that is trying to be retrieved
	 * @return the sprite in {@code DoaSprites.SHADED_ANIMATIONS} whose name is
	 *         spriteName
	 */
	public static DoaAnimation get(final String animationName) {
		return SHADED_ANIMATIONS.get(animationName);
	}
}
