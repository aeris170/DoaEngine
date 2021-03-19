package doa.main.test;

import static doa.engine.core.DoaGraphicsFunctions.popClip;
import static doa.engine.core.DoaGraphicsFunctions.pushClip;
import static doa.engine.core.DoaGraphicsFunctions.setClip;

import java.awt.geom.Ellipse2D;

import doa.engine.scene.elements.renderers.DoaSpriteRenderer;

public class A extends DoaSpriteRenderer {

	@Override
	public void render() {
		pushClip();
		setClip(new Ellipse2D.Float(-50, -50, 100, 100));
		super.render();
		popClip();
	}
}
