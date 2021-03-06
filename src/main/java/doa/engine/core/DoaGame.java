package doa.engine.core;

import java.lang.reflect.InvocationTargetException;

import com.google.errorprone.annotations.ForOverride;

import doa.engine.log.DoaLogger;

public abstract class DoaGame {

	private DoaEngine engine;
	private DoaEngineSettings eSettings;

	private DoaWindow window;
	private DoaWindowSettings wSettings;

	private boolean isLaunched = false;

	protected DoaGame() {
		eSettings = new DoaEngineSettings();
		wSettings = new DoaWindowSettings();
	}

	public static final void launch(final String... args) {
		String callerName = Thread.currentThread().getStackTrace()[2].getClassName();
		try {
			Class<?> caller = Class.forName(callerName);
			if (DoaGame.class.isAssignableFrom(caller)) {
				DoaGame a = (DoaGame) caller.getConstructor().newInstance();
				a.initialize(a.eSettings, a.wSettings, args);
				a.engine = new DoaEngine(a.eSettings, a.wSettings);
				a.window = new DoaWindow(a.wSettings, a.engine);
				a.isLaunched = true;
			} else {
				DoaLogger.getInstance().severe("The class " + callerName + " must subclass " + DoaGame.class.getCanonicalName() + ". Stop.");
				throw new ClassCastException();
			}
		} catch (ClassNotFoundException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			DoaLogger.getInstance().severe("An unforeseen error occured! Report the issue at GitHub please.");
			e.printStackTrace();
		} catch (InstantiationException e) {
			DoaLogger.getInstance().severe("The class " + callerName + " must be a concrete class! Stop.");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			DoaLogger.getInstance().severe("The class " + callerName + " must be declared as public! Stop.");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			DoaLogger.getInstance().severe("The class " + callerName + " can only have a public no argument constructor! Stop.");
			e.printStackTrace();
		}
	}

	/**
	 * @return has the game launched?
	 */
	public boolean isLaunched() { return isLaunched; }

	/**
	 * This method is used to configure DoaEngine and DoaWindow.
	 * 
	 * @param eSettings engine settings
	 * @param wSettings window settings
	 * @param args command line arguments
	 */
	@ForOverride
	public abstract void initialize(final DoaEngineSettings eSettings, final DoaWindowSettings wSettings, final String... args);
}
