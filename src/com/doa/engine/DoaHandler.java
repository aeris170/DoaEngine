package com.doa.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaMouse;
import com.doa.ui.DoaUIComponent;
import com.doa.ui.DoaUIContainer;

/**
 * Responsible for keeping track of all {@code DoaObject}s. All
 * {@code DoaObject}s and its respective child-classes are automatically added
 * to {@code DoaHandler} after they are created with invoking
 * {@link DoaHandler#instantiateDoaObject(Class, Object...)}, and have their
 * {@link DoaObject#tick()} and {@link DoaObject#render(DoaGraphicsContext)}
 * methods called by {@code DoaEngine}. This class is static, therefore has no
 * objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.3.2
 */
public final class DoaHandler {

	private static final List<DoaObject> STATIC_BACK_OBJECTS = new CopyOnWriteArrayList<>();
	private static final List<DoaObject> BACK_OBJECTS = new CopyOnWriteArrayList<>();
	private static final List<DoaObject> GAME_OBJECTS = new CopyOnWriteArrayList<>();
	private static final List<DoaObject> FRONT_OBJECTS = new CopyOnWriteArrayList<>();
	private static final List<DoaObject> STATIC_FRONT_OBJECTS = new CopyOnWriteArrayList<>();
	private static final List<DoaObject> UI_COMPONENTS = new CopyOnWriteArrayList<>();

	private static final ExecutorService EXECUTOR = DoaEngine.MULTI_THREAD_ENABLED ? Executors.newCachedThreadPool() : Executors.newSingleThreadExecutor();

	private static final String EXCEPTION_MSG = "DoaEngine ==> UNHANDLED EXCEPTION @ DoaHandler";

	/**
	 * Constructor.
	 */
	private DoaHandler() {}

