package doa.engine.graphics;

import static doa.engine.log.DoaLogger.LOGGER;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.javatuples.Pair;

/**
 * Responsible for providing a tool to create and set scene lights for
 * DoaEngine. This class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.1
 * @version 3.0
 */
public final class DoaLights {

	private static Color ambientLightColor = Color.WHITE;

	private DoaLights() {}

	/**
	 * Replaces the current ambient light with a new ambient light with the passed
	 * color. When a new ambient light is set, all DoaObjects DoaEngine renders will
	 * be affected by the new ambient light except the DoaObjects that are fixed,
	 * DoaUIComponents and the sprites and animations created without DoaSprites or
	 * DoaAnimations class.
	 * <p>
	 * Note: This operation is costly, and should be done on offscreen times, i.e.
	 * loading screens.
	 * <p>
	 * Note: If this method has not yet been invoked, the ambient light color will
	 * be {@link Color#WHITE}, meaning rendering will not be affected by ambient
	 * light at all.
	 *
	 * @param newAmbientLightColor color of the new ambient light
	 */
	public static void ambientLight(@NotNull final Color newAmbientLightColor) {
		if (ambientLightColor.equals(newAmbientLightColor)) { return; }
		DoaLights.ambientLightColor = newAmbientLightColor;

		int spriteCount = DoaSprites.ORIGINAL_SPRITES.size();

		int threadCount = Math.max((int) Math.sqrt(spriteCount), 1);
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(spriteCount);

		List<Pair<BufferedImage, String>> pairs = new ArrayList<>();
		DoaSprites.ORIGINAL_SPRITES.forEach((k, v) -> pairs.add(new Pair<>(v, k)));
		Deque<Pair<BufferedImage, String>> pairsSortedByArea = pairs.stream().sorted(
		        Comparator.comparingDouble(pair -> 1d / (pair.getValue0().getWidth() * pair.getValue0().getHeight()))).collect(
		                Collectors.toCollection(ConcurrentLinkedDeque::new));
		new Thread(() -> {
			Map<String, BufferedImage> shadedSprites = new HashMap<>();
			int size = pairsSortedByArea.size(); // executor may pop while for loops is running, must cache!
			for (int i = 0; i < size; i++) {
				executor.submit(() -> {
					Pair<BufferedImage, String> pair = pairsSortedByArea.pop();
					try {
						shadedSprites.put(pair.getValue1(), applyAmbientLight(pair.getValue0()));
					} catch (Exception ex) {
						ex.printStackTrace();
						shadedSprites.put(pair.getValue1(), pair.getValue0());
					} finally {
						latch.countDown();
					}
				});
			}
			executor.shutdown();
			try {
				latch.await();
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
				ex.printStackTrace();
			}
			DoaSprites.SHADED_SPRITES.clear();
			DoaSprites.SHADED_SPRITES.putAll(shadedSprites);
			LOGGER.fine(new StringBuilder(32).append("Ambient light set to R: ").append(newAmbientLightColor.getRed()).append(", G: ").append(
			        newAmbientLightColor.getGreen()).append(", B: ").append(newAmbientLightColor.getBlue()).append("."));
		}).start();
	}

	/**
	 * Returns the color of the current ambient light DoaEngine uses to shade
	 * DoaObjects.
	 *
	 * @return the current ambient light color
	 */
	public static Color getAmbientLightColor() { return ambientLightColor; }

	static BufferedImage applyAmbientLight(final BufferedImage sp) {
		final BufferedImage spclone = new BufferedImage(sp.getWidth(), sp.getHeight(), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2d = spclone.createGraphics();
		g2d.drawImage(sp, 0, 0, null);
		g2d.dispose();

		for (int xx = 0; xx < spclone.getWidth(); xx++) {
			for (int yy = 0; yy < spclone.getHeight(); yy++) {
				final Color objColor = new Color(spclone.getRGB(xx, yy), true);
				final int r = objColor.getRed() * ambientLightColor.getRed() / 255;
				final int g = objColor.getGreen() * ambientLightColor.getGreen() / 255;
				final int b = objColor.getBlue() * ambientLightColor.getBlue() / 255;
				final int a = objColor.getAlpha();
				spclone.setRGB(xx, yy, new Color(r, g, b, a).getRGB());
			}
		}
		return spclone;
	}

	static DoaAnimation applyAmbientLight(final DoaAnimation anim) {
		final List<BufferedImage> frames = new ArrayList<>();
		for (final BufferedImage sp : anim.getFrames()) {
			final BufferedImage spclone = applyAmbientLight(sp);
			frames.add(spclone);
		}
		return new DoaAnimation(frames, anim.getDelay());
	}
}
