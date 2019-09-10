package com.doa.engine.graphics;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Objects of this class are responsible for providing support for sprite based animations. In order
 * to facilitate this class' objects, one must use the factory methods inside {@code DoaAnimations}
 * class.
 *
 * @author Doga Oruc
 * @since DoaEngine 2.2
 * @version 2.7
 * @see com.doa.engine.graphics.DoaAnimations
 */
public class DoaAnimation {

	private List<BufferedImage> frames;
	private long delay;
	private int index = 0;

	DoaAnimation(final List<BufferedImage> frames, final long delay) {
		this.frames = frames;
		this.delay = delay;
	}

	/**
	 * @return the frames of this animation
	 */
	public List<BufferedImage> getFrames() {
		return frames;
	}

	/**
	 * @return the delay between each frame of this animation in milliseconds
	 */
	public long getDelay() {
		return delay;
	}

	/**
	 * @return the number of frames of this animation
	 */
	public int getFrameCount() {
		return frames.size();
	}

	/**
	 * Retrieves the sprite at sprite pointer.
	 *
	 * @return current sprite to render
	 */
	public BufferedImage current() {
		return frames.get((index + frames.size()) % frames.size());
	}

	/**
	 * Advances the sprite pointer to the next index, retrieves the sprite at that index; loops back to
	 * beginning if there are no more sprites on the buffer, and returns the sprite.
	 *
	 * @return the next sprite to render
	 */
	public BufferedImage next() {
		return frames.get((index++ + frames.size()) % frames.size());
	}

	/**
	 * Resets the next sprite to render to the first frame in the animation.
	 */
	public void reset() {
		index = 0;
	}

	/**
	 * Returns the current index of the sprite to render.
	 *
	 * @return the sprite to renders index
	 */
	public int getIndex() {
		return index;
	}
}
