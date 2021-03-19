package doa.engine.core;

/**
 * Specifies the rendering mode of DoaEngine. If DoaEngine's rendering mode is
 * set, the rendering will take place prioritizing the selected protocol. Else,
 * the rendering will take place balancing speed and quality a.k.a.
 * {@link DoaRenderingMode#BALANCED}.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 3.0
 */
public enum DoaRenderingMode {

	/**
	 * Maximize quality at the cost of performance: <br>
	 * <br>
	 * <table style="border: 1px solid white;">
	 * <caption>Quality</caption>
	 * <tr>
	 * <th style="border: 1px solid white;">ALPHA INTERPOLATION</th>
	 * <th style="border: 1px solid white;">QUALITY</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">ANTIALIASING</th>
	 * <th style="border: 1px solid white;">ON</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">COLOR RENDERING</th>
	 * <th style="border: 1px solid white;">QUALITY</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">DITHERING</th>
	 * <th style="border: 1px solid white;">ENABLED</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">FRACTIONALMETRICS</th>
	 * <th style="border: 1px solid white;">ON</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">INTERPOLATION</th>
	 * <th style="border: 1px solid white;">BICUBIC</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">RENDERING</th>
	 * <th style="border: 1px solid white;">QUALITY</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">STROKE CONTROL</th>
	 * <th style="border: 1px solid white;">NORMALIZE</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">TEXT ANTIALIASING</th>
	 * <th style="border: 1px solid white;">ON</th>
	 * </tr>
	 * </table>
	 */
	QUALITY,

	/**
	 * Balance out quality and performance: <br>
	 * <br>
	 * <table style="border: 1px solid white;">
	 * <caption>Balanced</caption>
	 * <tr>
	 * <th style="border: 1px solid white;">ALPHA INTERPOLATION</th>
	 * <th style="border: 1px solid white;">QUALITY</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">ANTIALIASING</th>
	 * <th style="border: 1px solid white;">ON</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">COLOR RENDERING</th>
	 * <th style="border: 1px solid white;">QUALITY</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">DITHERING</th>
	 * <th style="border: 1px solid white;">DISABLED</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">FRACTIONALMETRICS</th>
	 * <th style="border: 1px solid white;">OFF</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">INTERPOLATION</th>
	 * <th style="border: 1px solid white;">BILINEAR</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">RENDERING</th>
	 * <th style="border: 1px solid white;">QUALITY</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">STROKE CONTROL</th>
	 * <th style="border: 1px solid white;">PURE</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">TEXT ANTIALIASING</th>
	 * <th style="border: 1px solid white;">ON</th>
	 * </tr>
	 * </table>
	 */
	BALANCED,

	/**
	 * Maximize performance at the cost of quality: <br>
	 * <br>
	 * <table style="border: 1px solid white;">
	 * <caption>Speed</caption>
	 * <tr>
	 * <th style="border: 1px solid white;">ALPHA INTERPOLATION</th>
	 * <th style="border: 1px solid white;">SPEED</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">ANTIALIASING</th>
	 * <th style="border: 1px solid white;">OFF</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">COLOR RENDERING</th>
	 * <th style="border: 1px solid white;">SPEED</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">DITHERING</th>
	 * <th style="border: 1px solid white;">DISABLED</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">FRACTIONALMETRICS</th>
	 * <th style="border: 1px solid white;">OFF</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">INTERPOLATION</th>
	 * <th style="border: 1px solid white;">NEAREST NEIGHBOR</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">RENDERING</th>
	 * <th style="border: 1px solid white;">SPEED</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">STROKE CONTROL</th>
	 * <th style="border: 1px solid white;">PURE</th>
	 * </tr>
	 * <tr>
	 * <th style="border: 1px solid white;">TEXT ANTIALIASING</th>
	 * <th style="border: 1px solid white;">OFF</th>
	 * </tr>
	 * </table>
	 */
	SPEED,

	/**
	 * Set {@link DoaEngineSettings#RENDERING_MODE} to
	 * {@link DoaRenderingMode#CUSTOM} for this to be useful.
	 */
	CUSTOM;
}
