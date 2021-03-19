package doa.main.test;

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
import doa.engine.physics.DoaBodyType;
import doa.engine.physics.DoaPhysics;
import doa.engine.scene.DoaObject;
import doa.engine.scene.DoaScene;
import doa.engine.scene.DoaSceneHandler;
import doa.engine.scene.elements.physics.DoaBoxCollider;
import doa.engine.scene.elements.physics.DoaCircleCollider;
import doa.engine.scene.elements.physics.DoaRigidBody;
import doa.engine.scene.elements.renderers.DoaRectRenderer;
import doa.engine.scene.elements.renderers.DoaSpriteRenderer;

@Internal
public class PhysicsTest extends DoaGame {

	public static void main(final String... args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		DoaScene s = DoaSceneHandler.createScene("sampleScene");

		DoaLogger.getInstance().setLevel(LogLevel.FINEST);

		DoaPhysics.Gravity.y = 10;
		DoaPhysics.PPM = 64;

		DoaObject o = new DoaObject();
		o.transform.position = new DoaVector(200, 50);
		o.transform.rotation = 70;
		DoaSpriteRenderer r = new A();
		r.setSprite(DoaSprites.createSprite("asd", "/simge.jpg"));
		r.setDimensions(r.getDimensions());
		o.addComponent(r);

		DoaRigidBody rb = new DoaRigidBody();
		rb.elasticity = 0.6f;
		rb.collider = new DoaCircleCollider(50);
		o.addComponent(rb);
		s.add(o);

		DoaObject o2 = new DoaObject();
		o2.transform.position = new DoaVector(300, 600);
		o2.transform.rotation = 30;
		DoaRectRenderer r2 = new DoaRectRenderer();
		r2.setDimensions(new DoaVector(600, 10));
		o2.addComponent(r2);

		DoaRigidBody rb2 = new DoaRigidBody();
		rb2.collider = new DoaBoxCollider(r2.getDimensions());
		rb2.type = DoaBodyType.STATIC;
		o2.addComponent(rb2);
		s.add(o2);

		DoaObject o3 = new DoaObject();
		o3.transform.position = new DoaVector(1000, 900);
		DoaRectRenderer r3 = new DoaRectRenderer();
		r3.setDimensions(new DoaVector(900, 10));
		o3.addComponent(r3);

		DoaRigidBody rb3 = new DoaRigidBody();
		rb3.collider = new DoaBoxCollider(r3.getDimensions());
		rb3.type = DoaBodyType.STATIC;
		o3.addComponent(rb3);
		s.add(o3);

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
