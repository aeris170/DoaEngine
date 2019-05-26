package com.doa.ui;

import java.util.HashSet;
import java.util.Set;

import com.doa.engine.DoaHandler;
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
 * @version 2.5
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
		show();
		enable();
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
		show();
		enable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void show() {
		super.show();
		components.forEach(c -> c.show());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void hide() {
		super.hide();
		components.forEach(c -> c.hide());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enable() {
		super.enable();
		components.forEach(c -> c.enable());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disable() {
		super.disable();
		components.forEach(c -> c.disable());
	}

	/**
	 * Adds the specified component the this container.
	 * 
	 * @param component component to add to this container
	 */
	public void add(DoaUIComponent component) {
		if (component.getParent() != null) {
			component.getParent().remove(component);
		}
		components.add(component);
		component.setParent(this);
		component.isVisible = isVisible;
		component.isEnabled = isEnabled;
		DoaHandler.add(component);
	}

	/**
	 * Removes the specified component from this container.
	 * 
	 * @param component component to remove from this container
	 */
	public void remove(DoaUIComponent component) {
		if (components.contains(component)) {
			components.remove(component);
			component.setParent(null);
			component.isVisible = false;
			component.isEnabled = false;
			DoaHandler.remove(component);
		}
	}

	/**
	 * Removes all components from this container.
	 */
	public void removeAll() {
		components.forEach(c -> remove(c));
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