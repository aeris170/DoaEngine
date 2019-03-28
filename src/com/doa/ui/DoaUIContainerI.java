package com.doa.ui;

import java.util.List;

/**
 * Responsible for providing an interface for UI component containers
 * {@code DoaEngine} will process. It is encouraged by {@code DoaEngine} to
 * extend {@code DoaObject} and implement {@code DoaUIContainerI} if there is
 * need for highly customised UI component containers. Otherwise, subclasses of
 * {@code DoaUIContainerI} should be used.
 * 
 * @author Doga Oruc
 * @since DoaEngine 2.3
 * @version 2.3
 */
public interface DoaUIContainerI {

	/**
	 * Returns the list of components held by this container.
	 * 
	 * @return components inside this container
	 */
	public List<DoaUIComponent> getComponents();

	/**
	 * Adds the specified component the this container.
	 * 
	 * @param component component to add to this container
	 * @return components inside this container after this method finish
	 */
	public List<DoaUIComponent> add(DoaUIComponent component);

	/**
	 * Removes the specified component from this container.
	 * 
	 * @param component component to remove from this container
	 * @return components inside this container after this method finish
	 */
	public List<DoaUIComponent> remove(DoaUIComponent component);

	/**
	 * Removes all components from this container, effectively rendering it empty.
	 * 
	 * @return components inside this container after this method finish, or in this
	 *         case, an empty list
	 */
	public List<DoaUIComponent> removeAll();
}
