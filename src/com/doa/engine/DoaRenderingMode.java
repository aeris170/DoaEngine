package com.doa.engine;

/**
 * Specifies the rendering mode of {@code DoaEngine}. When {@code DoaEngine}'s
 * rendering mode is set, the rendering will take place prioritizing the
 * selected protocol. Else, the rendering will take place balancing speed and
 * quality a.k.a. {@link DoaRenderingMode#BALANCED}.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.6.1
 */
public enum DoaRenderingMode {

	QUALITY, BALANCED, SPEED, USER_DEFINED;
}