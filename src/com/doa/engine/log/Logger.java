package com.doa.engine.log;

import java.io.IOException;

/**
 * A simple logger interface.
 *
 * @author Doga Oruc
 * @version 2.6.1
 * @since DoaEngine 2.6
 * @see DoaLogger
 * @see LogLevel
 */
public interface Logger {

	/**
	 * Sets an external target file. The external target is guaranteed to receive
	 * all logs after this method returns except for the few cases listed below.
	 * <ul>
	 * <li>The target exists but is a directory rather than a regular file</li>
	 * <li>The target does not exist but cannot be created</li>
	 * <li>The target cannot be opened for any other reason</li>
	 * </ul>
	 * In the cases listed above, an {@link IOException} will be thrown.
	 *
	 * @param path the new target
	 * @throws IOException if the named file exists but is a directory rather than a
	 *         regular file, does not exist but cannot be created, or cannot be
	 *         opened for any other reason
	 */
	void setTarget(String path) throws IOException;

	/**
	 * Logs a message at a level.
	 *
	 * @param level the log level to log at
	 * @param message the message to log
	 */
	void log(LogLevel level, boolean message);

	/**
	 * Logs a message at a level.
	 *
	 * @param level the log level to log at
	 * @param message the message to log
	 */
	void log(LogLevel level, char message);

	/**
	 * Logs a message at a level.
	 *
	 * @param level the log level to log at
	 * @param message the message to log
	 */
	void log(LogLevel level, char[] message);

	/**
	 * Logs a message at a level.
	 *
	 * @param level the log level to log at
	 * @param message the message to log
	 */
	void log(LogLevel level, double message);

	/**
	 * Logs a message at a level.
	 *
	 * @param level the log level to log at
	 * @param message the message to log
	 */
	void log(LogLevel level, float message);

	/**
	 * Logs a message at a level.
	 *
	 * @param level the log level to log at
	 * @param message the message to log
	 */
	void log(LogLevel level, int message);

	/**
	 * Logs a message at a level.
	 *
	 * @param level the log level to log at
	 * @param message the message to log
	 */
	void log(LogLevel level, long message);

	/**
	 * Logs a message at a level.
	 *
	 * @param level the log level to log at
	 * @param message the message to log
	 */
	void log(LogLevel level, Object message);

	/**
	 * Logs a message at a level.
	 *
	 * @param level the log level to log at
	 * @param message the message to log
	 */
	void log(LogLevel level, String message);

	/**
	 * Logs a message at {@link LogLevel#FINEST} level.
	 *
	 * @param message the message to log
	 */
	void finest(boolean message);

	/**
	 * Logs a message at {@link LogLevel#FINEST} level.
	 *
	 * @param message the message to log
	 */
	void finest(char message);

	/**
	 * Logs a message at {@link LogLevel#FINEST} level.
	 *
	 * @param message the message to log
	 */
	void finest(char[] message);

	/**
	 * Logs a message at {@link LogLevel#FINEST} level.
	 *
	 * @param message the message to log
	 */
	void finest(double message);

	/**
	 * Logs a message at {@link LogLevel#FINEST} level.
	 *
	 * @param message the message to log
	 */
	void finest(float message);

	/**
	 * Logs a message at {@link LogLevel#FINEST} level.
	 *
	 * @param message the message to log
	 */
	void finest(int message);

	/**
	 * Logs a message at {@link LogLevel#FINEST} level.
	 *
	 * @param message the message to log
	 */
	void finest(long message);

	/**
	 * Logs a message at {@link LogLevel#FINEST} level.
	 *
	 * @param message the message to log
	 */
	void finest(Object message);

	/**
	 * Logs a message at {@link LogLevel#FINEST} level.
	 *
	 * @param message the message to log
	 */
	void finest(String message);

	/**
	 * Logs a message at {@link LogLevel#FINER} level.
	 *
	 * @param message the message to log
	 */
	void finer(boolean message);

