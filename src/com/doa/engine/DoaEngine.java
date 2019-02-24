package com.doa.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.io.NotSerializableException;
import java.util.HashMap;
import java.util.Map;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.input.DoaKeyboard;
import com.doa.engine.input.DoaMouse;

/**
 * Responsible for creating and supplying the graphics context used for drawing
 * all created {@code DoaObject}s, and updating all created {@code DoaObject}s.
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
	public static final String VERSION = "2.1.4";

	/**
	 * Number of updates per second. If {@code DoaEngine} is already running,
	 * {@link DoaEngine#stop()} is advised to call before setting this value.
	 */
	public static int TICK_RATE = 240;

	/**
	 * Enable/Disable verbose debugging to console.
	 */
	public static boolean DEBUG_ENABLED = false;

	/**
	 * <strong>EXPERIMENTAL</strong>
	 * <p>
	 * Determines if DoaEngine should run on multiple cores. The amount of cores
	 * DoaEngine will utilise depends on the system configuration and the OS of the
	 * machine DoaEngine runs on.
	 * </p>
	 */
	public static boolean MULTI_THREAD_ENABLED = false;

	/**
	 * Default background color of render canvas.
	 */
	public static Color CLEAR_COLOR = Color.BLACK;

	/**
	 * Rendering mode of {@code DoaEngine}. Default is
	 * {@link DoaRenderingMode#BALANCED}.
	 *
	 * @see DoaRenderingMode
	 */
	public static DoaRenderingMode RENDERING_MODE = DoaRenderingMode.BALANCED;

	/**
	 * User defined rendering hints. Initially, this map is empty. If none of the
	 * default rendering protocols suffice, any user can set the
	 * {@link DoaEngine#RENDERING_MODE} to {@link DoaRenderingMode#USER_DEFINED} and
	 * add their own {@code RenderingHints} to this map. This way, the rendering
	 * will take place with the user defined Hints.
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

	private boolean isRunning = false;
	private transient Thread gameThread;
	private static DoaEngine ENGINE = null;

	/**
	 * Constructor. Creates a new instance of {@code DoaEngine}.
	 */
	public DoaEngine() {
		if (DoaEngine.ENGINE == null) {
			System.setProperty("sun.java2d.opengl", "True");
			addKeyListener(DoaKeyboard.INPUT);
			addMouseListener(DoaMouse.INPUT);
			addMouseMotionListener(DoaMouse.INPUT);
			addMouseWheelListener(DoaMouse.INPUT);
			DoaEngine.ENGINE = this;
		} else {
			throw new DoaEngineInstanceException("Multiple DoaEngines are disallowed", "engine.Engine", "Engine != null");
		}
	}

	/**
	 * @return the running instance of {@code DoaEngine}
	 */
	public static DoaEngine getInstance() {
		return DoaEngine.ENGINE;
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
		} else {
			throw new DoaEngineStateException("DoaEngine is already running, cannot start again!");
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
			} catch (final InterruptedException ex) {
				Thread.currentThread().interrupt();
				ex.printStackTrace();
			}
		} else {
			throw new DoaEngineStateException("DoaEngine is already stopped, cannot stop again!");
		}
	}

	/**
	 * This method is required to be public, but should never be called explicitly.
	 * Render/Update Loop {@inheritDoc}
	 */
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
			if (DEBUG_ENABLED && System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames + " TICKS: " + ticks);
				ticks = 0;
				frames = 0;
			}
		}
	}

	/**
	 * {@code DoaEngine} tick method.
	 */
	@SuppressWarnings("static-method")
	private void tick() {
		DoaKeyboard.tick();
		DoaMouse.tick();
		DoaHandler.tick();
		DoaCamera.tick();
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

		g.clearRect(0, 0, DoaWindow.WINDOW_WIDTH, DoaWindow.WINDOW_HEIGHT);
		g.setColor(CLEAR_COLOR != null ? CLEAR_COLOR : Color.BLACK);
		g.fillRect(0, 0, DoaWindow.WINDOW_WIDTH, DoaWindow.WINDOW_HEIGHT);

		/* PRE RENDERING STARTS HERE */
		/* | */
		/* | */g.turnOnLightContribution();
		/* | */DoaHandler.renderStaticBackground(g);
		/* V */
		/* RELATIVE RENDERING ENDS HERE */

		/* RELATIVE RENDERING STARTS HERE */
		/* | */g.translate(-DoaCamera.getX(), -DoaCamera.getY());
		/* | */zoomToLookAt(g);
		/* | */
		/* | */
		/* | */DoaHandler.renderBackground(g);
		/* | */DoaHandler.render(g);
		/* | */DoaHandler.renderForeground(g);
		/* | */g.turnOffLightContribution();
		/* | */
		/* V */restoreTransform(g);
		/* RELATIVE RENDERING ENDS HERE */

		/* ADDITIONAL RENDERING STARTS HERE */
		/* | */
		/* | */DoaHandler.renderStaticForeground(g);
		/* V */
		/* ADDITIONAL RENDERING ENDS HERE */

		/* LIGHTING & POST-PROCESSING STARTS HERE */
		/* | */
		/* | */ /* I NEED HELP WITH THIS :( */
		/* V */
		/* LIGHTING & POST-PROCESSING ENDS HERE */

		bs.show();
		g.dispose();
	}

	private static void zoomToLookAt(final DoaGraphicsContext g) {
		if (DoaCamera.isMouseZoomingEnabled()) {
			final DoaObject centerOfZoom = DoaCamera.getObjectToZoomInto();
			if (centerOfZoom != null) {
				g.translate(DoaCamera.getX() + centerOfZoom.position.x + centerOfZoom.width / 2.0,
				        DoaCamera.getY() + centerOfZoom.position.y + centerOfZoom.height / 2.0);
				g.scale(DoaCamera.getZ(), DoaCamera.getZ());
				g.translate(-DoaCamera.getX() - centerOfZoom.position.x - centerOfZoom.width / 2.0,
				        -DoaCamera.getY() - centerOfZoom.position.y - centerOfZoom.height / 2.0);
			} else {
				g.translate(DoaCamera.getX() + DoaWindow.WINDOW_WIDTH / 2.0, DoaCamera.getY() + DoaWindow.WINDOW_HEIGHT / 2.0);
				g.scale(DoaCamera.getZ(), DoaCamera.getZ());
				g.translate(-DoaCamera.getX() - DoaWindow.WINDOW_WIDTH / 2.0, -DoaCamera.getY() - DoaWindow.WINDOW_HEIGHT / 2.0);
			}
		}
	}

	private static void restoreTransform(final DoaGraphicsContext g) {
		g.setTransform(new AffineTransform());
	}

	@SuppressWarnings({ "static-method", "unused" })
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
		throw new NotSerializableException("DoaEngine Serialization Disallowed");
	}

	@SuppressWarnings({ "static-method", "unused" })
	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		throw new NotSerializableException("DoaEngine Serialization Disallowed");
	}
}