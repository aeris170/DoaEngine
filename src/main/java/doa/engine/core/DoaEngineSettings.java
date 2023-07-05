package doa.engine.core;

import java.awt.Color;
import java.awt.RenderingHints;
import java.util.Map;

import javax.validation.constraints.NotNull;

import doa.engine.maths.DoaVector;

/**
 * An object of this class is supplied as an argument to
 * {@link DoaGame#initialize(DoaEngineSettings, DoaWindowSettings, String...)}.
 * Applications can modify the contents of that object to customize the behavior
 * of DoaEngine.
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public final class DoaEngineSettings {

	/**
	 * Number of updates per second, default value is 240. Increasing this value
	 * will increase the granularity of time at the cost of FPS.
	 */
	public int TICK_RATE = 240;

	/**
	 * Background color of the render canvas. Default value is {@link Color#BLACK}.
	 */
	@NotNull
	public Color CLEAR_COLOR = Color.BLACK;

	/**
	 * Rendering mode of DoaEngine. Default value is
	 * {@link DoaRenderingMode#BALANCED}.
	 *
	 * @see DoaRenderingMode
	 */
	@NotNull
	public DoaRenderingMode RENDERING_MODE = DoaRenderingMode.BALANCED;

	/**
	 * Set this flag to true to make DoaEngine render axis helpers. Make sure this
	 * flag is false when shipping your application!
	 */
	public boolean AXIS_HELPERS = false;

	/**
	 * User defined rendering hints. Initially, this map has default values set by
	 * {@link RenderingHints}. If none of the three default rendering protocols suffice,
	 * programmers can set the {@link DoaEngineSettings#RENDERING_MODE} to
	 * {@link DoaRenderingMode#CUSTOM} and apply their own RenderingHints into this map.
	 * This way, the rendering will take place using the user defined Hints.
	 */
	@NotNull
	public final Map<RenderingHints.Key, Object> CUSTOM_HINTS = Map.of(
		RenderingHints.KEY_ALPHA_INTERPOLATION,	RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT,
		RenderingHints.KEY_ANTIALIASING,		RenderingHints.VALUE_ANTIALIAS_DEFAULT,
		RenderingHints.KEY_COLOR_RENDERING,		RenderingHints.VALUE_COLOR_RENDER_DEFAULT,
		RenderingHints.KEY_DITHERING,			RenderingHints.VALUE_DITHER_DEFAULT,
		RenderingHints.KEY_FRACTIONALMETRICS,	RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT,
		RenderingHints.KEY_INTERPOLATION,		RenderingHints.VALUE_INTERPOLATION_BILINEAR,
		RenderingHints.KEY_RENDERING,			RenderingHints.VALUE_RENDER_DEFAULT,
		RenderingHints.KEY_STROKE_CONTROL,		RenderingHints.VALUE_STROKE_DEFAULT,
		RenderingHints.KEY_TEXT_ANTIALIASING,	RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT
	);

	final Map<RenderingHints.Key, Object> QUALITY_HINTS = Map.of(
		RenderingHints.KEY_ALPHA_INTERPOLATION,	RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY,
		RenderingHints.KEY_ANTIALIASING,		RenderingHints.VALUE_ANTIALIAS_ON,
		RenderingHints.KEY_COLOR_RENDERING,		RenderingHints.VALUE_COLOR_RENDER_QUALITY,
		RenderingHints.KEY_DITHERING,			RenderingHints.VALUE_DITHER_ENABLE,
		RenderingHints.KEY_FRACTIONALMETRICS,	RenderingHints.VALUE_FRACTIONALMETRICS_ON,
		RenderingHints.KEY_INTERPOLATION,		RenderingHints.VALUE_INTERPOLATION_BICUBIC,
		RenderingHints.KEY_RENDERING,			RenderingHints.VALUE_RENDER_QUALITY,
		RenderingHints.KEY_STROKE_CONTROL,		RenderingHints.VALUE_STROKE_NORMALIZE,
		RenderingHints.KEY_TEXT_ANTIALIASING,	RenderingHints.VALUE_TEXT_ANTIALIAS_ON
	);

	final Map<RenderingHints.Key, Object> BALANCED_HINTS = Map.of(
		RenderingHints.KEY_ALPHA_INTERPOLATION,	RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY,
		RenderingHints.KEY_ANTIALIASING,		RenderingHints.VALUE_ANTIALIAS_ON,
		RenderingHints.KEY_COLOR_RENDERING,		RenderingHints.VALUE_COLOR_RENDER_QUALITY,
		RenderingHints.KEY_DITHERING,			RenderingHints.VALUE_DITHER_DISABLE,
		RenderingHints.KEY_FRACTIONALMETRICS,	RenderingHints.VALUE_FRACTIONALMETRICS_OFF,
		RenderingHints.KEY_INTERPOLATION,		RenderingHints.VALUE_INTERPOLATION_BILINEAR,
		RenderingHints.KEY_RENDERING,			RenderingHints.VALUE_RENDER_QUALITY,
		RenderingHints.KEY_STROKE_CONTROL,		RenderingHints.VALUE_STROKE_PURE,
		RenderingHints.KEY_TEXT_ANTIALIASING,	RenderingHints.VALUE_TEXT_ANTIALIAS_ON
	);

	final Map<RenderingHints.Key, Object> SPEED_HINTS = Map.of(
		RenderingHints.KEY_ALPHA_INTERPOLATION,	RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED,
		RenderingHints.KEY_ANTIALIASING,		RenderingHints.VALUE_ANTIALIAS_OFF,
		RenderingHints.KEY_COLOR_RENDERING,		RenderingHints.VALUE_COLOR_RENDER_SPEED,
		RenderingHints.KEY_DITHERING,			RenderingHints.VALUE_DITHER_DISABLE,
		RenderingHints.KEY_FRACTIONALMETRICS,	RenderingHints.VALUE_FRACTIONALMETRICS_OFF,
		RenderingHints.KEY_INTERPOLATION,		RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR,
		RenderingHints.KEY_RENDERING,			RenderingHints.VALUE_RENDER_SPEED,
		RenderingHints.KEY_STROKE_CONTROL,		RenderingHints.VALUE_STROKE_PURE,
		RenderingHints.KEY_TEXT_ANTIALIASING,	RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
	);

	/**
	 * The reference resolution DoaEngine will use when scaling the game window and
	 * its contents. The default value is 1920x1080.
	 */
	@NotNull
	public DoaVector REFERENCE_RESOLUTION = new DoaVector(1920, 1080);

	DoaEngineSettings() {}
}
