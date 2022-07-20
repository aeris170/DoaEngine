package doa.engine.core;

import static doa.engine.core.DoaGraphicsFunctions.dispose;
import static doa.engine.core.DoaGraphicsFunctions.drawLine;
import static doa.engine.core.DoaGraphicsFunctions.fillRect;
import static doa.engine.core.DoaGraphicsFunctions.set;
import static doa.engine.core.DoaGraphicsFunctions.setColor;
import static doa.engine.core.DoaGraphicsFunctions.setRenderingHints;
import static doa.engine.core.DoaGraphicsFunctions.setStroke;
import static doa.engine.log.DoaLogger.LOGGER;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.Map;

import doa.engine.input.DoaKeyboard;
import doa.engine.input.DoaMouse;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaScene;
import doa.engine.scene.DoaSceneHandler;

/**
 * Responsible for creating and supplying the graphics context used for drawing
 * all created DoaObjects, and updating all created DoaObjects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version {@value DoaEngine#VERSION}
 */
public final class DoaEngine {

	/**
	 * Current version of DoaEngine.
	 */
	public static final String VERSION = "3.0";

	Thread gameThread;
	Canvas surface;
	private int ticks = 0;
	private int frames = 0;
	int tps = 0;
	int fps = 0;

	private int ticksPerSecond;
	private Color clearColor;
	private Map<RenderingHints.Key, Object> hints;
	private boolean axisHelpers;
	private DoaVector refResolution;
	private DoaVector actResolution;

	/**
	 * Constructor. Creates a new instance of DoaEngine.
	 * <p>
	 * <strong>Multiple instances of DoaEngine is disallowed!</strong>
	 */
	DoaEngine(final DoaEngineSettings esettings, final DoaWindowSettings wSettings) {
		ticksPerSecond = esettings.TICK_RATE;
		clearColor = esettings.CLEAR_COLOR;
		hints = switch (esettings.RENDERING_MODE) {
			case QUALITY -> esettings.QUALITY_HINTS;
			case BALANCED -> esettings.BALANCED_HINTS;
			case SPEED -> esettings.SPEED_HINTS;
			case CUSTOM -> esettings.CUSTOM_HINTS;
		};
		axisHelpers = esettings.AXIS_HELPERS;
		refResolution = esettings.REFERENCE_RESOLUTION;
		if (wSettings.RESOLUTION_OD != null) {
			actResolution = wSettings.RESOLUTION_OD;	
		} else {
			actResolution = wSettings.DM.getResolution();
		}
		
		surface = new Canvas();
		surface.addKeyListener(DoaKeyboard.INPUT);
		surface.addMouseListener(DoaMouse.INPUT);
		surface.addMouseMotionListener(DoaMouse.INPUT);
		surface.addMouseWheelListener(DoaMouse.INPUT);
		surface.setIgnoreRepaint(true);
		LOGGER.info("DoaEngine succesfully instantiated!");
	}

	/**
	 * Starts the engine.
	 */
	void start() {
		gameThread = new GameThread();
		gameThread.start();
		LOGGER.info("DoaEngine started!");
	}

	private class GameThread extends Thread {

		private GameThread() {
			setName("DoaEngine Game Loop");
			DoaCamera.init(refResolution, actResolution);
		}

		private void tick() {
			DoaKeyboard.tick();
			DoaMouse.tick();
			final DoaScene loadedScene = DoaSceneHandler.getLoadedScene();
			if (loadedScene != null) {
				loadedScene.tick(ticksPerSecond);
			}
			DoaCamera.tick();
		}

		private void render() {
			if (surface.getBufferStrategy() == null) {
				surface.createBufferStrategy(3);
			}
			final BufferStrategy bs = surface.getBufferStrategy();
			set((Graphics2D) bs.getDrawGraphics(), refResolution, actResolution);

			setRenderingHints(hints);

			setColor(clearColor);
			fillRect(0, 0, refResolution.x, refResolution.y);

			final DoaScene loadedScene = DoaSceneHandler.getLoadedScene();
			loadedScene.render();

			if (axisHelpers) {
				setStroke(new BasicStroke(2));
				setColor(Color.RED);
				drawLine(0, refResolution.y * .5f - 1, refResolution.x, refResolution.y * .5f - 1);
				setColor(Color.GREEN);
				drawLine(refResolution.x * .5f - 1, 0, refResolution.x * 0.5f - 1, refResolution.y);
			}
			frames++;

			bs.show();
			dispose();
		}

		@Override
		public void run() {
			long timer = System.currentTimeMillis();
			surface.requestFocus();
			long lastTime = System.nanoTime();
			long thisTime;
			final double nanoseconds = 1_000_000_000.0 / ticksPerSecond;
			double deltaTime = 0;
			while (true) {
				thisTime = System.nanoTime();
				deltaTime += (thisTime - lastTime) / nanoseconds;
				lastTime = thisTime;
				while (deltaTime >= 1) {
					tick();
					deltaTime--;
					ticks++;
				}
				render();
				if (System.currentTimeMillis() - timer > 1000) {
					timer += 1000;
					tps = ticks;
					fps = frames;
					ticks = 0;
					frames = 0;
					LOGGER.fine(new StringBuilder().append("FPS: ").append(fps).append(" TICKS: ").append(tps));
				}
			}
		}
	}
}
