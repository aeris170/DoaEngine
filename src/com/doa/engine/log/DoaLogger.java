package com.doa.engine.log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 * Implementation of {@link Logger}.
 *
 * @author Doga Oruc
 * @version 2.6.1
 * @since DoaEngine 2.6.1
 * @see Logger
 * @see LogLevel
 */
public final class DoaLogger implements Logger {

	private static final String DATE_SEQUENCE = "\033[40;37;1m";
	private static final String CLEAR_SEQUENCE = "\033[0m";

	private static DoaLogger _this;

	private final DateFormat dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private PrintStream console = System.out;
	private PrintWriter writer;

	/**
	 * Constructor.
	 */
	private DoaLogger() {}

	public static DoaLogger getInstance() {
		return _this == null ? _this = new DoaLogger() : _this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTarget(String path) throws IOException {
		writer = new PrintWriter(new FileWriter(path));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(LogLevel level, boolean message) {
		log(level, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(LogLevel level, char message) {
		log(level, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(LogLevel level, char[] message) {
		log(level, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(LogLevel level, double message) {
		log(level, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(LogLevel level, float message) {
		log(level, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(LogLevel level, int message) {
		log(level, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(LogLevel level, long message) {
		log(level, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(LogLevel level, Object message) {
		log(level, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(LogLevel level, String message) {
		if(level == LogLevel.OFF) {
			return;
		}
		String time = " " + dt.format(Calendar.getInstance().getTime());
		String prefix = "[" + level.toString() + "]";
		console.print(level.getColorSequence());
		console.print(prefix);
		console.print(DATE_SEQUENCE);
		console.print(time);
		console.print(level.getColorSequence());
		console.print(" " + message);
		if (Objects.nonNull(writer)) {
			writer.print(prefix + time + " " + message);
		}
		newLine();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(boolean message) {
		log(LogLevel.FINEST, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(char message) {
		log(LogLevel.FINEST, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(char[] message) {
		log(LogLevel.FINEST, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(double message) {
		log(LogLevel.FINEST, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(float message) {
		log(LogLevel.FINEST, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(int message) {
		log(LogLevel.FINEST, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(long message) {
		log(LogLevel.FINEST, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(Object message) {
		log(LogLevel.FINEST, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(String message) {
		log(LogLevel.FINEST, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(boolean message) {
		log(LogLevel.FINER, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(char message) {
		log(LogLevel.FINER, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(char[] message) {
		log(LogLevel.FINER, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(double message) {
		log(LogLevel.FINER, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(float message) {
		log(LogLevel.FINER, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(int message) {
		log(LogLevel.FINER, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(long message) {
		log(LogLevel.FINER, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(Object message) {
		log(LogLevel.FINER, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(String message) {
		log(LogLevel.FINER, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(boolean message) {
		log(LogLevel.FINE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(char message) {
		log(LogLevel.FINE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(char[] message) {
		log(LogLevel.FINE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(double message) {
		log(LogLevel.FINE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(float message) {
		log(LogLevel.FINE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(int message) {
		log(LogLevel.FINE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(long message) {
		log(LogLevel.FINE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(Object message) {
		log(LogLevel.FINE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(String message) {
		log(LogLevel.FINE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(boolean message) {
		log(LogLevel.CONFIG, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(char message) {
		log(LogLevel.CONFIG, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(char[] message) {
		log(LogLevel.CONFIG, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(double message) {
		log(LogLevel.CONFIG, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(float message) {
		log(LogLevel.CONFIG, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(int message) {
		log(LogLevel.CONFIG, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(long message) {
		log(LogLevel.CONFIG, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(Object message) {
		log(LogLevel.CONFIG, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(String message) {
		log(LogLevel.CONFIG, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(boolean message) {
		log(LogLevel.INFO, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(char message) {
		log(LogLevel.INFO, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(char[] message) {
		log(LogLevel.INFO, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(double message) {
		log(LogLevel.INFO, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(float message) {
		log(LogLevel.INFO, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(int message) {
		log(LogLevel.INFO, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(long message) {
		log(LogLevel.INFO, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(Object message) {
		log(LogLevel.INFO, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String message) {
		log(LogLevel.INFO, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(boolean message) {
		log(LogLevel.WARNING, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(char message) {
		log(LogLevel.WARNING, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(char[] message) {
		log(LogLevel.WARNING, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(double message) {
		log(LogLevel.WARNING, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(float message) {
		log(LogLevel.WARNING, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(int message) {
		log(LogLevel.WARNING, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(long message) {
		log(LogLevel.WARNING, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(Object message) {
		log(LogLevel.WARNING, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(String message) {
		log(LogLevel.WARNING, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(boolean message) {
		log(LogLevel.SEVERE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(char message) {
		log(LogLevel.SEVERE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(char[] message) {
		log(LogLevel.SEVERE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(double message) {
		log(LogLevel.SEVERE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(float message) {
		log(LogLevel.SEVERE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(int message) {
		log(LogLevel.SEVERE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(long message) {
		log(LogLevel.SEVERE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(Object message) {
		log(LogLevel.SEVERE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(String message) {
		log(LogLevel.SEVERE, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void newLine() {
		console.println(CLEAR_SEQUENCE);
		if (Objects.nonNull(writer)) {
			writer.println();
			writer.flush();
		}
	}
}
