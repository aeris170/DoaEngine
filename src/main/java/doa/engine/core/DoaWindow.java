package doa.engine.core;

import static doa.engine.log.DoaLogger.LOGGER;

import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Responsible for being the window for the game.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 3.0
 */
public final class DoaWindow {

	private JFrame window;

	private String title;
	private GraphicsDevice screen;
	private DisplayMode dm;
	private Cursor[] cursors;
	private Image icon;

	DoaWindow(final DoaWindowSettings settings, final DoaEngine engine) {
		title = settings.TITLE;
		screen = settings.SCREEN;
		dm = settings.DM;
		cursors = new Cursor[14];
		cursors[0] = settings.DEFAULT_CURSOR;
		cursors[1] = settings.CROSSHAIR_CURSOR;
		cursors[2] = settings.TEXT_CURSOR;
		cursors[3] = settings.WAIT_CURSOR;
		cursors[4] = settings.SW_RESIZE_CURSOR;
		cursors[5] = settings.SE_RESIZE_CURSOR;
		cursors[6] = settings.NW_RESIZE_CURSOR;
		cursors[7] = settings.NE_RESIZE_CURSOR;
		cursors[8] = settings.N_RESIZE_CURSOR;
		cursors[9] = settings.S_RESIZE_CURSOR;
		cursors[10] = settings.W_RESIZE_CURSOR;
		cursors[11] = settings.E_RESIZE_CURSOR;
		cursors[12] = settings.HAND_CURSOR;
		cursors[13] = settings.MOVE_CURSOR;
		icon = settings.ICON;

		window = new JFrame();
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setLocationByPlatform(true);
		window.setTitle(title);
		window.setUndecorated(true);
		window.setCursor(cursors[0]);
		window.setIconImage(icon);
		window.setResizable(false);
		window.setAlwaysOnTop(true);
		window.setAutoRequestFocus(true);
		window.setIgnoreRepaint(true);

		window.add(engine.surface);

		if (screen.isFullScreenSupported()) {
			screen.setFullScreenWindow(window);
		} else {
			window.setExtendedState(Frame.MAXIMIZED_BOTH);
		}
		if (screen.isDisplayChangeSupported()) {
			try {
				screen.setDisplayMode(dm);
			} catch (IllegalArgumentException e) {
				LOGGER.severe("Invalid display mode!");
				e.printStackTrace();
			}
		}
		engine.start();

		LOGGER.info("DoaWindow succesfully instantiated!");
	}
}
