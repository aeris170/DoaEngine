package doa.engine.scene.elements.scripts;

import doa.engine.log.DoaLogger;

/**
 * Debug script. Logs out the position, rotation and scale of the transform of
 * attached DoaObject.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaDebugTransform extends DoaScript {

	private static final long serialVersionUID = -4163825021894901306L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tick() {
		DoaLogger.LOGGER.newLine();
		DoaLogger.LOGGER.info("Position:\t" + getOwner().transform.position);
		DoaLogger.LOGGER.info("Rotation:\t" + getOwner().transform.rotation);
		DoaLogger.LOGGER.info("Scale:\t" + getOwner().transform.scale);
	}

}
