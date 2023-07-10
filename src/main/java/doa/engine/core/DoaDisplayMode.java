package doa.engine.core;

import static doa.engine.log.DoaLogger.LOGGER;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;

import doa.engine.maths.DoaVector;
import lombok.Getter;
import lombok.NonNull;

/**
 * Encapsulates the data of a display mode.
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version {@value DoaEngine#VERSION}
 */
public class DoaDisplayMode {

	/**
	 * Resolution of the display mode.
	 *
	 * @return The current value of this display mode's resolution.
	 */
	@Getter private DoaVector resolution = new DoaVector(1920, 1080);

	/**
	 * Refresh rate of the display mode.
	 *
	 * @return The current value of this display mode's refresh rate.
	 */
	@Getter private Integer refreshRate = 60;

	/**
	 * Bit depth of the display mode.
	 *
	 * @return The current value of this display mode's bit depth.
	 */
	@Getter private Integer bitDepth = 32;

	public DoaDisplayMode() {}
	public DoaDisplayMode(@NonNull final DoaVector resolution) { this.resolution = resolution; }
	public DoaDisplayMode(@NonNull final DoaVector resolution, @NonNull final Integer refreshRate) {
		this.resolution = resolution;
		this.refreshRate = refreshRate;
	}
	public DoaDisplayMode(@NonNull final DoaVector resolution, @NonNull final Integer refreshRate, @NonNull final Integer bitDepth) {
		this.resolution = resolution;
		this.refreshRate = refreshRate;
		this.bitDepth = bitDepth;
	}
	public DoaDisplayMode(DisplayMode mode) {
		resolution = new DoaVector(mode.getWidth(), mode.getHeight());
		refreshRate = mode.getRefreshRate();
		bitDepth = mode.getBitDepth();
	}

	public static DoaDisplayMode findDisplayModeWithCapabilities(@NonNull final GraphicsDevice screen, @NonNull final DoaVector resolution, @NonNull final Integer bitDepth, @NonNull final Integer refreshRate) {
		var modes = screen.getDisplayModes();
		for (var mode : modes) {
			if (resolution.x == mode.getWidth() && resolution.y == mode.getHeight() &&
				refreshRate == mode.getRefreshRate() &&
				bitDepth == mode.getBitDepth()) {
				return new DoaDisplayMode(mode);
			}
		}
		LOGGER.warning("Cannot find such display mode with params:" +
				" resolution=[" + resolution.x + ", " + resolution.y + "]" +
				" bitDepth=" + bitDepth +
				" refreshRate=" + refreshRate);
		return new DoaDisplayMode(screen.getDisplayMode());
	}
	public static DisplayMode toNativeDisplayMode(@NonNull final DoaDisplayMode mode) {
		return new DisplayMode((int)mode.getResolution().x, (int)mode.getResolution().y, mode.getBitDepth(), mode.getRefreshRate());
	}
}
