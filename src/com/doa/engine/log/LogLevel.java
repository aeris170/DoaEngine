package com.doa.engine.log;

/**
 * Possible logging levels that can be used to signify log message importance.
 * <ul>
 * <li>{@link #OFF} - No Logging</li>
 * <li>{@link #SEVERE} - Serious Failure</li>
 * <li>{@link #WARNING} - Potential Problem</li>
 * <li>{@link #INFO} - General Info</li>
 * <li>{@link #CONFIG} - Configuration Info</li>
 * <li>{@link #FINE} - General Developer Info</li>
 * <li>{@link #FINER} - Detailed Developer Info</li>
 * <li>{@link #FINEST} - Specialized Developer Info</li>
 * </ul>
 * 
 * @author Doga Oruc
 * @version 2.6.1
 * @since DoaEngine 2.6
 * @see Logger
 * @see DoaLogger
 */
public enum LogLevel {

	/**
	 * Log level which indicates no logging
	 */
	OFF("\033[40;31;1m"),

	/**
	 * Log level that should be used to log serious failures
	 */
	SEVERE("\033[40;31;1m"),

	/**
	 * Log level that should be used to log potential problems
	 */
	WARNING("\033[40;33;1m"),

	/**
	 * Log level that should be used to log general info
	 */
	INFO("\033[40;36;1m"),

	/**
	 * Log level that should be used to log configuration info
	 */
	CONFIG("\033[40;32;1m"),

	/**
	 * Log level that should be used to log general developer info
	 */
	FINE("\033[40;37;1m"),

	/**
	 * Log level that should be used to log detailed developer info
	 */
	FINER("\033[40;37;1m"),

	/**
	 * Log level that should be used to log specialized developer info
	 */
	FINEST("\033[40;37;1m");

	private String colorSequence;

	/**
	 * Constructor.
	 * 
	 * @param colorSequence the coloring sequence for content
	 */
	private LogLevel(String colorSequence) {
		this.colorSequence = colorSequence;
	}

	/**
	 * For consoles that support ANSI escape sequences for coloring, returns the
	 * sequence for info content.
	 * 
	 * @return the coloring sequence for content
	 */
	public String getColorSequence() {
		return colorSequence;
	}

	/* Foreground colors 30 Black 31 Red 32 Green 33 Yellow 34 Blue 35 Magenta 36
	 * Cyan 37 White Background colors 40 Black 41 Red 42 Green 43 Yellow 44 Blue 45
	 * Magenta 46 Cyan 47 White */
}