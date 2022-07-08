package doa.main;

import java.awt.Color;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import doa.engine.Internal;
import doa.engine.core.DoaEngine;
import doa.engine.core.DoaEngineSettings;
import doa.engine.core.DoaGame;
import doa.engine.core.DoaWindowSettings;
import doa.engine.graphics.DoaSprites;
import doa.engine.log.DoaLogger;
import doa.engine.log.LogLevel;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.DoaScene;
import doa.engine.scene.DoaSceneHandler;
import doa.engine.scene.elements.renderers.DoaSpriteRenderer;
import doa.engine.scene.elements.scripts.DoaArrowKeysControl;

@Internal
public class Main extends DoaGame {

	public static void main(final String... args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		DoaScene s = DoaSceneHandler.createScene("sampleScene");

		DoaLogger.getInstance().setLevel(LogLevel.FINEST);

		DoaObject o = new DoaObject();
		DoaSpriteRenderer r = new DoaSpriteRenderer();
		r.setSprite(DoaSprites.createSprite("asd", "/simge.jpg"));
		r.setDimensions(new DoaVector(100, 100));
		r.setOffset(new DoaVector(50, 50));
		o.addComponent(r);
		o.addComponent(new DoaArrowKeysControl());
		s.add(o);

		DoaSceneHandler.loadScene(s);

		launch(args);
	}

	@Override
	public void initialize(DoaEngineSettings eSettings, DoaWindowSettings wSettings, String... args) {
		eSettings.TICK_RATE = 240;
		eSettings.CLEAR_COLOR = Color.BLACK;
		eSettings.AXIS_HELPERS = true;

		wSettings.TITLE = "DoaEngine " + DoaEngine.VERSION;
	}
}
