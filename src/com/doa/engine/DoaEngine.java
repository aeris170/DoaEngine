package com.doa.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.io.NotSerializableException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaKeyboard;
import com.doa.engine.input.DoaMouse;
import com.doa.engine.log.DoaLogger;
import com.doa.engine.log.LogLevel;
import com.doa.engine.scene.DoaSceneHandler;

/**
 * Responsible for creating and supplying the graphics context used for drawing all created
 * {@code DoaObject}s, and updating all created {@code DoaObject}s.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version {@value DoaEngine#VERSION}
 */
public final class DoaEngine extends Canvas implements Runnable {

	private static final long serialVersionUID = -2105482237764442641L;

	/**
	 * Current version of DoaEngine.
	 */
	@Internal
	public static final String VERSION = "2.7";

	/**
	 * Number of updates per second. If {@code DoaEngine} is already running, {@link DoaEngine#stop()}
	 * must be called before setting this value. Otherwise DoaEngine will ignore the change.
	 */
	public static int TICK_RATE = 240;

	/**
	 * Enable/Disable debug logging to console/file.
	 */
	@NotNull
	public static LogLevel INTERNAL_LOG_LEVEL = LogLevel.OFF;

	/**
	 * Default background color of render canvas.
	 */
	public static Color CLEAR_COLOR = Color.BLACK;

	/**
	 * Rendering mode of {@code DoaEngine}. Default is {@link DoaRenderingMode#BALANCED}.
	 *
	 * @see DoaRenderingMode
	 */
	@NotNull
	public static DoaRenderingMode RENDERING_MODE = DoaRenderingMode.BALANCED;

	/**
	 * User defined rendering hints. Initially, this map is empty. If none of the default rendering
	 * protocols suffice, programmers can set the {@link DoaEngine#RENDERING_MODE} to
	 * {@link DoaRenderingMode#USER_DEFINED} and add their own {@code RenderingHints} to this map. This
	 * way, the rendering will take place with the user defined Hints.
	 */
	public static final Map<RenderingHints.Key, Object> USER_HINTS = new HashMap<>();

