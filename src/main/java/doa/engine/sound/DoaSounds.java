package doa.engine.sound;

import static doa.engine.log.DoaLogger.LOGGER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.validation.constraints.NotNull;

import doa.engine.log.LogLevel;

/**
 * Responsible for providing a Factory to create DoaAudioClips for DoaEngine to
 * play. This class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.1
 * @version 3.0
 */
public final class DoaSounds {

	/**
	 * Collection that maps Strings(clipNames) to DoaSoundClips.
	 */
	public static final Map<String, DoaSoundClip> SOUND_CLIPS = new HashMap<>();

	/**
	 * Global volume of the DoaSounds system. [0, 1] linear.
	 */
	static float GlobalVolume = 1;

	/**
	 * Constructor.
	 */
	private DoaSounds() {}

	/**
	 * Sets the global volume level. The parameter newVolume must be in the range
	 * [0, 1]. When newVolume = 0, volume is mute. When newVolume = 1, volume is
	 * max. When newVolume = 0.5, volume is midpoint of mute and max.
	 *
	 * @param newVolume the new value of the global volume level
	 * @throws IllegalArgumentException if newVolume is not in range [0, 1]
	 */
	public static void setGlobalVolume(final float newVolume) {
		if (newVolume > 1.0 || newVolume < 0.0) {
			LOGGER.severe("Volume level must be in range [0, 1].");
			throw new IllegalArgumentException("newVolume is not in range [0, 1]");
		}
		GlobalVolume = newVolume;
		for (final Entry<String, DoaSoundClip> entry : DoaSounds.SOUND_CLIPS.entrySet()) {
			entry.getValue().setVolume(entry.getValue().getVolume());
		}
		LOGGER.fine(new StringBuilder(33).append("Global Volume level is now ").append(newVolume * 100).append("%."));
	}

	/**
	 * Creates a new DoaSoundClip, adds the created clip
	 * to{@code DoaSounds.SOUND_CLIPS}, and returns the newly created clip.
	 *
	 * @param soundName unique name of the sprite that will be created
	 * @param soundFile path to the sprite on the disk
	 * @return the DoaSoundClip in {@code DoaSounds.SOUND_CLIPS} which has a name of
	 *         soundFile
	 * @throws IOException if file cannot be loaded by DoaEngine
	 * @throws UnsupportedAudioFileException if the file does not contain valid data
	 *         of a recognized filetype and format. Recognized filetypes include,
	 *         but are not limited to; .mp3, .wav and .ogg.
	 * @throws LineUnavailableException if a line cannot be opened because it is
	 *         unavailable. This situation arises most commonly when a requested
	 *         line is already in use by another application.
	 */
	public static DoaSoundClip createSoundClip(@NotNull final String soundName, @NotNull final String soundFile) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		final DoaSoundClip sound = new DoaSoundClip();

		try (final AudioInputStream stream = AudioSystem.getAudioInputStream(DoaSounds.class.getResourceAsStream(soundFile))) {
			AudioFormat sourceFormat = stream.getFormat();
			AudioFormat pcm = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
			AudioInputStream converted = AudioSystem.getAudioInputStream(pcm, stream);
			final Clip clip = AudioSystem.getClip();
			clip.open(converted);
			sound.setClip(clip);
			SOUND_CLIPS.put(soundName, sound);
			if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(64).append(soundName).append(" sound clip instantiated."));
			} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
				LOGGER.fine("DoaSoundClip instantiated.");
			}
		}
		return sound;
	}

	/**
	 * Conveniency method. This method exists solely to reduce the amount of typing
	 * required while trying to retrieve a DoaSoundClip from DoaSounds class.
	 * Calling this method is equivalent to calling:
	 * {@code DoaSounds.SOUND_CLIPS.get(soundName)};
	 *
	 * @param soundName name of the sprite that is trying to be retrieved
	 * @return the DoaSoundClip in {@code DoaSounds.SOUND_CLIPS} which has a name of
	 *         soundName
	 */
	public static DoaSoundClip getSoundClip(final String soundName) { return SOUND_CLIPS.get(soundName); }
}
