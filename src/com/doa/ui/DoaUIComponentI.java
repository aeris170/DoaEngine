package com.doa.ui;

/**
 * Responsible for providing an interface for UI components {@code DoaEngine}
 * will process. It is encouraged by {@code DoaEngine} to extend
 * {@code DoaObject} and implement {@code DoaUIComponentI} if there is need for
 * highly customised UI components. Otherwise, subclasses of
 * {@code DoaUIComponentI} should be used. The only method this interface has is
 * {@code void recalibrateBounds(void)} at the moment. All UI components that
 * implement this interface must recalculate the component's bounds upon
 * mutation of the component.
 * 
 * @author Doga Oruc
 * @since DoaEngine 2.3
 * @version 2.3
 */
public interface DoaUIComponentI {

	void recalibrateBounds();
}