	/**
	 * Logs a message at {@link LogLevel#FINER} level.
	 *
	 * @param message the message to log
	 */
	void finer(char message);

	/**
	 * Logs a message at {@link LogLevel#FINER} level.
	 *
	 * @param message the message to log
	 */
	void finer(char[] message);

	/**
	 * Logs a message at {@link LogLevel#FINER} level.
	 *
	 * @param message the message to log
	 */
	void finer(double message);

	/**
	 * Logs a message at {@link LogLevel#FINER} level.
	 *
	 * @param message the message to log
	 */
	void finer(float message);

	/**
	 * Logs a message at {@link LogLevel#FINER} level.
	 *
	 * @param message the message to log
	 */
	void finer(int message);

	/**
	 * Logs a message at {@link LogLevel#FINER} level.
	 *
	 * @param message the message to log
	 */
	void finer(long message);

	/**
	 * Logs a message at {@link LogLevel#FINER} level.
	 *
	 * @param message the message to log
	 */
	void finer(Object message);

	/**
	 * Logs a message at {@link LogLevel#FINER} level.
	 *
	 * @param message the message to log
	 */
	void finer(String message);

	/**
	 * Logs a message at {@link LogLevel#FINE} level.
	 *
	 * @param message the message to log
	 */
	void fine(boolean message);

	/**
	 * Logs a message at {@link LogLevel#FINE} level.
	 *
	 * @param message the message to log
	 */
	void fine(char message);

	/**
	 * Logs a message at {@link LogLevel#FINE} level.
	 *
	 * @param message the message to log
	 */
	void fine(char[] message);

	/**
	 * Logs a message at {@link LogLevel#FINE} level.
	 *
	 * @param message the message to log
	 */
	void fine(double message);

	/**
	 * Logs a message at {@link LogLevel#FINE} level.
	 *
	 * @param message the message to log
	 */
	void fine(float message);

	/**
	 * Logs a message at {@link LogLevel#FINE} level.
	 *
	 * @param message the message to log
	 */
	void fine(int message);

	/**
	 * Logs a message at {@link LogLevel#FINE} level.
	 *
	 * @param message the message to log
	 */
	void fine(long message);

	/**
	 * Logs a message at {@link LogLevel#FINE} level.
	 *
	 * @param message the message to log
	 */
	void fine(Object message);

	/**
	 * Logs a message at {@link LogLevel#FINE} level.
	 *
	 * @param message the message to log
	 */
	void fine(String message);

	/**
	 * Logs a message at {@link LogLevel#CONFIG} level.
	 *
	 * @param message the message to log
	 */
	void config(boolean message);

	/**
	 * Logs a message at {@link LogLevel#CONFIG} level.
	 *
	 * @param message the message to log
	 */
	void config(char message);

	/**
	 * Logs a message at {@link LogLevel#CONFIG} level.
	 *
	 * @param message the message to log
	 */
	void config(char[] message);

	/**
	 * Logs a message at {@link LogLevel#CONFIG} level.
	 *
	 * @param message the message to log
	 */
	void config(double message);

	/**
	 * Logs a message at {@link LogLevel#CONFIG} level.
	 *
	 * @param message the message to log
	 */
	void config(float message);

	/**
	 * Logs a message at {@link LogLevel#CONFIG} level.
	 *
	 * @param message the message to log
	 */
	void config(int message);

	/**
	 * Logs a message at {@link LogLevel#CONFIG} level.
	 *
	 * @param message the message to log
	 */
	void config(long message);

	/**
	 * Logs a message at {@link LogLevel#CONFIG} level.
	 *
	 * @param message the message to log
	 */
	void config(Object message);

	/**
	 * Logs a message at {@link LogLevel#CONFIG} level.
	 *
	 * @param message the message to log
	 */
	void config(String message);

	/**
	 * Logs a message at {@link LogLevel#INFO} level.
	 *
	 * @param message the message to log
	 */
	void info(boolean message);

