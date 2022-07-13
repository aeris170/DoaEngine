package doa.engine.core;

import static doa.engine.log.DoaLogger.LOGGER;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import doa.engine.log.DoaLogger;
import doa.engine.maths.DoaVector;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

/**
 * Responsible for being the window for the game.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 3.0
 */
public final class DoaWindow {

	JFrame window;

	private String title;
	private GraphicsDevice screen;
	private DoaDisplayMode dm;
	private DoaVector resolutionOD;	
	private Integer refreshRateOD;
	private Integer bppOD;
	private DoaWindowMode wm;
	private Cursor[] cursors;
	private Image icon;

	DoaWindow(final DoaWindowSettings settings, final DoaEngine engine) {
		title = settings.TITLE;
		screen = settings.SCREEN;
		dm = settings.DM;
		resolutionOD = settings.RESOLUTION_OD;
		refreshRateOD = settings.REFRESH_RATE_OD;
		bppOD = settings.BPP_OD;
		wm = settings.WM;
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
		window.setCursor(cursors[0]);
		window.setIconImage(icon);
		window.setResizable(false);
		//window.setAlwaysOnTop(true);
		window.setAutoRequestFocus(true);
		window.setIgnoreRepaint(true);

		window.add(engine.surface);
		
		window.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Component c = (Component)e.getSource();
				engine.surface.setSize(c.getSize());
			}
		});

		setWindowMode(wm);
		if (resolutionOD == null) { resolutionOD = dm.getResolution(); }
		if (refreshRateOD == null) { refreshRateOD = dm.getRefreshRate(); }
		if (bppOD == null) { bppOD = dm.getBitDepth(); }
		val displayMode = DoaDisplayMode.findDisplayModeWithCapabilities(screen, resolutionOD, bppOD, refreshRateOD);
		setDisplayMode(displayMode);

		window.setVisible(true);
		engine.start();

		LOGGER.info("DoaWindow succesfully instantiated!");
	}
	
	public static GraphicsDevice[] getGraphicsDevices() { return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices(); }
	public GraphicsDevice getGraphicsDevice() { return screen; }
	public void setGraphicsDevice(@NonNull final GraphicsDevice screen) {
		this.screen.setFullScreenWindow(null);
		this.screen = screen;
		setWindowMode(wm);
		setDisplayMode(dm);
	}
	
	public DoaDisplayMode getDisplayMode() { return dm; }
	public void setDisplayMode(@NonNull final DoaDisplayMode dm) {
		if (wm == DoaWindowMode.WINDOWED) {
			window.setVisible(true);
			var insets = window.getInsets();
			int insetX = insets.left + insets.right;
			int insetY = insets.top + insets.bottom;
			window.setSize((int)dm.getResolution().x + insetX, (int)dm.getResolution().y + insetY);
			window.setVisible(false);
			this.dm = dm;
		} else if (screen.isDisplayChangeSupported()) {
			try {
				screen.setDisplayMode(DoaDisplayMode.toNativeDisplayMode(dm));
				this.dm = dm;
			} catch (IllegalArgumentException e) {
				LOGGER.severe("Invalid display mode!");
				e.printStackTrace();
			}
		} else {
			LOGGER.warning("Display does not support mode change!");
		}
	}
	
	public DoaWindowMode getWindowMode() { return wm; }
	public void setWindowMode(DoaWindowMode mode) {
		if (mode == DoaWindowMode.FULLSCREEN) {
			if (screen.isFullScreenSupported()) {
				wm = mode;
				screen.setFullScreenWindow(window);
			} else {
				LOGGER.config("Display does not support fullscreen windows, switched to borderless fullscreen");
				wm = DoaWindowMode.BORDERLESS_FULLSCREEN;
				window.setUndecorated(true);
				window.setExtendedState(Frame.MAXIMIZED_BOTH);
			}
		} else if (mode == DoaWindowMode.BORDERLESS_FULLSCREEN) {
			wm = mode;
			window.setUndecorated(true);
			window.setExtendedState(Frame.MAXIMIZED_BOTH);
		} else if (mode == DoaWindowMode.WINDOWED) {
			wm = mode;
		}
	}
}
