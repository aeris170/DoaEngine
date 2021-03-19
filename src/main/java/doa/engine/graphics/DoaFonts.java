package doa.engine.graphics;

import static doa.engine.log.DoaLogger.LOGGER;
import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import doa.engine.log.LogLevel;

/**
 * Responsible for providing a Factory to create Fonts for DoaEngine to use.
 * This class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public final class DoaFonts {

	private static final GraphicsEnvironment GE = getLocalGraphicsEnvironment();

	/**
	 * Collection that maps font names to fonts.
	 */
	public static final Map<String, Font> FONTS = new HashMap<>();

	private DoaFonts() {}

	/**
	 * Creates a fonts, adds the created fonts to {@link DoaFonts#FONTS}, and
	 * returns the newly created fonts.
	 *
	 * @param fontName unique name of the font that will be created
	 * @param fontFile path to the font on the disk
	 * @return the newly created font in {@link DoaFonts#FONTS} whose name is
	 *         fontName
	 * @throws FontFormatException if the supplied font is bad
	 * @throws IOException if the font cannot be loaded by DoaEngine
	 */
	public static Font createFont(@NotNull final String fontName, @NotNull final String fontFile) throws FontFormatException, IOException {
		Font customFont = Font.createFont(Font.TRUETYPE_FONT, DoaFonts.class.getResourceAsStream(fontFile)).deriveFont(72f);
		if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(128).append(fontName).append(" font instantiated."));
		} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine("DoaFont instantiated.");
		}
		boolean success = GE.registerFont(customFont);
		if (!success) {
			LOGGER.warning("Couldn't register the font "
			        + fontName
			        + ". Font creation via new Font may fail. Use DoaFonts#get(String) to retrieve your custom font! See GraphicsEnvironment#registerFont(Font) for details.");
		}
		FONTS.put(fontName, customFont);
		return customFont;
	}

	/**
	 * Conveniency method. This method exists solely to reduce the amount of typing
	 * required while trying to retrieve a font from DoaFonts class. Calling this
	 * method is equivalent to calling: DoaFonts.FONTS.get(fontName);
	 *
	 * @param fontName name of the font that is trying to be retrieved
	 * @return the font in {@link DoaFonts#FONTS} whose name is fontName
	 */
	public static Font getFont(@NotNull final String fontName) { return FONTS.get(fontName); }
}
