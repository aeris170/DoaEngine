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
		DoaLogger.LOGGER.info(new StringBuilder(512)
			.append("DoaObject: ")
			.append(getOwner().name)
			.append("\nPosition:\t" + getOwner().transform.position)
			.append("\nRotation:\t" + getOwner().transform.rotation)
			.append("\nScale:\t" + getOwner().transform.scale)
			.append("\n")
		);
	}
}
