package com.doa.engine;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.MenuBar;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.NotSerializableException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import com.doa.engine.log.DoaLogger;
import com.doa.engine.log.LogLevel;

/**
 * Responsible for providing a windowing tool for {@code DoaEngine}.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.7
 */
public final class DoaWindow extends JFrame {

	private static final long serialVersionUID = -2046962443183200678L;

	private static final DoaLogger LOGGER = DoaLogger.getInstance();

	private static DoaWindow Window;

	/**
	 * Creates and returns a {@code DoaWindow}. This method should only be called once.
	 *
	 * @return an instance of {@code DoaWindow}
	 */
	public static DoaWindow createWindow() {
		if (Window == null) {
			Window = new DoaWindow();
			Window.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(final ComponentEvent componentEvent) {
					Window.setSize(componentEvent.getComponent().getSize());
				}
			});
			if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.INFO) >= 0) {
				LOGGER.info("DoaWindow succesfully instantiated!");
			}
			return Window;
		}
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.SEVERE) >= 0) {
			LOGGER.severe("Instantiation of multiple DoaWindows is disallowed!");
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
	 * {@inheritDoc} <strong> Note: Calling this method with DoaEngine as the parameter will start
	 * DoaEngine.</strong>
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized Component add(final Component c) {
		final Component rv = super.add(c);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(128).append("Component ").append(c.getClass().getName()).append(" is now added to DoaWindow."));
		}
		if (c instanceof DoaEngine) {
			((DoaEngine) c).start();
		}
		super.revalidate();
		return rv;
	}

	/**
	 * {@inheritDoc} <strong> Note: Calling this method with DoaEngine as the parameter will stop
	 * DoaEngine.</strong>
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void remove(final Component c) {
		if (c instanceof DoaEngine) {
			((DoaEngine) c).stop();
		}
		super.remove(c);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(128).append("Component ").append(c.getClass().getName()).append(" is now removed from DoaWindow."));
		}
		super.revalidate();
	}

	/**
	 * {@inheritDoc} <strong> Note: Calling this method will stop DoaEngine.</strong>
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void removeAll() {
		final Optional<Component> o = Stream.of(getComponents()).filter(c -> c instanceof DoaEngine).findFirst();
		if (o.isPresent()) {
			((DoaEngine) o.get()).stop();
		}
		super.removeAll();
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.INFO) >= 0) {
			LOGGER.info("DoaWindow is now empty.");
		}
		super.revalidate();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setTitle(final String title) {
		super.setTitle(title);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(64).append("DoaWindow's title is now \"").append(title).append("\"."));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setResizable(final boolean resizable) {
		super.setResizable(resizable);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(32).append("DoaWindow is now ").append(resizable ? "resizable." : "not resizable."));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setVisible(final boolean b) {
		super.setVisible(b);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(32).append("DoaWindow is now ").append(b ? "visible." : "invisible."));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setOpacity(final float opacity) {
		super.setOpacity(opacity);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(32).append("DoaWindow is now ").append(opacity * 100).append("% opaque."));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setShape(final Shape shape) {
		super.setShape(shape);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(32).append("DoaWindow is now shaped as ").append(shape).append("."));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setUndecorated(final boolean undecorated) {
		super.setUndecorated(undecorated);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(32).append("DoaWindow is now ").append(undecorated ? "undecorated." : "decorated."));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setSize(final Dimension d) {
		super.setSize(d);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(64).append("DoaWindow's size is now ").append((int) d.getWidth()).append("x").append((int) d.getHeight()).append("."));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setSize(final int width, final int height) {
		super.setSize(width, height);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(64).append("DoaWindow's size is now ").append(width).append("x").append(height).append("."));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setLocation(final Point p) {
		super.setLocation(p);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(64).append("DoaWindow's top left is now at (").append(p.x).append(", ").append(p.y).append(")."));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setLocation(final int x, final int y) {
		super.setLocation(x, y);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(64).append("DoaWindow's top left is now at (").append(x).append(", ").append(y).append(")."));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setLocationRelativeTo(final Component c) {
		super.setLocationRelativeTo(c);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(64).append("DoaWindow's location is now relative to ").append(Objects.nonNull(c) ? c : "center of the screen"));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setLocationByPlatform(final boolean locationByPlatform) {
		super.setLocationByPlatform(locationByPlatform);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0 && locationByPlatform) {
			LOGGER.config("DoaWindow's location is now set by platform.");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setBounds(final Rectangle r) {
		super.setBounds(r);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(64).append("DoaWindow's bounds is now a rectangle of size ").append(r.width).append("x").append(r.height).append(" at (").append(r.x)
			        .append(", ").append(r.y).append(")."));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.awt.Component
	 */
	@Override
	public synchronized void setBounds(final int x, final int y, final int width, final int height) {
		super.setBounds(x, y, width, height);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(64).append("DoaWindow's bounds is now a rectangle of size ").append(width).append("x").append(height).append(" at (").append(x)
			        .append(", ").append(y).append(")."));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setCursor(final Cursor cursor) {
		super.setCursor(cursor);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config("DoaWindow custom cursor is set.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setIconImage(final Image image) {
		super.setIconImage(image);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config("DoaWindow icon image is set.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setIconImages(final List<? extends Image> icons) {
		super.setIconImages(icons);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config("DoaWindow icon images are set.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setLayout(final LayoutManager manager) {
		super.setLayout(manager);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(128).append("DoaWindow's layout is now: ").append(manager.getClass().getName()).append("."));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setMenuBar(final MenuBar mb) {
		super.setMenuBar(mb);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(128).append("DoaWindow MenuBar is now: ").append(mb.getClass().getName()).append("."));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setJMenuBar(final JMenuBar menubar) {
		super.setJMenuBar(menubar);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			LOGGER.config(new StringBuilder(128).append("DoaWindow JMenuBar is now: ").append(menubar.getClass().getName()).append("."));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setDefaultCloseOperation(final int operation) {
		super.setDefaultCloseOperation(operation);
		if (DoaEngine.INTERNAL_LOG_LEVEL.compareTo(LogLevel.CONFIG) >= 0) {
			String operationString;
			switch (operation) {
				case 3:
					operationString = "EXIT_ON_CLOSE";
					break;
				case 2:
					operationString = "DISPOSE_ON_CLOSE";
					break;
				case 1:
					operationString = "HIDE_ON_CLOSE";
					break;
				case 0:
				default:
					operationString = "DO_NOTHING_ON_CLOSE";
					break;
			}
			LOGGER.config(new StringBuilder(128).append("DoaWindow close behaviour is now: WindowConstants#").append(operationString));
		}
	}

	@SuppressWarnings({ "static-method", "unused" })
	private void writeObject(final java.io.ObjectOutputStream stream) throws IOException {
		throw new NotSerializableException("DoaWindow");
	}

	@SuppressWarnings({ "static-method", "unused" })
	private void readObject(final java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		throw new NotSerializableException("DoaWindow");
	}
}