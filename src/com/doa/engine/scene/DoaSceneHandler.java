package com.doa.engine.scene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.doa.engine.DoaEngine;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.log.DoaLogger;
import com.doa.engine.log.LogLevel;

/**
 * This class is for loading-unloading scenes to {@code DoaEngine}. If a {@code DoaScene} is loaded
 * using {@link DoaSceneHandler#loadScene(DoaScene)}, the scene will have it's
 * {@link DoaScene#tick()} and {@link DoaScene#render(DoaGraphicsContext)} method called. Only one
 * scene can be loaded at one time.
 *
 * @author Doga Oruc
 * @since DoaEngine 2.7
 * @version 2.7
 */
public final class DoaSceneHandler {

	private static final DoaLogger LOGGER = DoaLogger.getInstance();

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
	 * Creates and returns an empty {@code DoaScene}.
	 *
	 * @param sceneName name of the scene
	 * @return newly created empty {@code DoaScene}
	 */
	public static DoaScene createScene(final String sceneName) {
		final DoaScene rv = new DoaScene(sceneName);
		SCENES.put(sceneName, rv);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine(new StringBuilder(128).append(sceneName).append(" is succesfully instantiated."));
		}
		return rv;
	}

	/**
	 * Creates and returns a {@code DoaScene} that contains the objects inside the parameter list.
	 *
	 * @param sceneName name of the scene
	 * @param objects DoaObjects that will be put to the scene
	 * @return newly created {@code DoaScene}
	 */
	public static DoaScene createScene(final String sceneName, final DoaObject... objects) {
		final DoaScene rv = createScene(sceneName);
		Stream.of(objects).forEach(rv::add);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(512).append(sceneName).append(" is succesfully instantiated.\n object count: ").append(objects.length));
		} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine(new StringBuilder(128).append(sceneName).append(" is succesfully instantiated."));
		}
		return rv;
	}

	/**
	 * Creates and returns a {@code DoaScene} that contains the objects inside the list.
	 *
	 * @param sceneName name of the scene
	 * @param objects DoaObjects that will be put to the scene
	 * @return newly created {@code DoaScene}
	 */
	public static DoaScene createScene(final String sceneName, final List<DoaObject> objects) {
		final DoaScene rv = createScene(sceneName);
		objects.forEach(rv::add);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(512).append(sceneName).append(" is succesfully instantiated.\n object count: ").append(objects.size()));
		} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine(new StringBuilder(128).append(sceneName).append(" is succesfully instantiated."));
		}
		return rv;
	}

	/**
	 * Loads a scene. If there is already a scene loaded, the loaded scene will be unloaded first.
	 *
	 * @param sceneName name of the scene to load
	 */
	public static void loadScene(final String sceneName) {
		if (loadedScene != null) {
			loadedScene.isLoaded = false;
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(512).append("Unloaded ").append(loadedScene.name).append("\n object count: ").append(loadedScene.size()));
			} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINE) >= 0) {
				LOGGER.fine(new StringBuilder(128).append("Unloaded ").append(loadedScene.name));
			}
		}
		loadedScene = SCENES.get(sceneName);
		loadedScene.isLoaded = true;
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(512).append("Loaded ").append(sceneName).append("\n object count: ").append(SCENES.get(sceneName).size()));
		} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine(new StringBuilder(128).append("Loaded ").append(sceneName));
		}
	}

	/**
	 * Loads a scene. If there is already a scene loaded, the loaded scene will be unloaded first.
	 *
	 * @param scene scene to load
	 */
	public static void loadScene(final DoaScene scene) {
		if (loadedScene != null) {
			loadedScene.isLoaded = false;
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
				LOGGER.finer(new StringBuilder(512).append("Unloaded ").append(loadedScene.name).append("\n object count: ").append(loadedScene.size()));
			} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINE) >= 0) {
				LOGGER.fine(new StringBuilder(128).append("Unloaded ").append(loadedScene.name));
			}
		}
		loadedScene = scene;
		loadedScene.isLoaded = true;
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINER) >= 0) {
			LOGGER.finer(new StringBuilder(512).append("Loaded ").append(scene.name).append("\n object count: ").append(scene.size()));
		} else if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINE) >= 0) {
			LOGGER.fine(new StringBuilder(128).append("Loaded ").append(scene.name));
		}
	}

	public static DoaScene getLoadedScene() {
		return loadedScene;
	}
}