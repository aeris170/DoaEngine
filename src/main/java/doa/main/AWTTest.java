package doa.main;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

public class AWTTest {
	public static void main(String[] args) {
		JFrame frame = new JFrame("AWT test");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(600, 600));
		GLData data = new GLData();
		data.samples = 4;
		data.swapInterval = 0;
		AWTGLCanvas canvas;
		frame.add(canvas = new AWTGLCanvas(data) {
			private static final long serialVersionUID = 1L;

			@Override
			public void initGL() {
				System.out.println("OpenGL version: " + effective.majorVersion + "." + effective.minorVersion + " (Profile: " + effective.profile + ")");
				createCapabilities();
				glClearColor(0.3f, 0.4f, 0.5f, 1);
			}

			@Override
			public void paintGL() {
				int w = getWidth();
				int h = getHeight();
				float aspect = (float) w / h;
				double now = System.currentTimeMillis() * 0.001;
				float width = (float) Math.abs(Math.sin(now * 0.3));
				glClear(GL_COLOR_BUFFER_BIT);
				glViewport(0, 0, w, h);
				glBegin(GL_QUADS);
				glColor3f(0.4f, 0.6f, 0.8f);
				glVertex2f(-0.75f * width / aspect, 0.0f);
				glVertex2f(0, -0.75f);
				glVertex2f(+0.75f * width / aspect, 0);
				glVertex2f(0, +0.75f);
				glEnd();
				swapBuffers();
			}
		}, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.transferFocus();

		Runnable renderLoop = new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (!canvas.isValid()) {
						GL.setCapabilities(null);
						return;
					}
					canvas.render();
				}
			}
		};
		new Thread(renderLoop).start();

	}
}
