package com.doa.ui;

/**
 * Responsible for providing an interface for UI components {@code DoaEngine}
 * will process. It is encouraged by {@code DoaEngine} to extend
 * {@code DoaObject} and implement {@code DoaUIComponentI} if there is need for
 * highly customised UI components. Otherwise, subclasses of
 * {@code DoaUIComponentI} should be used. All UI components that implement this
 * interface <b>must</b> recalculate the component's bounds upon mutation of the
 * component.
 * 
 * @author Doga Oruc
 * @since DoaEngine 2.3
 * @version 2.3
 */
public interface DoaUIComponentI {

	/**
	 * Recalculates the bounds of this component.
	 */
	void recalibrateBounds();

	/**
	 * Sets the parent of this component.
	 * 
	 * @param parent the container which will hold this component
	 */
	void setParent(DoaUIContainer parent);

	/**
	 * Sets whether or not this component will be active or not.
	 * 
	 * @param active true, if the component should be rendered, false otherwise.
	 */
	public void setActive(boolean active);

	/**
	 * Return whether or not this component is active or not.
	 * 
	 * @return true if and only if, the component will be ticked and rendered on the
	 *         next frame(excluding function stubbing)
	 */
	public boolean getActive();

	/**
	 * Returns the container which holds this component.
	 * 
	 * @return parent of this component, or null if parent == null;
	 */
	public DoaUIContainer getParent();
}