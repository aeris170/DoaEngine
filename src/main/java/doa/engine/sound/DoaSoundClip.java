package doa.engine.sound;

import static doa.engine.log.DoaLogger.LOGGER;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import doa.engine.task.DoaTasker;

/**
 * Easy use version of Clip. Houses a Clip object and some necessary methods and
 * utilities to quickly play, pause and stop arbitrary sound clips.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.1
 * @version 3.0
 * @see Clip
 */
public final class DoaSoundClip {

	/**
	 * Constant for looping forever.
	 */
	public static final int FOREVER = Clip.LOOP_CONTINUOUSLY;

	/**
	 * The actual sound clip. This is the object that stores all the information
	 * about what sound this object will play.
	 *
	 * @see Clip
	 */
	private Clip clip;

	/**
	 * Position of the playhead in terms of microseconds. 1 second = 1.000.000
	 * microseconds.
	 */
	private long microsecondPosition;

	/**
	 * Volume of this sound clip. Always in the range of [0, 1].
	 */
	private float volume;

	/**
	 * Loops the sound clip this object will play count times. If the clip is to be
	 * looped forever (or until it is stopped), use {@link DoaSoundClip#FOREVER}.
	 *
	 * @param count loop count
	 */
	public void loop(final int count) {
		if (clip != null) {
			DoaTasker.executeNow(() -> {
				final Clip c = getClip();
				synchronized (c) {
					c.stop();
					c.setFramePosition(0);
					c.loop(count);
				}
			});
		}
	}

	/**
	 * Plays the sound clip this object will play from the beginning.
	 */
	public void play() {
		if (clip != null) {
			DoaTasker.executeNow(() -> {
				final Clip c = getClip();
				synchronized (c) {
					c.stop();
					c.setFramePosition(0);
					c.start();
				}
			});
		}
	}

	/**
	 * Stops the sound clip. This method will set the microsecond position of the
	 * clip to 0.
	 */
	public void stop() {
		if (clip != null) {
			DoaTasker.executeNow(() -> {
				final Clip c = getClip();
				synchronized (c) {
					setMicrosecondPosition(0);
					c.stop();
				}
			});
		}
	}

	/**
	 * Pauses the sound clip. This method will not change the microsecond position
	 * of the clip.
	 */
	public void pause() {
		if (clip != null) {
			DoaTasker.executeNow(() -> {
				final Clip c = getClip();
				synchronized (c) {
					setMicrosecondPosition(c.getMicrosecondPosition());
					c.stop();
				}
			});
		}
	}

	/**
	 * Resumes the sound clip from the microsecond position.
	 */
	public void resume() {
		if (clip != null) {
			DoaTasker.executeNow(() -> {
				final Clip c = getClip();
				synchronized (c) {
					c.setMicrosecondPosition(getMicrosecondPosition());
					c.start();
				}
			});
		}
	}

	/**
	 * Returns the position of the playhead in terms of microseconds. 1 second =
	 * 1.000.000 microseconds.
	 * 
	 * @return playhead position
	 */
	public long getMicrosecondPosition() { return microsecondPosition; }

	/**
	 * Sets the position of the playhead in terms of microseconds. 1 second =
	 * 1.000.000 microseconds. For example, passing 10.000.000 into this method will
	 * result in this clip forwarding to the 10th second.
	 * 
	 * @param microsecondPosition new playhead position
	 */
	public void setMicrosecondPosition(final long microsecondPosition) { this.microsecondPosition = microsecondPosition; }

	/**
	 * Returns the volume at which this clip is/will be playing at. The returned
	 * value is always between 0 and 1 inclusive.
	 * 
	 * @return the volume of the clip
	 */
	public float getVolume() { return volume; }

	/**
	 * Sets the volume level. The parameter volume must be in the range [0, 1]. When
	 * volume = 0, clip is muted. When volume = 1, clip is playing at max volume.
	 * The volume is linear, not logarithmic. <br>
	 * <br>
	 * There appears to be a slight delay for the volume to change from its old
	 * value to its new value. This is possibly due to the internal workings of
	 * javax.sound.sampled package and has nothing to do with DoaEngine. Programmers
	 * should not rely on fast volume switches, instead they should make your sound
	 * designer do that in their DAW.
	 * 
	 * @param volume the new value of the volume level
	 * @throws IllegalArgumentException if newVolume is not in range [0, 1]
	 */
	public void setVolume(final float volume) {
		if (volume > 1.0 || volume < 0.0) {
			LOGGER.severe("Volume level must be in range [0, 1].");
			throw new IllegalArgumentException("volume is not in range [0, 1]");
		}
		this.volume = volume;
		final float dB = (float) (Math.log(volume * DoaSounds.GlobalVolume) / Math.log(10) * 20);
		setDecibels(dB);
	}

	void setDecibels(float dB) { ((FloatControl) getClip().getControl(FloatControl.Type.MASTER_GAIN)).setValue(dB); }

	Clip getClip() { return clip; }

	void setClip(final Clip clip) {
		this.clip = clip;
		setVolume(1);
	}
}
