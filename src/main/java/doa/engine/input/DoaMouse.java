package doa.engine.input;

import static doa.engine.log.DoaLogger.LOGGER;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import com.google.errorprone.annotations.DoNotCall;

import doa.engine.Internal;
import doa.engine.maths.DoaMath;

/**
 * Responsible for catching all fired mouse inputs at any logical frame. This
 * class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 3.0
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
	@Internal
	public static final MouseInputAdapter INPUT = new MouseInputAdapter() {

		@Override
		public synchronized void mousePressed(final MouseEvent e) {
			press[e.getButton()] = true;
			hold[e.getButton()] = true;
			mouseX = e.getX();
			mouseY = e.getY();
			if (SwingUtilities.isLeftMouseButton(e)) {
				LOGGER.finest("Left mouse button is pressed.");
			} else if (SwingUtilities.isRightMouseButton(e)) {
				LOGGER.finest("Right mouse button is pressed.");
			} else if (SwingUtilities.isMiddleMouseButton(e)) {
				LOGGER.finest("Middle mouse button is pressed.");
			} else {
				LOGGER.finest("A mouse button is pressed.");
			}
		}

		@Override
		public synchronized void mouseReleased(final MouseEvent e) {
			press[e.getButton()] = false;
			hold[e.getButton()] = false;
			release[e.getButton()] = true;
			mouseX = e.getX();
			mouseY = e.getY();
			if (SwingUtilities.isLeftMouseButton(e)) {
				LOGGER.finest("Left mouse button is released.");
			} else if (SwingUtilities.isRightMouseButton(e)) {
				LOGGER.finest("Right mouse button is released.");
			} else if (SwingUtilities.isMiddleMouseButton(e)) {
				LOGGER.finest("Middle mouse button is released.");
			} else {
				LOGGER.finest("A mouse button is released.");
			}
		}

		@Override
		public synchronized void mouseMoved(final MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public synchronized void mouseDragged(final MouseEvent e) { mouseMoved(e); }

		@Override
		public synchronized void mouseWheelMoved(final MouseWheelEvent e) { mouseZ -= e.getPreciseWheelRotation() * 0.05; }
	};

	static boolean[] press = new boolean[20];
	static boolean[] hold = new boolean[20];
	static boolean[] release = new boolean[20];
	static float mouseX = 0;
	static float mouseY = 0;
	static float mouseZ = 1;
	static float minZ = Float.MIN_VALUE;
	static float maxZ = Float.MAX_VALUE;

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except DoaEngine. If done otherwise, DoaEngine
	 * provides no guarantees on the consistency of the MouseEvent catching
	 * mechanism.
	 */
	@Internal
	@DoNotCall
	public static synchronized void tick() {
		MB1 = press[MouseEvent.BUTTON1];
		MB2 = press[MouseEvent.BUTTON2];
		MB3 = press[MouseEvent.BUTTON3];
		Arrays.fill(press, false);
		MB1_HOLD = hold[MouseEvent.BUTTON1];
		MB2_HOLD = hold[MouseEvent.BUTTON2];
		MB3_HOLD = hold[MouseEvent.BUTTON3];
		MB1_RELEASE = release[MouseEvent.BUTTON1];
		MB2_RELEASE = release[MouseEvent.BUTTON2];
		MB3_RELEASE = release[MouseEvent.BUTTON3];
		Arrays.fill(release, false);
		X = mouseX;
		Y = mouseY;
		mouseZ = DoaMath.clamp(mouseZ, minZ, maxZ);
		WHEEL = mouseZ;
	}

	/**
	 * Clamps the WHEEL property with the parameters.
	 *
	 * @param min minimum value for the WHEEL property
	 * @param max max value for the WHEEL property
	 */
	public static void clampWheel(final float min, final float max) {
		minZ = min;
		maxZ = max;
	}

	/**
	 * @see java.awt.event.MouseEvent
	 */
	public static volatile boolean MB1, MB2, MB3;
	public static volatile boolean MB1_HOLD, MB2_HOLD, MB3_HOLD;
	public static volatile boolean MB1_RELEASE, MB2_RELEASE, MB3_RELEASE;
	public static volatile float X = mouseX;
	public static volatile float Y = mouseY;
	public static volatile float WHEEL = mouseZ;
}
