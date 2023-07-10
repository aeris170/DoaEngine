package doa.engine.scene.elements.scripts;

import static doa.engine.input.DoaMouse.MB1;
import static doa.engine.input.DoaMouse.MB1_HOLD;
import static doa.engine.input.DoaMouse.MB1_RELEASE;
import static doa.engine.input.DoaMouse.MB2;
import static doa.engine.input.DoaMouse.MB2_HOLD;
import static doa.engine.input.DoaMouse.MB2_RELEASE;
import static doa.engine.input.DoaMouse.MB3;
import static doa.engine.input.DoaMouse.MB3_HOLD;
import static doa.engine.input.DoaMouse.MB3_RELEASE;
import static doa.engine.input.DoaMouse.X;
import static doa.engine.input.DoaMouse.Y;

import com.google.errorprone.annotations.ForOverride;

/**
 * A simple mouse control. Should be extended, and have its required methods
 * overriden to use.
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public abstract class DoaMouseAdapter extends DoaScript {

	private static final long serialVersionUID = 9003250762496961157L;

	private boolean isMouseInside = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tick() {
		if (MB1)			{ onMouse1Click(); }
		if (MB1_HOLD)		{ onMouse1Hold(); }
		if (MB1_RELEASE)	{ onMouse1Release(); }
		if (MB2)			{ onMouse2Click(); }
		if (MB2_HOLD)		{ onMouse2Hold(); }
		if (MB2_RELEASE)	{ onMouse2Release(); }
		if (MB3)			{ onMouse3Click(); }
		if (MB3_HOLD)		{ onMouse3Hold(); }
		if (MB3_RELEASE)	{ onMouse3Release(); }
		if (getOwner().transform.getPreciseBounds().contains(X, Y)) {
			if (isMouseInside) {
				onMouseHover();
			} else {
				isMouseInside = true;
				onMouseEnter();
			}
		} else if (isMouseInside) {
			isMouseInside = false;
			onMouseExit();
		}
	}

	@ForOverride
	public void onMouse1Click() {}

	@ForOverride
	public void onMouse1Hold() {}

	@ForOverride
	public void onMouse1Release() {}

	@ForOverride
	public void onMouse2Click() {}

	@ForOverride
	public void onMouse2Hold() {}

	@ForOverride
	public void onMouse2Release() {}

	@ForOverride
	public void onMouse3Click() {}

	@ForOverride
	public void onMouse3Hold() {}

	@ForOverride
	public void onMouse3Release() {}

	@ForOverride
	public void onMouseEnter() {}

	@ForOverride
	public void onMouseHover() {}

	@ForOverride
	public void onMouseExit() {}
}
