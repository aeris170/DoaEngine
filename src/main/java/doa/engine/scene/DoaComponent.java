package doa.engine.scene;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.google.errorprone.annotations.ForOverride;

public abstract class DoaComponent implements Serializable {

	private static final long serialVersionUID = -4729258438998530587L;

	protected boolean isEnabled = true;
	DoaObject owner;

	@NotNull
	public String name;

	/**
	 * Debug flag. Handled internally for components.Programmers who choose to
	 * directly extend from DoaComponent, must handle this properly.
	 */
	public boolean debugRender = false;

	protected DoaComponent() {
		name = getClass().getSimpleName();
		init();
	}

	/**
	 * De-facto constructor of DoaComponent. Programmers should override this method
	 * instead of creating a constructor. This method is called by the contructor of
	 * DoaComponent upon instantiation.
	 */
	@ForOverride
	protected void init() {}

	/**
	 * Called when this DoaComponent is successfully added to a DoaObject.
	 * 
	 * @param o the DoaObject this DoaComponent added to
	 */
	@ForOverride
	protected void onAdd(DoaObject o) {}

	/**
	 * Called when this DoaComponent is successfully removed from a DoaObject.
	 * 
	 * @param o the DoaObject this DoaComponent is removed from
	 */
	@ForOverride
	protected void onRemove(DoaObject o) {}

	/**
	 * Enabled components participate in logic/rendering. This is handled internally
	 * for pre-made scripts/renderers. Programmers who choose to directly extend
	 * from DoaComponent, must handle this properly.
	 * 
	 * @return true if component is enabled
	 */
	public boolean isEnabled() { return isEnabled; }

	/**
	 * Enables this DoaComponent.
	 */
	public void enable() { isEnabled = true; }

	/**
	 * Disables this DoaComponent.
	 */
	public void disable() { isEnabled = false; }

	/**
	 * @return the DoaObject this DoaComponent is added to.
	 */
	public DoaObject getOwner() { return owner; }
}
