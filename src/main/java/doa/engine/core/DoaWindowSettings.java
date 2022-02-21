package doa.engine.core;

import java.awt.Cursor;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;

import javax.validation.constraints.NotNull;

import doa.engine.maths.DoaVector;

/**
 * An object of this class is supplied as an argument to
 * {@link DoaGame#initialize(DoaEngineSettings, DoaWindowSettings, String...)}. Applications can
 * modify the contents of that object to customize the behavior of DoaWindow.
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public final class DoaWindowSettings {

	/**
	 * Title of the window.
	 */
	@NotNull
	public String TITLE = "DoaEngine " + DoaEngine.VERSION;

	/**
	 * Screen in which the window will be displayed.
	 */
	@NotNull
	public GraphicsDevice SCREEN = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

	/**
	 * Information related to window's resolution and refresh rate and etc.
	 */
	@NotNull
	public DoaDisplayMode DM = new DoaDisplayMode(SCREEN.getDisplayMode());
	public DoaVector RESOLUTION_OD = null;	
	public Integer REFRESH_RATE_OD = null;
	public Integer BPP_OD = null;
	public DoaWindowMode WM = DoaWindowMode.FULLSCREEN;

	/**
	 * 
	 */
	@NotNull
	public Cursor DEFAULT_CURSOR = Cursor.getDefaultCursor();

	/**
	 * 
	 */
	@NotNull
	public Cursor CROSSHAIR_CURSOR = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);

	/**
	 * 
	 */
	@NotNull
	public Cursor TEXT_CURSOR = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);

	/**
	 * 
	 */
	@NotNull
	public Cursor WAIT_CURSOR = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

	/**
	 * 
	 */
	@NotNull
	public Cursor SW_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);

	/**
	 * 
	 */
	@NotNull
	public Cursor SE_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);

	/**
	 * 
	 */
	@NotNull
	public Cursor NW_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);

	/**
	 * 
	 */
	@NotNull
	public Cursor NE_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);

	/**
	 * 
	 */
	@NotNull
	public Cursor N_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);

	/**
	 * 
	 */
	@NotNull
	public Cursor S_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);

	/**
	 * 
	 */
	@NotNull
	public Cursor W_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);

	/**
	 * 
	 */
	@NotNull
	public Cursor E_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);

	/**
	 * 
	 */
	@NotNull
	public Cursor HAND_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

	/**
	 * 
	 */
	@NotNull
	public Cursor MOVE_CURSOR = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);

	/**
	 * Icon of the window.
	 */
	public Image ICON = null;

	DoaWindowSettings() {}
}
