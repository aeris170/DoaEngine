package com.doa.ui.button;

import java.util.ArrayList;
import java.util.List;

import com.doa.engine.DoaEngine;
import com.doa.engine.input.DoaMouse;
import com.doa.engine.log.DoaLogger;
import com.doa.engine.log.LogLevel;
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
 * @version 2.6.1
 */
public abstract class DoaButton extends DoaUIComponent {

	private static final long serialVersionUID = 8880492250633733283L;

	private static final DoaLogger LOGGER = DoaLogger.getInstance();

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
	 * Indicates if the mouse pointer is inside this buttons bounds and if mouse
	 * button 1 is pressed and released. As described by the <a href=
	 * "https://docs.oracle.com/javase/10/docs/api/java/awt/Shape.html#def_insideness">
	 * definition of insideness</a>.
	 * 
	 * @see java.awt.Shape
	 */
	protected boolean click;

	/**
	 * The events the button will do when it is clicked.
	 */
	protected transient List<DoaUIAction> actionList = new ArrayList<>();

	/**
	 * Instantiates a button with the bounds and action.
	 * 
	 * @param x x coordinate of the top left corner of the UI component
	 * @param y y coordinate of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 */
	public DoaButton(Float x, Float y, Integer width, Integer height) {
		super(x, y, width, height);
	}

	/**
	 * Instantiates a button with the bounds and action.
	 * 
	 * @param position position of the top left corner of the UI component
	 * @param width width of the UI component
	 * @param height height of the UI component
	 */
	public DoaButton(DoaVectorF position, Integer width, Integer height) {
		super(position, width, height);
	}

	@Override
	public void tick() {
		if (isEnabled) {
			hover = false;
			click = false;
			if (getBounds().contains(DoaMouse.X, DoaMouse.Y)) {
				hover = true;
				if (DoaMouse.MB1_HOLD) {
					click = true;
					if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
						LOGGER.finer(new StringBuilder(32).append(getClass().getName()).append(" click."));
					}
				}
				if (DoaMouse.MB1_RELEASE) {
					actionList.forEach(action -> {
						action.execute();
						if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINEST) >= 0) {
							LOGGER.finer(new StringBuilder(32).append(action.getClass().getName()).append(" executed."));
						}
					});
					DoaMouse.MB1 = false;
					DoaMouse.MB1_HOLD = false;
					DoaMouse.MB1_RELEASE = false;
				}
			}
		}
	}

	/**
	 * Adds the specified action to this button's action list. When a button is
	 * clicked, all actions in the button's action list will be executed.
	 * 
	 * @param action action to add to this button's action list
	 * @return true (as specified by Collection.add(E))
	 */
	public boolean addAction(DoaUIAction action) {
		return actionList.add(action);
	}

	/**
	 * Removes the specified action to this button's action list.
	 * 
	 * @param action action to be removed from this button's action list, if present
	 * @return true if this button's action list contained the specified action
	 */
	public boolean removeAction(DoaUIAction action) {
		return actionList.remove(action);
	}
}