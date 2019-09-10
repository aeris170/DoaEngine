package com.doa.engine.scene;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.doa.engine.DoaCamera;
import com.doa.engine.DoaEngine;
import com.doa.engine.Internal;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaMouse;
import com.doa.engine.log.DoaLogger;
import com.doa.engine.log.LogLevel;
import com.doa.ui.DoaUIComponent;
import com.doa.ui.DoaUIContainer;

/**
 * Responsible for keeping {@code DoaObject}s. A scene is composed of zero or more
 * {@code DoaObject}s. {@code DoaObject}s can be added to a scene either by
 * <ul>
 * <li>invoking {@link DoaScene#add(DoaObject)} and passing an existing {@code DoaObject}</li>
 * <li><b>OR</b></li>
 * <li>creating a {@code DoaObject} with {@link DoaObjectBuilder}</li>
 * </ul>
 * <br>
 * All {@code DoaObject}s inside a scene will have their {@link DoaObject#tick()} and
 * {@link DoaObject#render(DoaGraphicsContext)} methods called by {@code DoaEngine}. If the scene
 * the {@code DoaObject} is in is loaded using {@link DoaSceneHandler#loadScene(DoaScene)}. This
 * class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.7
 */
public class DoaScene implements Serializable {

	private static final long serialVersionUID = 378268835684118855L;

	private static final DoaLogger LOGGER = DoaLogger.getInstance();

	private final ConcurrentNavigableMap<Integer, Set<DoaObject>> OBJECTS = new ConcurrentSkipListMap<>();
	private final ConcurrentNavigableMap<Integer, Set<DoaUIComponent>> UI_COMPONENTS = new ConcurrentSkipListMap<>();

	String name;
	boolean isLoaded = false;

	DoaScene(final String sceneName) {
		name = sceneName;
	}

	@Internal
	public void tick() {
		for (final Map.Entry<Integer, Set<DoaUIComponent>> e : UI_COMPONENTS.descendingMap().entrySet()) {
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
		for (final Map.Entry<Integer, Set<DoaObject>> e : OBJECTS.descendingMap().entrySet()) {
			e.getValue().forEach(DoaObject::tick);
		}
	}

	@Internal
	public void render(final DoaGraphicsContext g) {
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
					renderContainerAndAllChildren((DoaUIContainer) component, g);
				} else {
					component.render(g);
				}
			}
		}));
	}

	/**
	 * Adds the specified {@code DoaObject} to this scene.
	 *
	 * @param o object to add to this scene
	 */
	public void add(final DoaObject o) {
		if (o instanceof DoaUIComponent) {
			UI_COMPONENTS.computeIfAbsent(o.getzOrder(), k -> new CopyOnWriteArraySet<>());
			UI_COMPONENTS.get(o.getzOrder()).add((DoaUIComponent) o);
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINEST) >= 0) {
				LOGGER.finest(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully added to ").append(name).append(". at zOrder: ").append(o.getzOrder())
				        .append("."));
			} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully added to ").append(name).append("."));
			}
			if (o instanceof DoaUIContainer) {
				((DoaUIContainer) o).getComponents().forEach(c -> {
					UI_COMPONENTS.computeIfAbsent(c.getzOrder(), k -> new CopyOnWriteArraySet<>());
					UI_COMPONENTS.get(c.getzOrder()).add(c);
					if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINEST) >= 0) {
						LOGGER.finest(new StringBuilder(128).append("\t").append(c.getClass().getName()).append(" is succesfully added to ").append(name).append(". Parent: ")
						        .append(o.getClass().getName()));
					} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
						LOGGER.finer(new StringBuilder(128).append("\t").append(c.getClass().getName()).append(" is succesfully added to ").append(name).append("."));
					}
				});
			}
		} else {
			OBJECTS.computeIfAbsent(o.getzOrder(), k -> new CopyOnWriteArraySet<>());
			OBJECTS.get(o.getzOrder()).add(o);
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINEST) >= 0) {
				LOGGER.finest(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully added to ").append(name).append(". at zOrder: ").append(o.getzOrder())
				        .append("."));
			} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully added to ").append(name).append("."));
			}
		}
		o.setScene(this);
	}

	/**
	 * Removes the specified {@code DoaObject} from this scene.
	 *
	 * @param o object to remove from this scene
	 */
	public void remove(final DoaObject o) {
		if (o instanceof DoaUIComponent) {
			UI_COMPONENTS.get(o.getzOrder()).remove(o);
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINEST) >= 0) {
				LOGGER.finest(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully removed from ").append(name).append(". It was at zOrder: ")
				        .append(o.getzOrder()).append("."));
			} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully removed from ").append(name).append("."));
			}
			if (o instanceof DoaUIContainer) {
				((DoaUIContainer) o).getComponents().forEach(c -> {
					UI_COMPONENTS.get(c.getzOrder()).remove(c);
					if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINEST) >= 0) {
						LOGGER.finest(new StringBuilder(128).append("\t").append(c.getClass().getName()).append(" is succesfully removed from ").append(name).append(". Parent: ")
						        .append(o.getClass().getName()));
					} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
						LOGGER.finer(new StringBuilder(128).append("\t").append(c.getClass().getName()).append(" is succesfully removed from ").append(name).append("."));
					}
				});
			}
		} else {
			OBJECTS.get(o.getzOrder()).remove(o);
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINEST) >= 0) {
				LOGGER.finest(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully removed from ").append(name).append(". It was at zOrder: ")
				        .append(o.getzOrder()).append("."));
			} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully removed from ").append(name).append("."));
			}
		}
		o.setScene(this);
	}

	/**
	 * Removes the specified {@code DoaObject} from this scene.
	 *
	 * @param o object to remove from this scene
	 * @return a
	 */
	public boolean contains(final DoaObject o) {
		for (final Map.Entry<Integer, Set<DoaObject>> entry : OBJECTS.entrySet()) {
			if (entry.getValue().contains(o)) {
				return true;
			}
		}
		for (final Map.Entry<Integer, Set<DoaUIComponent>> entry : UI_COMPONENTS.entrySet()) {
			if (entry.getValue().contains(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes ALL of the {@code DoaObject}s from this scene. The scene will be empty after this method
	 * returns.
	 */
	public void clear() {
		OBJECTS.clear();
		UI_COMPONENTS.clear();
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.INFO) >= 0) {
			LOGGER.info(new StringBuilder(128).append(name).append(" is now empty."));
		}
	}

	private static void renderContainerAndAllChildren(final DoaUIContainer container, final DoaGraphicsContext g) {
		container.render(g);
		container.getComponents().forEach(component -> {
			if (component instanceof DoaUIContainer) {
				renderContainerAndAllChildren((DoaUIContainer) component, g);
			} else {
				component.render(g);
			}
		});
	}

	@Override
	public String toString() {
		return new StringBuilder(128).append("DoaScene [\n\t{\n\t\tname:").append(name).append(",\n\t\tisLoaded:").append(isLoaded).append(",\n\t\tobject count:").append(size())
		        .append("\n\t}\n]").toString();
	}

	public String getName() {
		return name;
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public int size() {
		return OBJECTS.size() + UI_COMPONENTS.size();
	}
}
