package com.doa.engine.main;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
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
	private static int extractedFiles = 0;

	public static void main(final String[] args) throws IOException, URISyntaxException {
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

	private static void showPage() throws IOException {
		Desktop.getDesktop().browse(new File(TMP_DIR + "/doc/index.html").toURI());
	}

	private static void showLoading() {
		loading = new JWindow();
		panel = new JPanel(null);
		final JLabel top = new JLabel(new ImageIcon(Main.class.getClassLoader().getResource("1.gif")));
		final JLabel bottom = new JLabel(new ImageIcon(Main.class.getClassLoader().getResource("2.gif")));
		top.setBounds(0, -80, 640, 360);
		bottom.setBounds(0, 180, 640, 180);
		panel.add(top);
		panel.add(bottom);
		panel.setComponentZOrder(bottom, 0);
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

	@SuppressWarnings("resource")
	private static void extractRequiredContent() throws IOException, URISyntaxException {
		tmpDirectory.mkdir();
		final JarFile jar = new JarFile(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
		final Enumeration<JarEntry> enumEntries = jar.entries();
		final List<JarEntry> entryList = Collections.list(enumEntries);
		final Iterator<JarEntry> listIterator = entryList.iterator();
		final int totalFiles = entryList.size();
		JProgressBar loadingBar = new JProgressBar();
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
			if (file.isDirectory()) {
				continue;
			}
			final InputStream is = jar.getInputStream(file);
			final FileOutputStream fos = new FileOutputStream(f);
			while (is.available() > 0) {
				fos.write(is.read());
			}
			fos.close();
			is.close();
			extractedFiles++;
			try {
				SwingUtilities.invokeAndWait(() -> loadingBar.setValue(extractedFiles));
			} catch (InvocationTargetException | InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		jar.close();
		new File(tmpDirectory.getPath() + File.separator + "lockFile" + DoaEngine.VERSION).createNewFile();
		delete(new File(TMP_DIR + File.separator + "com"));
		delete(new File(TMP_DIR + File.separator + "1.gif"));
		delete(new File(TMP_DIR + File.separator + "2.gif"));
	}

	private static void delete(final File f) throws IOException {
		if (f.isDirectory()) {
			for (final File c : f.listFiles()) {
				delete(c);
			}
		}
		if (!f.delete()) {
			throw new FileNotFoundException("Failed to delete file: " + f);
		}
	}
}