	/**
	 * Logs a message at {@link LogLevel#INFO} level.
	 *
	 * @param message the message to log
	 */
	void info(char message);

	/**
	 * Logs a message at {@link LogLevel#INFO} level.
	 *
	 * @param message the message to log
	 */
	void info(char[] message);

	/**
	 * Logs a message at {@link LogLevel#INFO} level.
	 *
	 * @param message the message to log
	 */
	void info(double message);

	/**
	 * Logs a message at {@link LogLevel#INFO} level.
	 *
	 * @param message the message to log
	 */
	void info(float message);

	/**
	 * Logs a message at {@link LogLevel#INFO} level.
	 *
	 * @param message the message to log
	 */
	void info(int message);

	/**
	 * Logs a message at {@link LogLevel#INFO} level.
	 *
	 * @param message the message to log
	 */
	void info(long message);

	/**
	 * Logs a message at {@link LogLevel#INFO} level.
	 *
	 * @param message the message to log
	 */
	void info(Object message);

	/**
	 * Logs a message at {@link LogLevel#INFO} level.
	 *
	 * @param message the message to log
	 */
	void info(String message);

	/**
	 * Logs a message at {@link LogLevel#WARNING} level.
	 *
	 * @param message the message to log
	 */
	void warning(boolean message);

	/**
	 * Logs a message at {@link LogLevel#WARNING} level.
	 *
	 * @param message the message to log
	 */
	void warning(char message);

	/**
	 * Logs a message at {@link LogLevel#WARNING} level.
	 *
	 * @param message the message to log
	 */
	void warning(char[] message);

	/**
	 * Logs a message at {@link LogLevel#WARNING} level.
	 *
	 * @param message the message to log
	 */
	void warning(double message);

	/**
	 * Logs a message at {@link LogLevel#WARNING} level.
	 *
	 * @param message the message to log
	 */
	void warning(float message);

	/**
	 * Logs a message at {@link LogLevel#WARNING} level.
	 *
	 * @param message the message to log
	 */
	void warning(int message);

	/**
	 * Logs a message at {@link LogLevel#WARNING} level.
	 *
	 * @param message the message to log
	 */
	void warning(long message);

	/**
	 * Logs a message at {@link LogLevel#WARNING} level.
	 *
	 * @param message the message to log
	 */
	void warning(Object message);

	/**
	 * Logs a message at {@link LogLevel#WARNING} level.
	 *
	 * @param message the message to log
	 */
	void warning(String message);

	/**
	 * Logs a message at {@link LogLevel#SEVERE} level.
	 *
	 * @param message the message to log
	 */
	void severe(boolean message);

	/**
	 * Logs a message at {@link LogLevel#SEVERE} level.
	 *
	 * @param message the message to log
	 */
	void severe(char message);

	/**
	 * Logs a message at {@link LogLevel#SEVERE} level.
	 *
	 * @param message the message to log
	 */
	void severe(char[] message);

	/**
	 * Logs a message at {@link LogLevel#SEVERE} level.
	 *
	 * @param message the message to log
	 */
	void severe(double message);

	/**
	 * Logs a message at {@link LogLevel#SEVERE} level.
	 *
	 * @param message the message to log
	 */
	void severe(float message);

	/**
	 * Logs a message at {@link LogLevel#SEVERE} level.
	 *
	 * @param message the message to log
	 */
	void severe(int message);

	/**
	 * Logs a message at {@link LogLevel#SEVERE} level.
	 *
	 * @param message the message to log
	 */
	void severe(long message);

	/**
	 * Logs a message at {@link LogLevel#SEVERE} level.
	 *
	 * @param message the message to log
	 */
	void severe(Object message);

	/**
	 * Logs a message at {@link LogLevel#SEVERE} level.
	 *
	 * @param message the message to log
	 */
	void severe(String message);

	/**
	 * Sends a new line character(\r\n | \r | \n) to the loggers output, and turns
	 * off all coloring attributes.
	 */
	void newLine();
}
