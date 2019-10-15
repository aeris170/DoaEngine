package com.doa.engine.scene;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.doa.engine.DoaCamera;
import com.doa.engine.DoaEngine;
import com.doa.engine.Internal;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaMouse;
import com.doa.engine.log.DoaLogger;
import com.doa.engine.log.LogLevel;
import com.doa.ui.DoaUIComponent;
import com.doa.ui.DoaUIContainer;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

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

	private final SetMultimap<Integer, DoaObject> OBJECTS = Multimaps.synchronizedSetMultimap(MultimapBuilder.treeKeys().hashSetValues().build());
	private final SetMultimap<Integer, DoaUIComponent> UI_COMPONENTS = Multimaps.synchronizedSetMultimap(MultimapBuilder.treeKeys().hashSetValues().build());

	String name;
	boolean isLoaded = false;

	private transient List<Entry<Integer, DoaObject>> cachedObjects = new ArrayList<>();
	private transient List<Entry<Integer, DoaUIComponent>> cachedComponents = new ArrayList<>();

	DoaScene(final String sceneName) {
		name = sceneName;
	}

	@Internal
	public void tick() {
		cachedObjects = new ArrayList<>(OBJECTS.entries());
		cachedComponents = new ArrayList<>(UI_COMPONENTS.entries());

		for (int i = cachedComponents.size() - 1; i >= 0; i--) {
			DoaUIComponent component = cachedComponents.get(i).getValue();
			if (component.isVisible()) {
				component.recalibrateBounds();
				component.tick();
				if (!(component instanceof DoaUIContainer) && component.isEnabled() && component.getBounds().contains(DoaMouse.X, DoaMouse.Y)) {
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
		}
		for (int i = cachedObjects.size() - 1; i >= 0; i--) {
			cachedObjects.get(i).getValue().tick();
		}
	}

	@Internal
	public void render(final DoaGraphicsContext g) {
		for (int i = 0; i < cachedObjects.size(); i++) {
			DoaObject object = cachedObjects.get(i).getValue();
			if (object.isFixed()) {
				g.turnOffLightContribution();
				object.render(g);
				g.turnOnLightContribution();
			} else {
				g.pushTransform();
				g.translate(-DoaCamera.getX(), -DoaCamera.getY());
				object.render(g);
				g.popTransform();
			}
		}
		g.turnOffLightContribution();
		for (int i = 0; i < cachedComponents.size(); i++) {
			DoaUIComponent component = cachedComponents.get(i).getValue();
			if (component.isVisible() && component.getParent() == null) {
				if (component instanceof DoaUIContainer) {
					renderContainerAndAllChildren((DoaUIContainer) component, g);
				} else {
					component.render(g);
				}
			}
		}
		g.turnOnLightContribution();
	}

	/**
	 * Adds the specified {@code DoaObject} to this scene.
	 *
	 * @param o object to add to this scene
	 */
	public void add(@NotNull final DoaObject o) {
		final DoaScene possibleOldScene = o.getScene();
		if (possibleOldScene != null) {
			possibleOldScene.remove(o);
		}
		if (o instanceof DoaUIComponent) {
			UI_COMPONENTS.put(o.getzOrder(), (DoaUIComponent) o);
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINEST) >= 0) {
				LOGGER.finest(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully added to ").append(name).append(". at zOrder: ").append(o.getzOrder())
				        .append("."));
			} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully added to ").append(name).append("."));
			}
			if (o instanceof DoaUIContainer) {
				((DoaUIContainer) o).getComponents().forEach(c -> {
					UI_COMPONENTS.put(c.getzOrder(), c);
					if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINEST) >= 0) {
						LOGGER.finest(new StringBuilder(128).append("\t").append(c.getClass().getName()).append(" is succesfully added to ").append(name).append(". Parent: ")
						        .append(o.getClass().getName()));
					} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
						LOGGER.finer(new StringBuilder(128).append("\t").append(c.getClass().getName()).append(" is succesfully added to ").append(name).append("."));
					}
				});
			}
		} else {
			OBJECTS.put(o.getzOrder(), o);
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
	public void remove(@NotNull final DoaObject o) {
		if (o instanceof DoaUIComponent) {
			UI_COMPONENTS.entries().removeIf(x -> x.getValue().equals(o));
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINEST) >= 0) {
				LOGGER.finest(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully removed from ").append(name).append(". It was at zOrder: ")
				        .append(o.getzOrder()).append("."));
			} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully removed from ").append(name).append("."));
			}
			if (o instanceof DoaUIContainer) {
				((DoaUIContainer) o).getComponents().forEach(c -> {
					UI_COMPONENTS.entries().removeIf(x -> x.getValue().equals(o));
					if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINEST) >= 0) {
						LOGGER.finest(new StringBuilder(128).append("\t").append(c.getClass().getName()).append(" is succesfully removed from ").append(name).append(". Parent: ")
						        .append(o.getClass().getName()));
					} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
						LOGGER.finer(new StringBuilder(128).append("\t").append(c.getClass().getName()).append(" is succesfully removed from ").append(name).append("."));
					}
				});
			}
		} else {
			OBJECTS.entries().removeIf(x -> x.getValue().equals(o));
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINEST) >= 0) {
				LOGGER.finest(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully removed from ").append(name).append(". It was at zOrder: ")
				        .append(o.getzOrder()).append("."));
			} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully removed from ").append(name).append("."));
			}
		}
		o.setScene(null);
	}

	/**
	 * Checks if the specified {@code DoaObject} from this scene. More formally, returns true if and
	 * only if this scene contains an element e such that Objects.equals(o, e).
	 *
	 * @param o object to remove from this scene
	 * @return true if this scene contains an element e such that Objects.equals(o, e)
	 */
	public boolean contains(final DoaObject o) {
		return OBJECTS.containsValue(o) || UI_COMPONENTS.containsValue(o);
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

	public List<DoaObject> objectsInScene() {
		return OBJECTS.entries().stream().map(Entry::getValue).collect(Collectors.toList());
	}

	public List<DoaUIComponent> uiComponentsInScene() {
		return UI_COMPONENTS.entries().stream().map(Entry::getValue).collect(Collectors.toList());
	}

	void updatezOrder(final DoaObject o, final int newzOrder) {
		if (o instanceof DoaUIComponent) {
			UI_COMPONENTS.remove(o.getzOrder(), o);
			UI_COMPONENTS.put(newzOrder, (DoaUIComponent) o);
		} else {
			OBJECTS.remove(o.getzOrder(), o);
			OBJECTS.put(newzOrder, o);
		}
	}
}
