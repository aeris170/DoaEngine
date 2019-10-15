package com.doa.ui;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.validation.constraints.NotNull;

import com.doa.engine.Internal;
import com.doa.engine.scene.DoaObject;
import com.doa.engine.scene.DoaScene;
import com.doa.maths.DoaVectorF;

/**
 * {@code DoaUIContainer} is the root of the UI component container hierarchy. Every UI component
 * container have {@code DoaUIContainer} as a superclass. It is encouraged to add
 * {@code DoaUIComponent}s to {@code DoaUIContainer}s after instantiating them. When a UI component
 * container becomes active, all components the container holds also become active.
 *
 * @author Doga Oruc
 * @since DoaEngine 2.3
 * @version 2.7
 */
public abstract class DoaUIContainer extends DoaUIComponent {

	private static final long serialVersionUID = -485166756790159338L;

	private List<DoaUIComponent> components = new CopyOnWriteArrayList<>();

	/**
	 * Instantiates a UI component container with the specified bounds
	 *
	 * @param x x coordinate of the top left corner of the UI component
	 * @param y y coordinate of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 */
	public DoaUIContainer(final float x, final float y, final int width, final int height) {
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
	public DoaUIContainer(@NotNull final DoaVectorF position, final int width, final int height) {
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
		components.forEach(DoaUIComponent::show);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void hide() {
		super.hide();
		components.forEach(DoaUIComponent::hide);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enable() {
		super.enable();
		components.forEach(DoaUIComponent::enable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disable() {
		super.disable();
		components.forEach(DoaUIComponent::disable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Internal
	public void setScene(final DoaScene scene) {
		super.setScene(scene);
		components.forEach(scene::add);
	}

	/**
	 * Adds the specified component the this container.
	 *
	 * @param component component to add to this container
	 */
	public void add(final DoaUIComponent component) {
		if (!components.contains(component)) {
			if (component.getParent() != null) {
				component.getParent().remove(component);
			}
			components.add(component);
			components.sort(Comparator.comparing(DoaObject::getzOrder));
			component.setParent(this);
			component.isVisible = isVisible;
			component.isEnabled = isEnabled;
			if (scene != null) {
				scene.add(component);
			}
		}
	}

	/**
	 * Removes the specified component from this container.
	 *
	 * @param component component to remove from this container
	 */
	public void remove(final DoaUIComponent component) {
		if (components.contains(component)) {
			components.remove(component);
			component.setParent(null);
			component.isVisible = false;
			component.isEnabled = false;
			if (scene != null) {
				scene.remove(component);
			}
		}
	}

	/**
	 * Removes all components from this container.
	 */
	public void removeAll() {
		components.forEach(components::remove);
	}

	/**
	 * Returns the list of components held by this container.
	 *
	 * @return components inside this container
	 */
	public List<DoaUIComponent> getComponents() {
		return components;
	}

	/**
	 * Returns whether the passed component is added to this container.
	 *
	 * @param component component whose presence is to be tested
	 * @return true if and only if getComponents.contains(component) == true
	 */
	public boolean contains(final DoaUIComponent component) {
		return components.contains(component);
	}
}