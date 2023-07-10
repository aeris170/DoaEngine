package doa.engine.scene.elements.renderers;

import com.google.errorprone.annotations.ForOverride;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.scene.DoaComponent;

/**
 * Renderer blueprint.
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public abstract class DoaRenderer extends DoaComponent {

	private static final long serialVersionUID = -4604905089488731022L;

	/**
	 * <strong>Do not call this method, merely override it.</strong> This method is
	 * called by DoaEngine when:
	 * <ol>
	 * <li>This renderer is added to a DoaObject,</li>
	 * <li>The DoaObject this renderer is added to, is added to a DoaScene,</li>
	 * <li>The DoaScene the DoaObject this renderer is added to, is loaded.</li>
	 * </ol>
	 * Use the appropriate functions in {@link DoaGraphicsFunctions} to draw onto
	 * the screen.
	 */
	@ForOverride
	public abstract void render();

	/**
	 * <strong>Do not call this method, merely override it.</strong> Called when
	 * {@link DoaComponent#enableDebugRender} is set to true.
	 */
	@ForOverride
	public void debugRender() {}
}
