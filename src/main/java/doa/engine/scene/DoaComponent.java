package doa.engine.scene;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public abstract class DoaComponent implements Serializable {

	private static final long serialVersionUID = -4729258438998530587L;

	protected boolean isEnabled = true;
	DoaObject owner;

	@NotNull
	public String name;

	protected DoaComponent() {
		name = getClass().getSimpleName();
		init();
	}

	public void init() {}

	public boolean isEnabled() { return isEnabled; }

	public void enable() { isEnabled = true; }

	public void disable() { isEnabled = false; }

	public DoaObject getOwner() { return owner; }
}
