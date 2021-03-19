package doa.engine.scene;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import doa.engine.log.DoaLogger;
import doa.engine.log.LogLevel;

/**
 * Builder for instantiating sub-classes of DoaObject.
 *
 * @deprecated This class and all of its functionality has been deprecated and
 *             will be removed at some future date. Create DoaObjects by calling
 *             their constructors and add them to DoaScenes by calling
 *             {@link DoaScene#add(DoaObject)}.
 * @param <T> type of the instantiated object
 * @author Doga Oruc
 * @since DoaEngine 2.7
 * @version 3.0
 */
@Deprecated(since = "3.0", forRemoval = true)
public class DoaObjectBuilder<T extends DoaObject> {

	private static final DoaLogger LOGGER = DoaLogger.getInstance();

	/**
	 * Class of DoaObject this builder will instantiate.
	 */
	private Class<T> clazz = null;

	/**
	 * Constructor arguments that will be passed to a suitable constructor of a
	 * sub-class of DoaObject while instantiating it. If constructor arguments are
	 * not set using {@link DoaObjectBuilder#args(Object...)}, the builder will try
	 * to use the no argument constructor. If the no argument constructor is not
	 * found, the builder will throw a DoaObjectInstantiationException.
	 */
	private Object[] constructorArgs = new Object[0];

	/**
	 * The scene the instantiated object will be added to. If a scene is not set
	 * using {@link DoaObjectBuilder#scene(DoaScene)}, the builder will add the
	 * DoaObject to the currently loaded scene. However, if there are no scene
	 * loaded, the DoaObject will just be instantiated and not added to any scene.
	 */
	private DoaScene scene = null;

	/**
	 * Constructor.
	 *
	 * @param clazz type of the object to instantiate
	 */
	public DoaObjectBuilder(@NotNull final Class<T> clazz) { this.clazz = clazz; }

	/**
	 * Sets the constructor arguments.
	 *
	 * @param constructorArgs constructor arguments to pass
	 * @return this builder
	 */
	public DoaObjectBuilder<T> args(final Object... constructorArgs) {
		this.constructorArgs = constructorArgs;
		return this;
	}

	/**
	 * Sets the scene.
	 *
	 * @param scene the scene the instantiated object will be put
	 * @return this builder
	 */
	public DoaObjectBuilder<T> scene(final DoaScene scene) {
		this.scene = scene;
		return this;
	}

	/**
	 * Instantiates a new DoaObject by finding a suitable constructor using the
	 * constructor arguments set by {@link DoaObjectBuilder#args(Object...)} method.
	 * If no constructor arguments are set, the builder will try to invoke the no
	 * argument constructor. If a no argument constructor is present, the object
	 * will be successfully instantiated and returned, otherwise a
	 * DoaObjectInstantiationException will be thrown.
	 * <p>
	 * The instantiated DoaObject will also be added to the scene set by
	 * {@link DoaObjectBuilder#scene(DoaScene)} method. If no scene is set, the
	 * builder will add the instantiated DoaObject to the currently loaded scene. If
	 * no scene is set AND no scene is loaded at the time of this method's
	 * invocation, the instantiated DoaObject will not be added to any scene.
	 *
	 * @throws DoaObjectInstantiationException if supplied parameters cannot be used
	 *         with a constructor
	 * @return the instantiated DoaObject or null if instantiation is unsuccessful
	 *         due to an internal error
	 */
	public T instantiate() {
		final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		final String parameters = Stream.of(constructorArgs).map(Object::toString).collect(Collectors.joining(",", "[", "]"));
		for (final Constructor<?> c : constructors) {
			c.setAccessible(true);
			if (c.getParameterCount() == constructorArgs.length) {
				final Class<?>[] parameterTypes = c.getParameterTypes();
				boolean isSuitable = true;
				for (int i = 0; i < parameterTypes.length; i++) {
					final Class<?> requiredParameterType = parameterTypes[i];
					final Class<?> receivedParameterType = constructorArgs[i].getClass();
					if (!requiredParameterType.isAssignableFrom(receivedParameterType)) {
						if (requiredParameterType.isPrimitive()) {
							isSuitable = byte.class.equals(requiredParameterType) && Byte.class.equals(receivedParameterType)
							        || short.class.equals(requiredParameterType) && Short.class.equals(receivedParameterType)
							        || int.class.equals(requiredParameterType) && Integer.class.equals(receivedParameterType)
							        || long.class.equals(requiredParameterType) && Long.class.equals(receivedParameterType)
							        || float.class.equals(requiredParameterType) && Float.class.equals(receivedParameterType)
							        || double.class.equals(requiredParameterType) && Double.class.equals(receivedParameterType)
							        || char.class.equals(requiredParameterType) && Character.class.equals(receivedParameterType)
							        || boolean.class.equals(requiredParameterType) && Boolean.class.equals(receivedParameterType);
						} else {
							isSuitable = false;
							break;
						}
					}
				}
				if (isSuitable) {
					try {
						final Object o = c.newInstance(constructorArgs);
						if (clazz.isInstance(o)) {
							final T d = clazz.cast(o);
							if (scene != null) {
								scene.add(d);
							} else if (DoaSceneHandler.getLoadedScene() != null) {
								DoaSceneHandler.getLoadedScene().add(d);
							}
							if (LOGGER.getLevel().compareTo(LogLevel.FINER) >= 0) {
								LOGGER.finer(new StringBuilder(512).append(clazz.getName()).append(" is succesfully instantiated.\nWith parameters: ").append(
								        parameters));
							} else if (LOGGER.getLevel().compareTo(LogLevel.FINE) >= 0) {
								LOGGER.fine(new StringBuilder(128).append(clazz.getName()).append(" is succesfully instantiated."));
							}
							return d;
						}
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
						ex.printStackTrace();
					}
					return null;
				}
			}
		}
		final String errorMessage = new StringBuilder(512).append("Cannot find a suitable constructor for ").append(clazz.getName()).append(
		        ".\nWith parameters: ").append(parameters).toString();
		LOGGER.severe(errorMessage);
		throw new DoaObjectInstantiationException(errorMessage);
	}

	/**
	 * Instantiates a new DoaObject then resets the builders constructorArgs and
	 * scene fields. Calls {@link DoaObjectBuilder#instantiate()} and returns the
	 * return value. Any and all exceptions that may be thrown by
	 * {@link DoaObjectBuilder#instantiate()} can also be thrown by this method.
	 *
	 * @see DoaObjectBuilder#instantiate()
	 * @return the instantiated DoaObject
	 */
	public T instantiateAndReset() {
		T t = instantiate();
		constructorArgs = new Object[0];
		scene = null;
		return t;
	}
}
