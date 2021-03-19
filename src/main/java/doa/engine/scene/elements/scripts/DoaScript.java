package doa.engine.scene.elements.scripts;

import com.google.errorprone.annotations.ForOverride;

import doa.engine.scene.DoaComponent;

/**
 * Script blueprint.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public abstract class DoaScript extends DoaComponent {

	private static final long serialVersionUID = -6820458805754686219L;

	/**
	 * <strong>Do not call this method.</strong> This method is called by DoaEngine
	 * when:
	 * <ol>
	 * <li>This script is added to a DoaObject,</li>
	 * <li>The DoaObject this script is added to, is added to a DoaScene,</li>
	 * <li>The DoaScene the DoaObject this script is added to, is loaded.</li>
	 * </ol>
	 * Compute the logic of DoaObjects inside this method.
	 */
	@ForOverride
	public abstract void tick();

}
