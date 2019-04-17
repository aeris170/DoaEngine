package com.doa.ui.action;

/**
 * This is a functional interface that is used to utilise the core UI components
 * {@code DoaEngine} provides. Implementors define a single function called
 * {@code void execute(void)}.
 * 
 * @author Doga Oruc
 * @since DoaEngine 2.3
 * @version 2.3
 */
@FunctionalInterface
public interface DoaUIAction {

	/**
	 * Does an action.
	 */
	public void execute();
}