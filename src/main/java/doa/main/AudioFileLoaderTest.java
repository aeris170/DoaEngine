package doa.main;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioFileLoaderTest {

	public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		try (
		    final AudioInputStream stream = AudioSystem.getAudioInputStream(
		            new File("C:\\Users\\Doga\\Music\\DragonForce - Inhuman Rampage\\05 - Body Breakdown.mp3"))) {
			AudioFormat sourceFormat = stream.getFormat();
			AudioFormat pcm = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
			AudioInputStream converted = AudioSystem.getAudioInputStream(pcm, stream);
			final Clip clip = AudioSystem.getClip();
			clip.open(converted);
			clip.loop(0);
			while (true);
		}
	}
}
