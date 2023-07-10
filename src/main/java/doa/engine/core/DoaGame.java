package doa.engine.core;

import static doa.engine.core.DoaGraphicsFunctions.set;

import java.lang.reflect.InvocationTargetException;

import com.google.errorprone.annotations.ForOverride;

import doa.engine.log.DoaLogger;

public abstract class DoaGame {

	public static DoaGame INSTANCE;

	public static DoaGame getInstance() {
		if(INSTANCE == null) {
			throw new IllegalStateException("Game hasn't launched, cannot get instance of game!");
		}
		return INSTANCE;
	}

	DoaEngine engine;
	private DoaEngineSettings eSettings;

	DoaWindow window;
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
				a.initializeEngine(a.eSettings, a.wSettings, args);
				set(null, a.eSettings.REFERENCE_RESOLUTION, a.wSettings.RESOLUTION_OD != null ? a.wSettings.RESOLUTION_OD : a.wSettings.DM.getResolution());
				a.initializeGame(a.eSettings, a.wSettings, args);
				a.engine = new DoaEngine(a.eSettings, a.wSettings);
				a.window = new DoaWindow(a.wSettings, a.engine);
				INSTANCE = a;
				a.isLaunched = true;
				a.engine.start();
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
	 * @return the frame count in the last 1,000,000,000 nanoseconds (second)
	 */
	public int getFPS() { return engine.fps; }

	/**
	 * @return the tick count in the last 1,000,000,000 nanoseconds (second)
	 */
	public int getTPS() { return engine.tps; }

	/**
	 * Exits the game by stopping the game thread and disposing of the game window.
	 * Note that this method does not terminate the Java Virtual Machine.
	 * Invocation of this method is a one way trip, DoaEngine will enter in an
	 * unrecoverable state after this method returns.
	 */
	public static synchronized void exit() {
		if (INSTANCE == null) {
			DoaLogger.getInstance().warning("DoaGame#exit is called but DoaGame has no instance. exit will have no effect. returned");
			return;
		}
		DoaEngine.running = false;
		INSTANCE.window.window.dispose();
		INSTANCE.onExit();
	}

	/**
	 * This method is used to configure DoaEngine and DoaWindow. Don't initialize
	 * your game inside this method as DoaEngine is not initialized until an
	 * invocation to {@link DoaGame#initializeEngine(DoaEngineSettings, DoaWindowSettings, String...)}
	 * is made. Keep your client initialization in {@link DoaGame#initializeEngine(DoaEngineSettings, DoaWindowSettings, String...)}
	 *
	 * @param eSettings engine settings
	 * @param wSettings window settings
	 * @param args command line arguments
	 */
	@ForOverride
	public abstract void initializeEngine(final DoaEngineSettings eSettings, final DoaWindowSettings wSettings, final String... args);

	/**
	 * This method is used to initialize the game elements. This method is called
	 * right after {@link DoaGame#initializeEngine(DoaEngineSettings, DoaWindowSettings, String...)}
	 * returns. The parameters passed to the method are the same parameters the
	 * programmer edited in {@link DoaGame#initializeEngine(DoaEngineSettings, DoaWindowSettings, String...)}.
	 * Since DoaEngine and its internals are already initialized, all the params
	 * this method receives should be treated as read only, as they have no further
	 * use for DoaEngine and it's internals.
	 *
	 * @param eSettings engine settings, effectively immutable
	 * @param wSettings window settings, effectively immutable
	 * @param args command line arguments, effectively immutable
	 */
	@ForOverride
	public abstract void initializeGame(final DoaEngineSettings eSettings, final DoaWindowSettings wSettings, final String... args);

	/**
	 * This method is called when DoaEngine exits the game. Perform your clean ups in this method.
	 */
	@ForOverride
	public abstract void onExit();
}