	private static final Map<RenderingHints.Key, Object> QUALITY_HINTS = new HashMap<>();
	private static final Map<RenderingHints.Key, Object> BALANCED_HINTS = new HashMap<>();
	private static final Map<RenderingHints.Key, Object> SPEED_HINTS = new HashMap<>();
	static {
		USER_HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
		USER_HINTS.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
		USER_HINTS.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
		USER_HINTS.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DEFAULT);
		USER_HINTS.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
		USER_HINTS.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		USER_HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
		USER_HINTS.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		USER_HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);

		QUALITY_HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		QUALITY_HINTS.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		QUALITY_HINTS.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		QUALITY_HINTS.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		QUALITY_HINTS.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		QUALITY_HINTS.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		QUALITY_HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		QUALITY_HINTS.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		QUALITY_HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		BALANCED_HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		BALANCED_HINTS.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		BALANCED_HINTS.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		BALANCED_HINTS.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		BALANCED_HINTS.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		BALANCED_HINTS.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		BALANCED_HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		BALANCED_HINTS.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		BALANCED_HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		SPEED_HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		SPEED_HINTS.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		SPEED_HINTS.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		SPEED_HINTS.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		SPEED_HINTS.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		SPEED_HINTS.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		SPEED_HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		SPEED_HINTS.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		SPEED_HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
	}

	private static final DoaLogger LOGGER = DoaLogger.getInstance();

	private boolean isRunning = false;
	private transient Thread gameThread;
	private static DoaEngine ENGINE = null;

	/**
	 * Constructor. Creates a new instance of {@code DoaEngine}.
	 */
	public DoaEngine() {
		if (DoaEngine.ENGINE == null) {
			System.setProperty("sun.java2d.opengl", "True");
			if (INTERNAL_LOG_LEVEL.compareTo(LogLevel.INFO) >= 0) {
				if (System.getProperty("sun.java2d.opengl").equalsIgnoreCase("true")) {
					LOGGER.info("DoaEngine is using fast OpenGL pipeline.");
				} else {
					LOGGER.info("DoaEngine is using slow java2d pipeline.");
				}
			}
			addKeyListener(DoaKeyboard.INPUT);
			addMouseListener(DoaMouse.INPUT);
			addMouseMotionListener(DoaMouse.INPUT);
			addMouseWheelListener(DoaMouse.INPUT);
			DoaEngine.ENGINE = this;
			if (INTERNAL_LOG_LEVEL.compareTo(LogLevel.INFO) >= 0) {
				LOGGER.info("DoaEngine succesfully instantiated!");
			}
		} else {
			if (INTERNAL_LOG_LEVEL.compareTo(LogLevel.SEVERE) >= 0) {
				LOGGER.severe("Instantiation of multiple DoaEngies is disallowed!");
			}
			throw new DoaEngineInstanceException("Multiple DoaEngines are disallowed", "engine.Engine", "Engine != null");
		}
	}

	/**
	 * @return the one and only instance of {@code DoaEngine}
	 */
	public static DoaEngine getInstance() {
		return ENGINE;
	}

	/**
	 * @return true if and only if DoaEngine is running
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Starts the engine.
	 */
	public void start() {
		if (!isRunning) {
			isRunning = true;
			gameThread = new Thread(this);
			gameThread.start();
			if (INTERNAL_LOG_LEVEL.compareTo(LogLevel.INFO) >= 0) {
				LOGGER.info("DoaEngine started!");
			}
		} else {
			if (INTERNAL_LOG_LEVEL.compareTo(LogLevel.WARNING) >= 0) {
				LOGGER.warning("DoaEngine is already started, cannot start again!");
			}
		}
	}

	/**
	 * Stops the engine.
	 */
	public void stop() {
		if (isRunning) {
			isRunning = false;
			try {
				gameThread.join();
				if (INTERNAL_LOG_LEVEL.compareTo(LogLevel.INFO) >= 0) {
					LOGGER.info("DoaEngine stopped!");
				}
			} catch (final InterruptedException ex) {
				Thread.currentThread().interrupt();
				ex.printStackTrace();
			}
		} else {
			if (INTERNAL_LOG_LEVEL.compareTo(LogLevel.WARNING) >= 0) {
				LOGGER.warning("DoaEngine is already stopped, cannot stop again!");
			}
		}
	}

	/**
	 * This method is required to be public, but should never be called explicitly. This is the
	 * Render/Update Loop {@inheritDoc}
	 */
	@Internal
	@Override
	public void run() {
		long timer = System.currentTimeMillis();
		int ticks = 0;
		int frames = 0;
		this.requestFocus();
		long lastTime = System.nanoTime();
		long thisTime;
		final double nanoseconds = 1000000000.0 / TICK_RATE;
		double deltaTime = 0;
		while (isRunning) {
			thisTime = System.nanoTime();
			deltaTime += (thisTime - lastTime) / nanoseconds;
			lastTime = thisTime;
			while (deltaTime >= 1) {
				tick();
				deltaTime--;
				ticks++;
			}
			render();
			frames++;
			if (INTERNAL_LOG_LEVEL.compareTo(LogLevel.FINE) >= 0 && System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				LOGGER.fine("FPS: " + frames + " TICKS: " + ticks);
				ticks = 0;
				frames = 0;
			}
		}
	}

	/**
	 * {@code DoaEngine} tick method.
	 */
	private void tick() {
		DoaKeyboard.tick();
		DoaMouse.tick();
		DoaSceneHandler.getLoadedScene().tick();

		final Component parent = getParent();
		DoaCamera.tick(parent.getWidth(), parent.getHeight());
	}

	/**
	 * {@code DoaEngine} render method.
	 */
	private void render() {
		if (getBufferStrategy() == null) {
			super.createBufferStrategy(3);
		}
		final BufferStrategy bs = getBufferStrategy();

		final DoaGraphicsContext g = new DoaGraphicsContext((Graphics2D) bs.getDrawGraphics());

		if (RENDERING_MODE == DoaRenderingMode.QUALITY) {
			g.setRenderingHints(QUALITY_HINTS);
		} else if (RENDERING_MODE == DoaRenderingMode.BALANCED) {
			g.setRenderingHints(BALANCED_HINTS);
		} else if (RENDERING_MODE == DoaRenderingMode.SPEED) {
			g.setRenderingHints(SPEED_HINTS);
		} else if (RENDERING_MODE == DoaRenderingMode.USER_DEFINED) {
			g.setRenderingHints(USER_HINTS);
		}

		final Component parent = getParent();
		final int width = parent.getWidth();
		final int height = parent.getHeight();

		g.clearRect(0, 0, width, height);
		g.setColor(CLEAR_COLOR != null ? CLEAR_COLOR : Color.BLACK);
		g.fillRect(0, 0, width, height);

		DoaSceneHandler.getLoadedScene().render(g);

		bs.show();
		g.dispose();
	}

	@SuppressWarnings({ "static-method", "unused" })
	private void writeObject(final java.io.ObjectOutputStream stream) throws IOException {
		if (INTERNAL_LOG_LEVEL.compareTo(LogLevel.SEVERE) >= 0) {
			LOGGER.severe("Serialization of DoaEngine is Disallowed");
		}
		throw new NotSerializableException("Serialization of DoaEngine is Disallowed");
	}

	@SuppressWarnings({ "static-method", "unused" })
	private void readObject(final java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		if (INTERNAL_LOG_LEVEL.compareTo(LogLevel.SEVERE) >= 0) {
			LOGGER.severe("Serialization of DoaEngine is Disallowed");
		}
		throw new NotSerializableException("Serialization of DoaEngine is Disallowed");
	}
}