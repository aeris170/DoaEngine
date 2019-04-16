package com.doa.ui;

import java.util.HashSet;
import java.util.Set;

import com.doa.maths.DoaVectorF;

/**
 * {@code DoaUIContainer} is the root of the UI component container hierarchy.
 * Every UI component container have {@code DoaUIContainer} as a superclass. It
 * is encouraged to add {@code DoaUIComponent}s to {@code DoaUIContainer}s after
 * instantiating them. When a UI component container becomes active, all
 * components the container holds also become active.
 * 
 * @author Doga Oruc
 * @since DoaEngine 2.3
 * @version 2.3.2
 */
public abstract class DoaUIContainer extends DoaUIComponent {

	private static final long serialVersionUID = -485166756790159338L;

	private Set<DoaUIComponent> components = new HashSet<>();

	/**
	 * Instantiates a UI component container with the specified bounds
	 * 
	 * @param x x coordinate of the top left corner of the UI component
	 * @param y y coordinate of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 */
	public DoaUIContainer(Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height);
	}

	/**
	 * Instantiates a UI component container with the specified bounds
	 * 
	 * @param position position of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 */
	public DoaUIContainer(DoaVectorF position, Integer width, Integer height) {
		super(position, width, height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void show() {
		super.show();
		components.forEach(component -> component.show());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void hide() {
		super.hide();
		components.forEach(component -> component.hide());
	}

	/**
	 * Adds the specified component the this container.
	 * 
	 * @param component component to add to this container
	 * @return the added component
	 */
	public Set<DoaUIComponent> add(DoaUIComponent component) {
		component.setParent(this);
		components.add(component);
		return components;
	}

	/**
	 * Removes the specified component from this container.
	 * 
	 * @param component component to remove from this container
	 * @return the removed component
	 */
	public Set<DoaUIComponent> remove(DoaUIComponent component) {
		component.setParent(null);
		components.remove(component);
		return components;
	}

	/**
	 * Removes all components from this container, effectively rendering it empty.
	 * 
	 * @return components inside this container after before the removal
	 */
	public Set<DoaUIComponent> removeAll() {
		components.clear();
		return components;
	}

	/**
	 * Returns the list of components held by this container.
	 * 
	 * @return components inside this container
	 */
	public Set<DoaUIComponent> getComponents() {
		return components;
	}

	/**
	 * Returns whether the passed component is added to this container.
	 * 
	 * @param component component whose presence is to be tested
	 * @return true if and only if getComponents.contains(component) == true
	 */
	public boolean contains(DoaUIComponent component) {
		return components.contains(component);
	}
}