package com.doa.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
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
	public static final String VERSION = "2.0";

	/**
	 * Number of updates per second. If {@code DoaEngine} is already running,
	 * {@link DoaEngine#stop()} is advised to call before setting this value.
	 */
	public static int TICK_RATE = 240;

	/**
	 * Enable/Disable verbose debugging to console.
	 */
	public static boolean DEBUG_ENABLED = true;

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
	private Thread gameThread;
	private static DoaEngine ENGINE = null;

	/**
	 * Constructor. Creates a new instance of {@code DoaEngine}.
	 */
	public DoaEngine() {
		if (DoaEngine.ENGINE == null) {
			addKeyListener(DoaKeyboard.INPUT);
			addMouseListener(DoaMouse.INPUT);
			addMouseMotionListener(DoaMouse.INPUT);
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
		long thisTime = System.nanoTime();
		final double nanoseconds = 1000000000 / TICK_RATE;
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
			if (System.currentTimeMillis() - timer > 1000 && DEBUG_ENABLED) {
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
		final BufferStrategy bs = super.getBufferStrategy();
		if (bs == null) {
			super.createBufferStrategy(3);
			return;
		}
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
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, DoaWindow.WINDOW_WIDTH, DoaWindow.WINDOW_HEIGHT);

		/* PRE RENDERING STARTS HERE */
		/* | */
		/* | */g.turnOnLightContribution();
		/* | */DoaHandler.renderStaticBackground(g);
		/* V */
		/* RELATIVE RENDERING ENDS HERE */

		/* RELATIVE RENDERING STARTS HERE */
		/* | */g.translate(-DoaCamera.getX(), -DoaCamera.getY());
		/* | */
		/* | */DoaHandler.render(g);
		/* | */g.turnOffLightContribution();
		/* | */
		/* V */g.translate(DoaCamera.getX(), DoaCamera.getY());
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
}