package com.doa.ui;

import java.util.ArrayList;
import java.util.List;

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
 * @version 2.3
 */
public abstract class DoaUIContainer extends DoaUIComponent implements DoaUIContainerI {

	private static final long serialVersionUID = -485166756790159338L;

	private List<DoaUIComponent> components = new ArrayList<>();

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
	public void setActive(boolean active) {
		super.setActive(active);
		components.forEach(component -> component.setActive(active));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DoaUIComponent> add(DoaUIComponent component) {
		component.setParent(this);
		components.add(component);
		return components;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DoaUIComponent> remove(DoaUIComponent component) {
		component.setParent(null);
		components.remove(component);
		return components;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DoaUIComponent> removeAll() {
		components.clear();
		return components;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DoaUIComponent> getComponents() {
		return components;
	}
}