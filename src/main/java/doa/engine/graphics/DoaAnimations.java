package doa.engine.graphics;

import static doa.engine.core.DoaGraphicsFunctions.areLightsContributing;
import static doa.engine.log.DoaLogger.LOGGER;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.validation.constraints.NotNull;

import doa.engine.log.LogLevel;

/**
 * Responsible for providing a Factory to create Animations for DoaEngine to
 * display. This class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 2.2
 * @version 3.0
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

	private DoaAnimations() {}

	/**
	 * Creates an animation from specifically .gif files, adds the created animation
	 * to{@code DoaAnimations.ORIGINAL_ANIMATIONS}, and returns the newly created
	 * animation.
	 *
	 * @param animationName unique name of the animation that will be created
	 * @param animationFile path to the animation(.gif) on the disk
	 * @param delay delay between each frame of animation in milliseconds
	 * @return the animation in {@code DoaSprites.ORIGINAL_ANIMATIONS} whose name is
	 *         animationName
	 * @throws IOException if sprite cannot be loaded by DoaEngine
	 */
	public static DoaAnimation createAnimation(@NotNull final String animationName, @NotNull final String animationFile, final long delay) throws IOException {
		final ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
		final ImageInputStream in = ImageIO.createImageInputStream(DoaSprites.class.getResourceAsStream(animationFile));
		reader.setInput(in);
		final List<BufferedImage> frames = new ArrayList<>();
		for (int i = 0, count = reader.getNumImages(true); i < count; i++) {
			frames.add(reader.read(i));
		}
		final DoaAnimation anim = new DoaAnimation(frames, delay);
		ORIGINAL_ANIMATIONS.put(animationName, anim);
		if (LOGGER.getLevel().compareTo(LogLevel.FINEST) >= 0) {
			LOGGER.finest(new StringBuilder(128).append(animationName).append(" animation instantiated with ").append(delay).append(" ms frame time."));
		} else if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(128).append(animationName).append(" animation instantiated."));
		} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine("DoaAnimation instantiated.");
		}
		SHADED_ANIMATIONS.put(animationName, DoaLights.applyAmbientLight(anim));
		return anim;
	}

	/**
	 * Creates an animation from a collection of DoaSprites, adds the created
	 * animation to{@code DoaAnimations.ORIGINAL_ANIMATIONS}, and returns the newly
	 * created animation.
	 *
	 * @param animationName unique name of the animation that will be created
	 * @param keyframes list that contains the frames of the animation
	 * @param delay delay between each frame of animation in milliseconds
	 * @return the animation in {@code DoaSprites.ORIGINAL_ANIMATIONS} whose name is
	 *         animationName
	 */
	public static DoaAnimation createAnimation(final String animationName, final List<BufferedImage> keyframes, final long delay) {
		final DoaAnimation anim = new DoaAnimation(keyframes, delay);
		ORIGINAL_ANIMATIONS.put(animationName, anim);
		if (LOGGER.getLevel().compareTo(LogLevel.FINEST) >= 0) {
			LOGGER.finest(new StringBuilder(128).append(animationName).append(" animation instantiated with ").append(delay).append("ms frame time."));
		} else if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(128).append(animationName).append(" animation instantiated."));
		} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine("DoaAnimation instantiated.");
		}
		SHADED_ANIMATIONS.put(animationName, DoaLights.applyAmbientLight(anim));
		return anim;
	}

	/**
	 * Conveniency method. This method exists solely to reduce the amount of typing
	 * required while trying to retrieve an animation from DoaSprites class. Calling
	 * this method is equivalent to calling:
	 * {@code DoaSprites.SHADED_ANIMATIONS.get(animationName)};
	 *
	 * @param animationName name of the animation that is trying to be retrieved
	 * @return the sprite in {@code DoaSprites.SHADED_ANIMATIONS} whose name is
	 *         spriteName
	 */
	public static DoaAnimation getAnimation(final String animationName) {
		if (areLightsContributing()) { return SHADED_ANIMATIONS.get(animationName); }
		return ORIGINAL_ANIMATIONS.get(animationName);
	}
}
