package com.doa.engine.main;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import com.doa.engine.DoaEngine;

public class Main {

	private static final String TMP_DIR = System.getProperty("user.home") + File.separator + "DoaEngineTMP";
	private static File tmpDirectory;
	private static JWindow loading;
	private static JPanel panel;
	private static JProgressBar loadingBar;
	private static Font font;
	private static int extractedFiles = 0;

	public static void main(final String[] args) {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/TOFFEE.otf"));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
		} catch (IOException | FontFormatException ex) {
			ex.printStackTrace();
		}
		try {
			Thread.sleep(100);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
		tmpDirectory = new File(TMP_DIR);
		if (tmpDirectory.exists()) {
			if (new File(tmpDirectory.getPath() + File.separator + "lockFile" + DoaEngine.VERSION).exists()) {
				showPage();
			} else {
				delete(new File(TMP_DIR));
				showLoading();
				extractRequiredContent();
				hideLoading();
				showPage();
			}
		} else {
			showLoading();
			extractRequiredContent();
			hideLoading();
			showPage();
		}
	}

	private static void showPage() {
		try {
			Desktop.getDesktop().browse(new File(TMP_DIR + "/doc/index.html").toURI());
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}

	private static void showLoading() {
		loading = new JWindow();
		panel = new JPanel(null);
		final JLabel top = new JLabel(new ImageIcon(Main.class.getClassLoader().getResource("1.gif")));
		final JLabel bottom = new JLabel(new ImageIcon(Main.class.getClassLoader().getResource("2.gif")));
		final JLabel v = new JLabel("v");
		final JLabel version = new JLabel(DoaEngine.VERSION);
		v.setFont(font.deriveFont(Font.ITALIC, 6f));
		System.out.println(v.getFont().getSize());
		v.setForeground(new Color(255, 255, 255, 200));
		v.setBackground(new Color(0, 0, 0, 0));
		version.setFont(font.deriveFont(Font.ITALIC, 10f));
		System.out.println(version.getFont().getSize());
		version.setForeground(new Color(255, 255, 255, 200));
		version.setBackground(new Color(0, 0, 0, 0));
		top.setBounds(0, -80, 640, 360);
		bottom.setBounds(0, 180, 640, 180);
		v.setBounds(452, 159, 102, 39);
		version.setBounds(460, 160, 102, 39);
		panel.add(top);
		panel.setComponentZOrder(bottom, 0);
		panel.setComponentZOrder(v, 0);
		panel.setComponentZOrder(version, 0);
		loading.add(panel);
		loading.setBounds(0, 0, 640, 350);
		loading.setLocationRelativeTo(null);
		loading.setVisible(true);
		loading.setBackground(new Color(20, 20, 20));
	}

	private static void hideLoading() {
		loading.setVisible(false);
		loading.dispose();
	}

	private static void extractRequiredContent() {
		tmpDirectory.mkdir();
		try (final JarFile jar = new JarFile(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()))) {
			final Enumeration<JarEntry> enumEntries = jar.entries();
			final List<JarEntry> entryList = Collections.list(enumEntries);
			final Iterator<JarEntry> listIterator = entryList.iterator();
			final int totalFiles = entryList.size();
			loadingBar = new JProgressBar();
			loadingBar.setForeground(Color.GREEN);
			loadingBar.setBackground(new Color(20, 20, 20));
			loadingBar.setBorderPainted(false);
			loadingBar.setMinimum(0);
			loadingBar.setMaximum(totalFiles);
			loadingBar.setBounds(0, 330, 640, 20);
			loadingBar.setValue(extractedFiles);
			panel.add(loadingBar);
			while (listIterator.hasNext()) {
				final JarEntry file = listIterator.next();
				File f = new File(TMP_DIR + File.separator, file.getName());
				if (!f.exists()) {
					final File parent = f.getParentFile();
					if (parent.getName().equals("com") || parent.getName().equals("META-INF")) {
						continue;
					}
					parent.mkdirs();
					f = new File(TMP_DIR + File.separator, file.getName());
				}
				if (!file.isDirectory()) {
					try (final InputStream is = jar.getInputStream(file)) {
						try (final FileOutputStream fos = new FileOutputStream(f)) {
							while (is.available() > 0) {
								fos.write(is.read());
							}
						}
					}
					updateLoadingBar(extractedFiles++);
				}
			}
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
		}
		try {
			new File(tmpDirectory.getPath() + File.separator + "lockFile" + DoaEngine.VERSION).createNewFile();
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		delete(new File(TMP_DIR + File.separator + "com"));
		delete(new File(TMP_DIR + File.separator + "1.gif"));
		delete(new File(TMP_DIR + File.separator + "2.gif"));
	}

	private static void delete(final File f) {
		if (f.isDirectory()) {
			for (final File c : f.listFiles()) {
				if (c != null) {
					delete(c);
				}
			}
		}
		try {
			Files.delete(Paths.get(f.getPath()));
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}

	private static void updateLoadingBar(final int newValue) {
		try {
			SwingUtilities.invokeAndWait(() -> loadingBar.setValue(newValue));
		} catch (final InvocationTargetException ex) {
			ex.printStackTrace();
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
	}
}