	/**
	 * Instantiates a new {@code DoaObject} and adds it to {@code DoaHandler}.
	 *
	 * @param clazz the class of the {@code DoaObject} to be instantiated
	 * @param constructorArgs arguments for the constructor
	 * @param <T> type of the class
	 * @return the instantiated {@code DoaObject} or null if instantiation is
	 *         unsuccessful
	 */
	public static <T extends DoaObject> T instantiateDoaObject(final Class<T> clazz, final Object... constructorArgs) {
		if (constructorArgs == null || constructorArgs.length == 0) {
			try {
				final T d = clazz.newInstance();
				add(d);
				return d;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException ex) {
				System.err.println(EXCEPTION_MSG);
				ex.printStackTrace();
				return null;
			}
		}
		final Constructor<?>[] constructors = clazz.getConstructors();
		for (final Constructor<?> c : constructors) {
			if (c.getParameterCount() == constructorArgs.length) {
				final Class<?>[] parameterTypes = c.getParameterTypes();
				boolean isSuitable = true;
				for (int i = 0; i < parameterTypes.length; i++) {
					if (!parameterTypes[i].isAssignableFrom(constructorArgs[i].getClass())) {
						isSuitable = false;
						break;
					}
				}
				if (isSuitable) {
					try {
						Object o = c.newInstance(constructorArgs);
						if (clazz.isInstance(o)) {
							final T d = clazz.cast(o);
							add(d);
							return d;
						}
						return null;
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
						System.err.println(EXCEPTION_MSG);
						ex.printStackTrace();
						return null;
					}
				}
			}
		}
		try {
			throw new InstantiationException();
		} catch (final InstantiationException ex) {
			System.err.println(EXCEPTION_MSG);
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except {@code DoaEngine}. If done otherwise,
	 * {@code DoaEngine} provides no guarantees on the consistency of the internal
	 * state of any {@code DoaObject} that is present in the {@code DoaHandler} at
	 * the time of this method's illegal invocation.
	 */
	public static void tick() {
		final List<Callable<Void>> tasks = new ArrayList<>();
		for (final DoaObject o : UI_COMPONENTS) {
			tasks.add(() -> {
				DoaUIComponent component = (DoaUIComponent) o;
				if (component.isVisible()) {
					o.tick();
					if (o.getBounds().contains(DoaMouse.X, DoaMouse.Y)) {
						if (DoaMouse.MB1) {
							DoaMouse.MB1 = false;
							DoaMouse.MB1_HOLD = false;
							DoaMouse.MB1_RELEASE = false;
						}
						if (DoaMouse.MB2) {
							DoaMouse.MB2 = false;
							DoaMouse.MB2_HOLD = false;
							DoaMouse.MB2_RELEASE = false;
						}
						if (DoaMouse.MB3) {
							DoaMouse.MB3 = false;
							DoaMouse.MB3_HOLD = false;
							DoaMouse.MB3_RELEASE = false;
						}
					}
				}
				return null;
			});
		}
		for (final DoaObject o : STATIC_BACK_OBJECTS) {
			tasks.add(() -> {
				o.tick();
				return null;
			});
		}
		for (final DoaObject o : BACK_OBJECTS) {
			tasks.add(() -> {
				o.tick();
				return null;
			});
		}
		for (final DoaObject o : GAME_OBJECTS) {
			tasks.add(() -> {
				o.tick();
				return null;
			});
		}
		for (final DoaObject o : FRONT_OBJECTS) {
			tasks.add(() -> {
				o.tick();
				return null;
			});
		}
		for (final DoaObject o : STATIC_FRONT_OBJECTS) {
			tasks.add(() -> {
				o.tick();
				return null;
			});
		}
		try {
			EXECUTOR.invokeAll(tasks);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
	}

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except {@code DoaEngine}. {@code DoaEngine} provides
	 * no guarantees on the quality and consistency of rendering of any
	 * {@code DoaObject} that is present in the {@code DoaHandler} at the time of
	 * this method's illegal invocation.
	 *
	 * @param g the graphics context of {@code DoaEngine}
	 */
	public static void renderStaticBackground(final DoaGraphicsContext g) {
		final List<Callable<Void>> tasks = new ArrayList<>();
		for (final DoaObject o : STATIC_BACK_OBJECTS) {
			tasks.add(() -> {
				o.render(g);
				return null;
			});
		}
		try {
			EXECUTOR.invokeAll(tasks);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
	}

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except {@code DoaEngine}. {@code DoaEngine} provides
	 * no guarantees on the quality and consistency of rendering of any
	 * {@code DoaObject} that is present in the {@code DoaHandler} at the time of
	 * this method's illegal invocation.
	 *
	 * @param g the graphics context of {@code DoaEngine}
	 */
	public static void renderBackground(final DoaGraphicsContext g) {
		final List<Callable<Void>> tasks = new ArrayList<>();
		for (final DoaObject o : BACK_OBJECTS) {
			tasks.add(() -> {
				o.render(g);
				return null;
			});
		}
		try {
			EXECUTOR.invokeAll(tasks);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
	}

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except {@code DoaEngine}. {@code DoaEngine} provides
	 * no guarantees on the quality and consistency of rendering of any
	 * {@code DoaObject} that is present in the {@code DoaHandler} at the time of
	 * this method's illegal invocation.
	 *
	 * @param g the graphics context of {@code DoaEngine}
	 */
	public static void render(final DoaGraphicsContext g) {
		final List<Callable<Void>> tasks = new ArrayList<>();
		for (final DoaObject o : GAME_OBJECTS) {
			tasks.add(() -> {
				o.render(g);
				return null;
			});
		}
		try {
			EXECUTOR.invokeAll(tasks);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
	}

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except {@code DoaEngine}. {@code DoaEngine} provides
	 * no guarantees on the quality and consistency of rendering of any
	 * {@code DoaObject} that is present in the {@code DoaHandler} at the time of
	 * this method's illegal invocation.
	 *
	 * @param g the graphics context of {@code DoaEngine}
	 */
	public static void renderForeground(final DoaGraphicsContext g) {
		final List<Callable<Void>> tasks = new ArrayList<>();
		for (final DoaObject o : FRONT_OBJECTS) {
			tasks.add(() -> {
				o.render(g);
				return null;
			});
		}
		try {
			EXECUTOR.invokeAll(tasks);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
	}

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except {@code DoaEngine}. {@code DoaEngine} provides
	 * no guarantees on the quality and consistency of rendering of any
	 * {@code DoaObject} that is present in the {@code DoaHandler} at the time of
	 * this method's illegal invocation.
	 *
	 * @param g the graphics context of {@code DoaEngine}
	 */
	public static void renderStaticForeground(final DoaGraphicsContext g) {
		final List<Callable<Void>> tasks = new ArrayList<>();
		for (final DoaObject o : STATIC_FRONT_OBJECTS) {
			tasks.add(() -> {
				o.render(g);
				return null;
			});
		}
		try {
			EXECUTOR.invokeAll(tasks);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
	}

	/**
	 * This method is required to be public, but should never be called explicitly
	 * by any class at any time except {@code DoaEngine}. {@code DoaEngine} provides
	 * no guarantees on the quality and consistency of rendering of any
	 * {@code DoaObject} that is present in the {@code DoaHandler} at the time of
	 * this method's illegal invocation.
	 *
	 * @param g the graphics context of {@code DoaEngine}
	 */
	public static void renderUI(final DoaGraphicsContext g) {
		final List<Callable<Void>> tasks = new ArrayList<>();
		for (final DoaObject o : UI_COMPONENTS) {
			tasks.add(() -> {
				if (o instanceof DoaUIContainer && ((DoaUIContainer) o).isVisible()) {
					o.render(g);
					((DoaUIContainer) o).getComponents().forEach(c -> c.render(g));
				} else if (o instanceof DoaUIComponent && ((DoaUIComponent) o).getParent() == null && ((DoaUIComponent) o).isVisible()) {
					o.render(g);
				}
				return null;
			});
		}
		try {
			EXECUTOR.invokeAll(tasks);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
	}

	/**
	 * Adds the passed {@code DoaObject} to {@code DoaHandler}. Normally, this is
	 * unneeded to invoke manually, as all {@code DoaObject}s and its respective
	 * child-classes are added to {@code DoaHandler} after they are created with
	 * {@link DoaHandler#instantiateDoaObject(Class, Object...)}
	 *
	 * @param o the {@code DoaObject} whose presence in {@code DoaHandler} is to be
	 *        ensured
	 * @return true if this collection changed as a result of the call, false
	 *         otherwise
	 */
	public static boolean add(final DoaObject o) {
		return findListAccordingToZOrder(o).add(o);
	}

	/**
	 * Removes the passed {@code DoaObject} from {@code DoaHandler}.
	 *
	 * @param o The {@code DoaObject} to be removed from {@code DoaHandler}, if
	 *        present
	 * @return true if an element was removed as a result of this call, false
	 *         otherwise
	 */
	public static boolean remove(final DoaObject o) {
		return findListAccordingToZOrder(o).remove(o);
	}

	/**
	 * Removes ALL of the {@code DoaObject}s from {@code DoaHandler}.
	 * {@code DoaHandler} will be empty after this method returns.
	 */
	public static void clear() {
		STATIC_BACK_OBJECTS.clear();
		BACK_OBJECTS.clear();
		GAME_OBJECTS.clear();
		FRONT_OBJECTS.clear();
		STATIC_FRONT_OBJECTS.clear();
		UI_COMPONENTS.clear();
	}

	/**
	 * Returns true if {@code DoaHandler} contains the specified {@code DoaObject}.
	 * More formally, returns true if and only if {@code DoaHandler} contains at
	 * least one {@code DoaObject} e such that (o == null ? e == null :
	 * o.equals(e)).
	 *
	 * @param o {@code DoaObject} whose presence in this collection is to be tested
	 * @return true if {@code DoaHandler} contains the specified {@code DoaObject}
	 */
	public static boolean contains(final DoaObject o) {
		return findListAccordingToZOrder(o).contains(o);
	}

	/**
	 * @return {@code DoaObject}s that are rendered to the back of the screen, but
	 *         are inanimate (do not confuse with off-screen).
	 * @see DoaObject#BACK
	 */
	public static List<DoaObject> getStaticBackObjects() {
		return STATIC_BACK_OBJECTS;
	}

	/**
	 * @return {@code DoaObject}s that are rendered to the back of the screen (do
	 *         not confuse with off-screen).
	 * @see DoaObject#BACK
	 */
	public static List<DoaObject> getBackObjects() {
		return BACK_OBJECTS;
	}

	/**
	 * @return {@code DoaObject}s that are considered as "GameObjects", i.e. objects
	 *         that can interact with other objects.
	 * @see DoaObject#GAME_OBJECTS
	 */
	public static List<DoaObject> getGameObjects() {
		return GAME_OBJECTS;
	}

	/**
	 * @return {@code DoaObject}s that are rendered to the front of the screen.
	 * @see DoaObject#FRONT
	 */
	public static List<DoaObject> getFrontObjects() {
		return FRONT_OBJECTS;
	}

	/**
	 * @return {@code DoaObject}s that are rendered to the front of the screen but
	 *         will not move relative to the {@code DoaCamera}.
	 * @see DoaObject#STATIC_FRONT
	 */
	public static List<DoaObject> getStaticFrontObjects() {
		return STATIC_FRONT_OBJECTS;
	}

	/**
	 * @return {@code DoaUIComponents}s that are rendered to the screen.
	 */
	public static List<DoaObject> getUIComponents() {
		return UI_COMPONENTS;
	}

	private static List<DoaObject> findListAccordingToZOrder(final DoaObject o) {
		switch (o.getzOrder()) {
			case -1:
				return STATIC_BACK_OBJECTS;
			case 0:
				return BACK_OBJECTS;
			case 1:
				return GAME_OBJECTS;
			case 2:
				return FRONT_OBJECTS;
			case 3:
				return STATIC_FRONT_OBJECTS;
			case 999:
				return UI_COMPONENTS;
			default:
				return new ArrayList<>();
		}
	}
}