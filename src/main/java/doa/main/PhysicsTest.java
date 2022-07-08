package doa.main;

import static doa.engine.core.DoaGraphicsFunctions.popClip;
import static doa.engine.core.DoaGraphicsFunctions.popTransform;
import static doa.engine.core.DoaGraphicsFunctions.pushClip;
import static doa.engine.core.DoaGraphicsFunctions.pushTransform;
import static doa.engine.core.DoaGraphicsFunctions.setClip;
import static doa.engine.core.DoaGraphicsFunctions.translate;

import java.awt.geom.Ellipse2D;
import java.io.IOException;

import doa.engine.Internal;
import doa.engine.core.DoaEngine;
import doa.engine.core.DoaEngineSettings;
import doa.engine.core.DoaGame;
import doa.engine.core.DoaWindowMode;
import doa.engine.core.DoaWindowSettings;
import doa.engine.graphics.DoaSprites;
import doa.engine.log.DoaLogger;
import doa.engine.log.LogLevel;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaObject;
import doa.engine.scene.DoaPhysics;
import doa.engine.scene.DoaScene;
import doa.engine.scene.DoaSceneHandler;
import doa.engine.scene.elements.physics.DoaBodyType;
import doa.engine.scene.elements.physics.DoaBoxCollider;
import doa.engine.scene.elements.physics.DoaCircleCollider;
import doa.engine.scene.elements.physics.DoaRigidBody;
import doa.engine.scene.elements.renderers.DoaRectRenderer;
import doa.engine.scene.elements.renderers.DoaSpriteRenderer;

@Internal
public class PhysicsTest extends DoaGame {

	public static void main(final String... args) throws IOException {
		DoaScene s = DoaSceneHandler.createScene("sampleScene");

		DoaLogger.getInstance().setLevel(LogLevel.FINEST);

		DoaPhysics.Gravity.y = 10;
		DoaPhysics.PPM = 64;

		DoaObject o = new DoaObject();
		o.transform.position = new DoaVector(400, 50);
		o.transform.rotation = 70;
		DoaSpriteRenderer r = new DoaSpriteRenderer() {
			@Override
			public void render() {
				pushTransform();
				translate(-50, -50);
				pushClip();
				setClip(new Ellipse2D.Float(0, 0, 100, 100));
				super.render();
				popClip();
				popTransform();
			}
		};
		r.setSprite(DoaSprites.createSprite("asd", "/simge.jpg"));
		r.setDimensions(new DoaVector(100, 100));
		o.addComponent(r);

		DoaRigidBody rb = new DoaRigidBody();
		rb.elasticity = 0.6f;
		rb.colliders.add(new DoaCircleCollider(50));
		rb.debugRender = true;
		o.addComponent(rb);
		s.add(o);

		DoaObject o2 = new DoaObject();
		o2.transform.position = new DoaVector(300, 600);
		o2.transform.rotation = 30;
		DoaRectRenderer r2 = new DoaRectRenderer();
		r2.setDimensions(new DoaVector(600, 10));
		o2.addComponent(r2);

		DoaRigidBody rb2 = new DoaRigidBody();
		rb2.colliders.add(new DoaBoxCollider(r2.getDimensions(), new DoaVector(r2.getDimensions().x / 2, r2.getDimensions().y / 2)));
		rb2.type = DoaBodyType.STATIC;
		rb2.debugRender = true;
		o2.addComponent(rb2);
		s.add(o2);

		DoaObject o3 = new DoaObject();
		o3.transform.position = new DoaVector(1000, 900);
		DoaRectRenderer r3 = new DoaRectRenderer();
		r3.setDimensions(new DoaVector(900, 10));
		o3.addComponent(r3);

		DoaRigidBody rb3 = new DoaRigidBody();
		rb3.colliders.add(new DoaBoxCollider(r3.getDimensions(), new DoaVector(r3.getDimensions().x / 2, r3.getDimensions().y / 2)));
		rb3.type = DoaBodyType.STATIC;
		rb3.debugRender = true;
		o3.addComponent(rb3);
		s.add(o3);

		DoaSceneHandler.loadScene(s);

		launch(args);
	}

	@Override
	public void initialize(DoaEngineSettings eSettings, DoaWindowSettings wSettings, String... args) {
		eSettings.AXIS_HELPERS = true;

		wSettings.RESOLUTION_OD = new DoaVector(1600, 900);
		wSettings.WM = DoaWindowMode.WINDOWED;
		wSettings.TITLE = "DoaEngine " + DoaEngine.VERSION + " Physics Test";
	}
}
