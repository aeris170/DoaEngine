package com.doa.ui.panel;

import com.doa.maths.DoaVectorF;
import com.doa.ui.DoaUIContainer;

/**
 * A bare bones UI component container.
 * 
 * @author Doga Oruc
 * @since DoaEngine 2.3
 * @version 2.3.2
 */
public abstract class DoaPanel extends DoaUIContainer {

	private static final long serialVersionUID = -955184547040405826L;

	/**
	 * Instantiates a panel with the specified bounds
	 * 
	 * @param x x coordinate of the top left corner of the UI component
	 * @param y y coordinate of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 */
	public DoaPanel(Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height);
	}

	/**
	 * Instantiates a panel with the specified bounds
	 * 
	 * @param position position of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 */
	public DoaPanel(DoaVectorF position, Integer width, Integer height) {
		super(position, width, height);
	}
}