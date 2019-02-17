package com.doa.engine;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.NotSerializableException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Responsible for providing a windowing tool for {@code DoaEngine}.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.1
 */
public final class DoaWindow extends JFrame {

	private static final long serialVersionUID = -2046962443183200678L;

	protected static int WINDOW_WIDTH;
	protected static int WINDOW_HEIGHT;

	private static DoaWindow Window;

	/**
	 * Creates and returns a {@code DoaWindow}. This method should only be called
	 * once.
	 *
	 * @return an instance of {@code DoaWindow}
	 */
	public static DoaWindow createWindow() {
		if (Window == null) {
			Window = new DoaWindow();
			return Window;
		}
		throw new DoaEngineInstanceException("Multiple DoaWindows are disallowed", "engine.DoaWindow", "Window != null");
	}

	/**
	 * Constructor.
	 */
	private DoaWindow() {
		super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * @return the only instance of {@code DoaWindow}
	 */
	public static DoaWindow getInstance() {
		return DoaWindow.Window;
	}

	/**
	 * {@inheritDoc} <strong> Note: Calling this method with DoaEngine as the
	 * parameter will start DoaEngine.</strong>
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized Component add(final Component c) {
		final Component rv = super.add(c);
		if (c instanceof DoaEngine) {
			((DoaEngine) c).start();
		}
		super.revalidate();
		return rv;
	}

	/**
	 * {@inheritDoc} <strong> Note: Calling this method with DoaEngine as the
	 * parameter will stop DoaEngine.</strong>
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void remove(final Component c) {
		if (c instanceof DoaEngine) {
			((DoaEngine) c).stop();
		}
		super.remove(c);
		super.revalidate();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setSize(final Dimension d) {
		super.setSize(d);
		DoaWindow.WINDOW_WIDTH = (int) d.getWidth();
		DoaWindow.WINDOW_HEIGHT = (int) d.getHeight();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setSize(final int width, final int height) {
		super.setSize(width, height);
		DoaWindow.WINDOW_WIDTH = width;
		DoaWindow.WINDOW_HEIGHT = height;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setBounds(final int x, final int y, final int width, final int height) {
		super.setBounds(x, y, width, height);
		DoaWindow.WINDOW_WIDTH = width;
		DoaWindow.WINDOW_HEIGHT = height;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setBounds(final Rectangle r) {
		super.setBounds(r);
		DoaWindow.WINDOW_WIDTH = r.width;
		DoaWindow.WINDOW_HEIGHT = r.height;
	}

	@SuppressWarnings({ "static-method", "unused" })
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
		throw new NotSerializableException("DoaWindow Serialization Disallowed");
	}

	@SuppressWarnings({ "static-method", "unused" })
	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		throw new NotSerializableException("DoaWindow Serialization Disallowed");
	}
}