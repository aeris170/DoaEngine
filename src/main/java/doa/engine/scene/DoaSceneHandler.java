package doa.engine.scene;

import static doa.engine.log.DoaLogger.LOGGER;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import doa.engine.log.LogLevel;

/**
 * Class is for loading-unloading scenes to DoaEngine. If a DoaScene is loaded
 * using {@link DoaSceneHandler#loadScene(DoaScene)}, the scene will have it's
 * {@link DoaScene#tick()} and {@link DoaScene#render()} methods called. Only
 * one scene can be loaded at a time.
 *
 * @author Doga Oruc
 * @since DoaEngine 2.7
 * @version 3.0
 */
public final class DoaSceneHandler {

	private static DoaScene loadedScene = null;

	/**
	 * Collection that maps scene names to scenes.
	 */
	private static final Map<String, DoaScene> SCENES = new HashMap<>();

	/**
	 * Constructor.
	 */
	private DoaSceneHandler() {}

	/**
	 * Creates and returns an empty DoaScene.
	 *
	 * @param sceneName unique name of the scene
	 * @return newly created empty DoaScene
	 */
	public static DoaScene createScene(@NotNull final String sceneName) {
		final DoaScene rv = new DoaScene(sceneName);
		SCENES.put(sceneName, rv);
		LOGGER.fine(new StringBuilder(128).append(sceneName).append(" is succesfully instantiated."));
		return rv;
	}

	/**
	 * Creates and returns a DoaScene that contains the objects inside the parameter
	 * list.
	 *
	 * @param sceneName unique name of the scene
	 * @param objects DoaObjects that will be put to the scene
	 * @return newly created DoaScene
	 */
	@SafeVarargs
	public static DoaScene createScene(@NotNull final String sceneName, @NotEmpty final DoaObject... objects) {
		final DoaScene rv = createScene(sceneName);
		Stream.of(objects).forEach(rv::add);
		if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(512).append(sceneName).append(" is succesfully instantiated.\n object count: ").append(objects.length));
		} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine(new StringBuilder(128).append(sceneName).append(" is succesfully instantiated."));
		}
		return rv;
	}

	/**
	 * Creates and returns a DoaScene that contains the objects inside the list.
	 *
	 * @param sceneName unique name of the scene
	 * @param objects DoaObjects that will be put to the scene
	 * @return newly created DoaScene
	 */
	public static DoaScene createScene(@NotNull final String sceneName, @NotEmpty final List<DoaObject> objects) {
		final DoaScene rv = createScene(sceneName);
		objects.forEach(rv::add);
		if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(512).append(sceneName).append(" is succesfully instantiated.\n object count: ").append(objects.size()));
		} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine(new StringBuilder(128).append(sceneName).append(" is succesfully instantiated."));
		}
		return rv;
	}

	/**
	 * Loads a scene. If there is already a scene loaded, the loaded scene will be
	 * unloaded first.
	 *
	 * @param sceneName unique name of the scene to load
	 */
	public static void loadScene(@NotNull final String sceneName) {
		if (loadedScene != null) {
			loadedScene.isLoaded = false;
			if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(512).append("Unloaded ").append(loadedScene.name).append("\n object count: ").append(loadedScene.size()));
			} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
				LOGGER.fine(new StringBuilder(128).append("Unloaded ").append(loadedScene.name));
			}
		}
		loadedScene = SCENES.get(sceneName);
		loadedScene.isLoaded = true;
		if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(512).append("Loaded ").append(sceneName).append("\n object count: ").append(SCENES.get(sceneName).size()));
		} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine(new StringBuilder(128).append("Loaded ").append(sceneName));
		}
	}

	/**
	 * Loads a scene. If there is already a scene loaded, the loaded scene will be
	 * unloaded first.
	 *
	 * @param scene scene to load
	 */
	public static void loadScene(@NotNull final DoaScene scene) {
		if (loadedScene != null) {
			loadedScene.isLoaded = false;
			if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(512).append("Unloaded ").append(loadedScene.name).append("\n object count: ").append(loadedScene.size()));
			} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
				LOGGER.fine(new StringBuilder(128).append("Unloaded ").append(loadedScene.name));
			}
		}
		loadedScene = scene;
		loadedScene.isLoaded = true;
		if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(512).append("Loaded ").append(scene.name).append("\n object count: ").append(scene.size()));
		} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine(new StringBuilder(128).append("Loaded ").append(scene.name));
		}
	}

	public static DoaScene getLoadedScene() { return loadedScene; }
}
