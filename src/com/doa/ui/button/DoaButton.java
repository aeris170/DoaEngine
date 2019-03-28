package com.doa.ui.button;

import com.doa.engine.input.DoaMouse;
import com.doa.maths.DoaVectorF;
import com.doa.ui.DoaUIComponent;
import com.doa.ui.action.DoaUIAction;

/**
 * A basic button. When hovered over, sets itself to be in "hover" state. When
 * clicked, sets itself to be in "click" state. Else, idle state. States are
 * denoted by 2 boolean fields.
 * 
 * @author Doga Oruc
 * @since DoaEngine 2.3
 * @version 2.3
 */
public abstract class DoaButton extends DoaUIComponent {

	private static final long serialVersionUID = 8880492250633733283L;

	/**
	 * Indicates if the mouse pointer is inside this buttons bounds. As described by
	 * the <a href=
	 * "https://docs.oracle.com/javase/10/docs/api/java/awt/Shape.html#def_insideness">
	 * definition of insideness</a>.
	 * 
	 * @see java.awt.Shape
	 */
	protected boolean hover;

	/**
	 * Indicates if the mouse pointer is inside this buttons bounds and if mouse is
	 * being pressed. As described by the <a href=
	 * "https://docs.oracle.com/javase/10/docs/api/java/awt/Shape.html#def_insideness">
	 * definition of insideness</a>.
	 * 
	 * @see java.awt.Shape
	 */
	protected boolean click;

	/**
	 * The event the button will do when it is clicked.
	 */
	protected transient DoaUIAction action = () -> {};

	/**
	 * Instantiates a button with the bounds and action.
	 * 
	 * @param x x coordinate of the top left corner of the UI component
	 * @param y y coordinate of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 * @param action action to execute upon click
	 */
	public DoaButton(Float x, Float y, Integer width, Integer height, DoaUIAction action) {
		super(x, y, width, height);
		if (action != null) {
			this.action = action;
		}
	}

	/**
	 * Instantiates a button with the bounds and action.
	 * 
	 * @param position position of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 * @param action action to execute upon click
	 */
	public DoaButton(DoaVectorF position, Integer width, Integer height, DoaUIAction action) {
		super(position, width, height);
		if (action != null) {
			this.action = action;
		}
	}

	@Override
	public void tick() {
		hover = false;
		click = false;
		if (getBounds().contains(DoaMouse.X, DoaMouse.Y)) {
			hover = true;
			if (DoaMouse.MB1) {
				click = true;
				action.execute();
			}
		}
	}
}