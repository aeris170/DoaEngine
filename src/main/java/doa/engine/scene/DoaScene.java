package doa.engine.scene;

import static doa.engine.core.DoaGraphicsFunctions.popTransform;
import static doa.engine.core.DoaGraphicsFunctions.pushTransform;
import static doa.engine.core.DoaGraphicsFunctions.resetAll;
import static doa.engine.core.DoaGraphicsFunctions.translate;
import static doa.engine.core.DoaGraphicsFunctions.turnOffLightContribution;
import static doa.engine.core.DoaGraphicsFunctions.turnOnLightContribution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.validation.constraints.NotNull;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import doa.engine.Internal;
import doa.engine.core.DoaCamera;
import doa.engine.log.DoaLogger;
import doa.engine.log.LogLevel;
import doa.engine.scene.elements.physics.DoaRigidBody;

/**
 * Responsible for keeping DoaObjects. A scene is composed of zero or more
 * DoaObjects. DoaObjects can be added to a scene only by invoking
 * {@link DoaScene#add(DoaObject)} and passing an existing DoaObject. <br>
 * <br>
 * For a DoaObject to have a contribution to the image on the screen, the
 * DoaObject has to be in the loaded scene. DoaScenes are loaded via
 * {@link DoaSceneHandler#loadScene(DoaScene)}.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 3.0
 */
public class DoaScene implements Serializable {

	private static final long serialVersionUID = 378268835684118855L;

	private static final DoaLogger LOGGER = DoaLogger.getInstance();

	// TODO make serializable!!!!
	private final Multimap<Integer, DoaObject> OBJECTS = MultimapBuilder.treeKeys().arrayListValues().build();
	final transient DoaPhysics physics = new DoaPhysics();

	String name;
	boolean isLoaded = false;

	private transient List<Entry<Integer, DoaObject>> cachedObjects = new ArrayList<>();
	private transient List<DoaObject> toRemove = new CopyOnWriteArrayList<>();

	DoaScene(@NotNull final String sceneName) { name = sceneName; }

	/**
	 * @hidden
	 */
	@SuppressWarnings("javadoc")
	@Internal
	public void tick(int ticks) {
		removeScheduled();
		cachedObjects = new ArrayList<>(OBJECTS.entries());
		for (int i = cachedObjects.size() - 1; i >= 0; i--) {
			cachedObjects.get(i).getValue().tick();
		}
		physics.tick(ticks);
	}

	/**
	 * @hidden
	 */
	@Internal
	public void render() {
		resetAll();
		for (int i = 0; i < cachedObjects.size(); i++) {
			DoaObject object = cachedObjects.get(i).getValue();
			if (object.isDynamic()) {
				turnOnLightContribution();
				pushTransform();
				translate(-DoaCamera.getX(), -DoaCamera.getY());
				object.render();
				popTransform();
			} else {
				turnOffLightContribution();
				object.render();
			}
		}
	}

	/**
	 * Adds the specified DoaObject to this scene.
	 *
	 * @param o object to add to this scene
	 */
	public void add(@NotNull final DoaObject o) {
		final DoaScene possibleOldScene = o.getScene();
		if (possibleOldScene != null) {
			possibleOldScene.remove(o);
		}
		OBJECTS.put(o.getzOrder(), o);
		if (LOGGER.getLevel().compareTo(LogLevel.FINEST) >= 0) {
			LOGGER.finest(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully added to ").append(name).append(". at zOrder: ").append(
			        o.getzOrder()).append("."));
		} else if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully added to ").append(name).append("."));
		}
		o.scene = this;
		o.onAddToScene(this);
	}

	/**
	 * Schedules the removal of the specified DoaObject from this scene. This method
	 * schedules the object for removal, the removal will take place in the start of
	 * next tick.
	 *
	 * @param o object to remove from this scene
	 */
	public void remove(@NotNull final DoaObject o) { toRemove.add(o); }

	private void removeScheduled() {
		for (DoaObject o : toRemove) {
			if (OBJECTS.entries().removeIf(x -> x.getValue().equals(o))) {
				if (LOGGER.getLevel().compareTo(LogLevel.FINEST) >= 0) {
					LOGGER.finest(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully removed from ").append(name).append(
					        ". It was at zOrder: ").append(o.getzOrder()).append("."));
				} else if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
					LOGGER.finer(new StringBuilder(128).append(o.getClass().getName()).append(" is succesfully removed from ").append(name).append("."));
				}
				o.scene = null;
				o.onRemoveFromScene(this);
			}
		}
		toRemove.clear();
	}

	/**
	 * Checks if the specified DoaObject (or UI component) is inside this scene.
	 * More formally, returns true if and only if this scene contains an element e
	 * such that Objects.equals(o, e).
	 *
	 * @param o object to remove from this scene
	 * @return true if this scene contains an element e such that Objects.equals(o,
	 *         e)
	 */
	public boolean contains(final DoaObject o) { return OBJECTS.containsValue(o); }

	/**
	 * Removes ALL of the DoaObjects from this scene. The scene will be empty after
	 * this method returns.
	 */
	public void clear() {
		List<Entry<Integer, DoaObject>> objects = new ArrayList<>(OBJECTS.entries());
		OBJECTS.clear();
		for (Entry<Integer, DoaObject> entry : objects) {
			entry.getValue().scene = null;
			entry.getValue().onRemoveFromScene(this);
		}
		LOGGER.info(new StringBuilder(128).append(name).append(" is now empty."));
	}

	@Override
	public String toString() {
		return new StringBuilder(128).append("DoaScene [\n\t{\n\t\tname:").append(name).append(",\n\t\tisLoaded:").append(isLoaded).append(
		        ",\n\t\tobject count:").append(size()).append("\n\t}\n]").toString();
	}

	public String getName() { return name; }

	public boolean isLoaded() { return isLoaded; }

	public int size() { return OBJECTS.size(); }

	/**
	 * @hidden
	 */
	@SuppressWarnings("javadoc")
	@Internal
	public void updatezOrder(final DoaObject o, final int newzOrder) {
		OBJECTS.remove(o.getzOrder(), o);
		OBJECTS.put(newzOrder, o);
	}

	/**
	 * @hidden
	 */
	@SuppressWarnings("javadoc")
	@Internal
	public void registerBody(@NotNull final DoaRigidBody rigidBody) { physics.registerBody(rigidBody); }

	/**
	 * @hidden
	 */
	@SuppressWarnings("javadoc")
	@Internal
	public void deleteBody(@NotNull final DoaRigidBody rigidBody) { physics.deleteBody(rigidBody); }
}
