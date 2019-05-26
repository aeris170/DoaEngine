package com.doa.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaMouse;
import com.doa.ui.DoaUIComponent;
import com.doa.ui.DoaUIContainer;

/**
 * Responsible for keeping track of all {@code DoaObject}s. All
 * {@code DoaObject}s and its respective child-classes are automatically added
 * to {@code DoaHandler} after they are created with invoking
 * {@link DoaHandler#instantiate(Class, Object...)}, and have their
 * {@link DoaObject#tick()} and {@link DoaObject#render(DoaGraphicsContext)}
 * methods called by {@code DoaEngine}. This class is static, therefore has no
 * objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.5
 */
public final class DoaHandler {

	private static final ConcurrentNavigableMap<Integer, Set<DoaObject>> OBJECTS = new ConcurrentSkipListMap<>();
	private static final ConcurrentNavigableMap<Integer, Set<DoaUIComponent>> UI_COMPONENTS = new ConcurrentSkipListMap<>();

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
	public static <T extends DoaObject> T instantiate(final Class<T> clazz, final Object... constructorArgs) {
		final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		for (final Constructor<?> c : constructors) {
			c.setAccessible(true);
			if (c.getParameterCount() == constructorArgs.length) {
				final Class<?>[] parameterTypes = c.getParameterTypes();
				boolean isSuitable = true;
				for (int i = 0; i < parameterTypes.length; i++) {
					Class<?> requiredParameterType = parameterTypes[i];
					Class<?> receivedParameterType = constructorArgs[i].getClass();
					if (!requiredParameterType.isAssignableFrom(receivedParameterType)) {
						if (requiredParameterType.isPrimitive()) {
							isSuitable = (byte.class.equals(requiredParameterType) && Byte.class.equals(receivedParameterType))
							        || (short.class.equals(requiredParameterType) && Short.class.equals(receivedParameterType))
							        || (int.class.equals(requiredParameterType) && Integer.class.equals(receivedParameterType))
							        || (long.class.equals(requiredParameterType) && Long.class.equals(receivedParameterType))
							        || (float.class.equals(requiredParameterType) && Float.class.equals(receivedParameterType))
							        || (double.class.equals(requiredParameterType) && Double.class.equals(receivedParameterType))
							        || (char.class.equals(requiredParameterType) && Character.class.equals(receivedParameterType))
							        || (boolean.class.equals(requiredParameterType) && Boolean.class.equals(receivedParameterType));
						} else {
							isSuitable = false;
							break;
						}
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
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
						ex.printStackTrace();
					}
					return null;
				}
			}
		}
		StringBuilder sb = new StringBuilder("[");
		String prefix = "";
		for (Object o : constructorArgs) {
			sb.append(prefix);
			prefix = ", ";
			sb.append(o.getClass().getSimpleName());
		}
		sb.append("]");
		System.err.println("Cannot find a suitable constructor for " + clazz.getName());
		System.err.println("With parameters: " + sb.toString());
		return null;
	}

	static void tick() {
		for (Map.Entry<Integer, Set<DoaUIComponent>> e : UI_COMPONENTS.descendingMap().entrySet()) {
			e.getValue().forEach(component -> {
				if (component.isVisible()) {
					component.recalibrateBounds();
					component.tick();
					if (component.isEnabled() && component.getBounds().contains(DoaMouse.X, DoaMouse.Y)) {
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
			});
		}
		for (Map.Entry<Integer, Set<DoaObject>> e : OBJECTS.descendingMap().entrySet()) {
			e.getValue().forEach(object -> object.tick());
		}
	}

	static void render(final DoaGraphicsContext g) {
		OBJECTS.forEach((zOrder, objects) -> objects.forEach(object -> {
			if (object.isFixed()) {
				object.render(g);
			} else {
				g.pushTransform();
				g.translate(-DoaCamera.getX(), -DoaCamera.getY());
				object.render(g);
				g.popTransform();
			}
		}));
		UI_COMPONENTS.forEach((zOrder, uiComponents) -> uiComponents.forEach(component -> {
			if (component.isVisible() && component.getParent() == null) {
				if (component instanceof DoaUIContainer) {
					renderContainerAndAllChildren(((DoaUIContainer) component), g);
				} else {
					component.render(g);
				}
			}
		}));
	}

	/**
	 * Adds the specified {@code DoaObject} to {@code DoaHandler}.
	 * 
	 * @param o object to add to {@code DoaHandler}
	 */
	public static void add(DoaObject o) {
		if (o instanceof DoaUIComponent) {
			UI_COMPONENTS.computeIfAbsent(o.getzOrder(), k -> new CopyOnWriteArraySet<>());
			UI_COMPONENTS.get(o.getzOrder()).add((DoaUIComponent) o);
			if (o instanceof DoaUIContainer) {
				((DoaUIContainer) o).getComponents().forEach(c -> {
					UI_COMPONENTS.computeIfAbsent(c.getzOrder(), k -> new CopyOnWriteArraySet<>());
					UI_COMPONENTS.get(c.getzOrder()).add(c);
				});
			}
		} else {
			OBJECTS.computeIfAbsent(o.getzOrder(), k -> new CopyOnWriteArraySet<>());
			OBJECTS.get(o.getzOrder()).add(o);
		}
	}

	/**
	 * Removes the specified {@code DoaObject} from {@code DoaHandler}.
	 * 
	 * @param o object to remove from {@code DoaHandler}
	 */
	public static void remove(DoaObject o) {
		if (o instanceof DoaUIComponent) {
			UI_COMPONENTS.get(o.getzOrder()).remove(o);
			if (o instanceof DoaUIContainer) {
				((DoaUIContainer) o).getComponents().forEach(c -> UI_COMPONENTS.get(c.getzOrder()).remove(c));
			}
		} else {
			OBJECTS.get(o.getzOrder()).remove(o);
		}
	}

	/**
	 * Removes ALL of the {@code DoaObject}s from {@code DoaHandler}.
	 * {@code DoaHandler} will be empty after this method returns.
	 */
	public static void clear() {
		OBJECTS.clear();
		UI_COMPONENTS.clear();
	}

	private static void renderContainerAndAllChildren(DoaUIContainer container, DoaGraphicsContext g) {
		container.render(g);
		container.getComponents().forEach(component -> {
			if (component instanceof DoaUIContainer) {
				renderContainerAndAllChildren((DoaUIContainer) component, g);
			} else {
				component.render(g);
			}
		});
	}
}