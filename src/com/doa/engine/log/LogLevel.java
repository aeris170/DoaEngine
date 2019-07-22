package com.doa.engine.log;

/**
 * Possible logging levels that can be used to signify log message importance.
 * <ul>
 * <li>{@link #FINEST}</li>
 * <li>{@link #FINER}</li>
 * <li>{@link #FINE}</li>
 * <li>{@link #CONFIG}</li>
 * <li>{@link #INFO}</li>
 * <li>{@link #WARNING}</li>
 * <li>{@link #SEVERE}</li>
 * </ul>
 * 
 * @author Doga Oruc
 * @since DoaEngine 2.6
 * @version 2.6
 * @see Logger
 * @see DoaLogger
 */
public enum LogLevel {

	/**
	 * Log level that should be used to log specialized developer info
	 */
	FINEST("\033[47;30;1m", "\033[40;37;1m"),

	/**
	 * Log level that should be used to log detailed developer info
	 */
	FINER("\033[47;30;1m", "\033[40;37;1m"),

	/**
	 * Log level that should be used to log general developer info
	 */
	FINE("\033[47;30;1m", "\033[40;37;1m"),

	/**
	 * Log level that should be used to log configuration info
	 */
	CONFIG("\033[42;30;1m", "\033[40;32;1m"),

	/**
	 * Log level that should be used to log general info
	 */
	INFO("\033[46;30;1m", "\033[40;36;1m"),

	/**
	 * Log level that should be used to log potential problems
	 */
	WARNING("\033[43;30;1m", "\033[40;33;1m"),

	/**
	 * Log level that should be used to log serious failures
	 */
	SEVERE("\033[41;30;1m", "\033[40;31;1m");

	private String prefixSequence;
	private String contentSequence;

	/**
	 * Constructor.
	 * 
	 * @param prefixSequence the coloring sequence for prefix
	 * @param contentSequence the coloring sequence for content
	 */
	private LogLevel(String prefixSequence, String contentSequence) {
		this.prefixSequence = prefixSequence;
		this.contentSequence = contentSequence;
	}

	/**
	 * For consoles that support ANSI escape sequences for coloring, returns the
	 * sequence for info prefix.
	 * 
	 * @return the coloring sequence for prefix
	 */
	public String getPrefixSequence() {
		// return prefixSequence;
		return contentSequence;
	}

	/**
	 * For consoles that support ANSI escape sequences for coloring, returns the
	 * sequence for info content.
	 * 
	 * @return the coloring sequence for content
	 */
	public String getContentSequence() {
		return contentSequence;
	}

	/* Foreground colors 30 Black 31 Red 32 Green 33 Yellow 34 Blue 35 Magenta 36
	 * Cyan 37 White Background colors 40 Black 41 Red 42 Green 43 Yellow 44 Blue 45
	 * Magenta 46 Cyan 47 White */
}