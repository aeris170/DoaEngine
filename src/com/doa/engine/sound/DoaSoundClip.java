package com.doa.engine.sound;

import javax.sound.sampled.Clip;

/**
 * Easy use version of {@code Clip}. Houses a {@code Clip} object and some
 * necessary methods and utilities to quickly play, pause and stop arbitrary
 * sound clips.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.1
 * @version 1.1
 * @see Clip
 */
public class DoaSoundClip {

	/**
	 * The actual sound clip. This is the object that stores all the information
	 * about what sound will this object will play.
	 *
	 * @see Clip
	 */
	private Clip clip;

	/**
	 * Position of the playhead in terms of microseconds. 1 second = 1,000,000
	 * microseconds.
	 */
	private long microsecondPosition;

	/**
	 * Loops the sound clip this object will play count times. If count is 0, then
	 * invocation of this method will result in the same way as invocation of
	 * {@code loop(Clip.LOOP_CONTINUOUSLY);}
	 *
	 * @param count loop count
	 */
	public void loop(final int count) {
		if (clip != null) {
			new Thread(() -> {
				final Clip c = getClip();
				synchronized (c) {
					c.stop();
					c.setFramePosition(0);
					c.loop(count);
				}
			}).start();
		}
	}

	/**
	 * Stops the sound clip. This method will set the microsecond position of the
	 * clip to 0.
	 */
	public void stop() {
		if (clip != null) {
			new Thread(() -> {
				final Clip c = getClip();
				synchronized (c) {
					setMicrosecondPosition(0);
					c.stop();
				}
			}).start();
		}
	}

	/**
	 * Pauses the sound clip. This method will not change set the microsecond
	 * position of the clip.
	 */
	public void pause() {
		if (clip != null) {
			new Thread(() -> {
				final Clip c = getClip();
				synchronized (c) {
					setMicrosecondPosition(c.getMicrosecondPosition());
					c.stop();
				}
			}).start();
		}
	}

	/**
	 * Resumes the sound clip from the microsecond position.
	 */
	public void resume() {
		if (clip != null) {
			new Thread(() -> {
				final Clip c = getClip();
				synchronized (c) {
					c.setMicrosecondPosition(getMicrosecondPosition());
					c.start();
				}
			}).start();
		}
	}

	public Clip getClip() {
		return clip;
	}

	public long getMicrosecondPosition() {
		return microsecondPosition;
	}

	public void setClip(final Clip clip) {
		this.clip = clip;
	}

	public void setMicrosecondPosition(final long microsecondPosition) {
		this.microsecondPosition = microsecondPosition;
	}
}