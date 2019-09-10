package com.doa.engine.sound;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.doa.engine.DoaEngine;
import com.doa.engine.log.DoaLogger;
import com.doa.engine.log.LogLevel;

/**
 * Responsible for providing a Factory to create {@code DoaAudioClip}s for {@code DoaEngine} to
 * play. This class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.1
 * @version 2.7
 */
public final class DoaSounds {

	private static final DoaLogger LOGGER = DoaLogger.getInstance();

	/**
	 * Collection that maps Strings(clipNames) to DoaSoundClips.
	 */
	public static final Map<String, DoaSoundClip> SOUND_CLIPS = new HashMap<>();

	/**
	 * Constructor.
	 */
	private DoaSounds() {}

	/**
	 * Sets the global volume level. The parameter newVolume must be in the range [0, 1]. When newVolume
	 * = 0, volume is mute. When newVolume = 1, volume is max. When newVolume = 0.5, volume is midpoint
	 * of mute and max.
	 *
	 * @param newVolume the new value of the global volume level
	 * @throws IllegalArgumentException if newVolume is not in range [0, 1]
	 */
	public static void setGlobalVolume(final float newVolume) {
		if (newVolume > 1.0 || newVolume < 0.0) {
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.SEVERE) >= 0) {
				LOGGER.severe("Volume level must be in range [0, 1].");
			}
			throw new IllegalArgumentException("newVolume is not in range [0, 1]");
		}
		final float dB = (float) (Math.log(newVolume) / Math.log(10) * 20) + 6;
		for (final Entry<String, DoaSoundClip> entry : DoaSounds.SOUND_CLIPS.entrySet()) {
			final FloatControl gainControl = (FloatControl) entry.getValue().getClip().getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(dB);
		}
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine(new StringBuilder(32).append("Volume level is now ").append(newVolume * 100).append("%."));
		}
	}

	/**
	 * Creates a new {@code DoaSoundClip}, adds the created clip to{@code DoaSounds.SOUND_CLIPS}, and
	 * returns the newly created clip.
	 *
	 * @param soundName name of the sprite that will be created
	 * @param soundFile path to the sprite on the disk
	 * @return the {@code DoaSoundClip} in {@code DoaSounds.SOUND_CLIPS} which has a name of soundFile
	 * @throws IOException if file cannot be loaded by {@code DoaEngine}
	 * @throws UnsupportedAudioFileException if the file does not contain valid data of a recognised
	 *         filetype and format.
	 * @throws LineUnavailableException if a line cannot be opened because it is unavailable. This
	 *         situation arises most commonly when a requested line is already in use by another
	 *         application.
	 */
	public static DoaSoundClip createSoundClip(final String soundName, final String soundFile) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		final DoaSoundClip sound = new DoaSoundClip();
		try (final AudioInputStream stream = AudioSystem.getAudioInputStream(DoaSounds.class.getResource(soundFile))) {
			final Clip clip = AudioSystem.getClip();
			clip.open(stream);
			sound.setClip(clip);
			SOUND_CLIPS.put(soundName, sound);
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(64).append(soundName).append(" sound clip instantiated."));
			} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINE) >= 0) {
				LOGGER.fine("DoaSoundClip instantiated.");
			}
		}
		return sound;
	}

	/**
	 * Conveniency method. This method exists solely to reduce the amount of typing required while
	 * trying to retrieve a {@code DoaSoundClip} from {@code DoaSounds} class. Calling this method is
	 * equivalent to calling: {@code DoaSounds.SOUND_CLIPS.get(soundName)};
	 *
	 * @param soundName name of the sprite that is trying to be retrieved
	 * @return the {@code DoaSoundClip} in {@code DoaSounds.SOUND_CLIPS} which has a name of soundName
	 */
	public static DoaSoundClip get(final String soundName) {
		return SOUND_CLIPS.get(soundName);
	}
}
