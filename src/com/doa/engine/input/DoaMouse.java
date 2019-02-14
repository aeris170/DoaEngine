package com.doa.engine.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;

import javax.swing.event.MouseInputAdapter;

import com.doa.maths.DoaMath;

/**
 * Responsible for catching all fired mouse inputs at any logical frame. This
 * class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 1.1
 */
public final class DoaMouse extends MouseInputAdapter {

	/**
	 * Constructor.
	 */
	private DoaMouse() {}

	/**
	 * This construct ensures all mouse events in any logical frame to be captured
	 * and saved in this class' internal state. At any logical frame, a reference to
	 * any of this class' fields will return true if and only if that reference mask
	 * has been produced according to a captured mouse event. Informally, if a mouse
	 * button has been pressed at any time, at that time the respective field will
	 * be set to true, and when the same button has been released the same field
	 * will be set to false.
	 *
	 * @see javax.swing.event.MouseInputAdapter
	 */
	public static final MouseInputAdapter INPUT = new MouseInputAdapter() {

		@Override
		public synchronized void mousePressed(final MouseEvent e) {
			press[e.getButton()] = true;
			hold[e.getButton()] = true;
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public synchronized void mouseReleased(final MouseEvent e) {
			press[e.getButton()] = false;
			hold[e.getButton()] = false;
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public synchronized void mouseMoved(final MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public synchronized void mouseDragged(final MouseEvent e) {
			mouseMoved(e);
		}

		@Override
		public synchronized void mouseWheelMoved(final MouseWheelEvent e) {
			mouseZ -= e.getPreciseWheelRotation() * 0.5;
			mouseZ = DoaMath.clamp(mouseZ, 0.5, 10);
		}
	};

	static boolean[] press = new boolean[20];
	static boolean[] hold = new boolean[20];
	static double mouseX = 0;
	static double mouseY = 0;
	static double mouseZ = 4;

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except {@code DoaEngine}. If done otherwise,
	 * {@code DoaEngine} provides no guarantees on the consistency of the MouseEvent
	 * catching mechanism.
	 */
	public static synchronized void tick() {
		MB1 = press[MouseEvent.BUTTON1];
		MB2 = press[MouseEvent.BUTTON2];
		MB3 = press[MouseEvent.BUTTON3];
		Arrays.fill(press, false);
		MB1_HOLD = hold[MouseEvent.BUTTON1];
		MB2_HOLD = hold[MouseEvent.BUTTON2];
		MB3_HOLD = hold[MouseEvent.BUTTON3];
		X = mouseX;
		Y = mouseY;
		WHEEL = mouseZ;
	}

	/**
	 * @see java.awt.event.MouseEvent
	 */
	public static boolean MB1, MB2, MB3;
	public static boolean MB1_HOLD, MB2_HOLD, MB3_HOLD;
	public static double X = mouseX;
	public static double Y = mouseY;
	public static double WHEEL = mouseZ;
}