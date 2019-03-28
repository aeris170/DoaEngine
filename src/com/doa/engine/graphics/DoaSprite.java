package com.doa.engine.graphics;

import java.awt.image.BufferedImage;

/**
 * Wrapper object for {@code BufferedImage}s, exists to provide further support
 * for sprite based drawing and animations. In order to facilitate this class'
 * objects, one must use the factory methods inside {@code DoaSprites} class.
 * 
 * @author Doga Oruc
 * @since DoaEngine 2.2
 * @version 2.2
 * @see com.doa.engine.graphics.DoaSprites
 */
public class DoaSprite extends BufferedImage {

	DoaSprite(BufferedImage bf) {
		super(bf.getWidth(), bf.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.createGraphics().drawImage(bf, 0, 0, null);
	}
}
