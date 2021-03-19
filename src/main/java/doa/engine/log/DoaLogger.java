package doa.engine.log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * Implementation of Logger.
 *
 * @author Doga Oruc
 * @since DoaEngine 2.6.1
 * @version 3.0
 * @see Logger
 * @see LogLevel
 */
public final class DoaLogger implements Logger {

	public static final DoaLogger LOGGER = getInstance();

	private static final String DATE_SEQUENCE = "\033[40;37;1m";
	private static final String CLEAR_SEQUENCE = "\033[0m";

	private static DoaLogger _this;

	private final DateFormat dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private LogLevel level = LogLevel.INFO;

	private PrintStream console = System.out;
	private PrintWriter writer;

	/**
	 * Constructor.
	 */
	private DoaLogger() {}

	public static DoaLogger getInstance() { return _this == null ? _this = new DoaLogger() : _this; }

	public LogLevel getLevel() { return level; }

	@Override
	public void setLevel(@NotNull final LogLevel level) { this.level = level; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTarget(final String path) throws IOException {
		writer = null;
		if (path == null) { return; }
		writer = new PrintWriter(new FileWriter(path));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(@NotNull final LogLevel level, final boolean message) { log(level, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(@NotNull final LogLevel level, final char message) { log(level, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(@NotNull final LogLevel level, @NotNull final char[] message) {
		log(level, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(@NotNull final LogLevel level, final double message) { log(level, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(@NotNull final LogLevel level, final float message) { log(level, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(@NotNull final LogLevel level, final int message) { log(level, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(@NotNull final LogLevel level, final long message) { log(level, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(@NotNull final LogLevel level, @NotNull final Object message) {
		log(level, String.valueOf(message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(@NotNull final LogLevel level, @NotNull final String message) {
		if (this.level.compareTo(level) < 0) { return; }
		final String time = dt.format(Calendar.getInstance().getTime());
		final String prefix = new StringBuilder(32).append(" [")
			.append(level.toString())
			.append("]")
			.append(level.getExtraSpaceCharacters())
			.toString();
		console.print(DATE_SEQUENCE);
		console.print(time);
		console.print(level.getColorSequence());
		console.print(prefix
			+ " "
			+ message);
		if (Objects.nonNull(writer)) {
			writer.print(time
				+ prefix
				+ " "
				+ message);
		}
		newLine();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(final boolean message) { log(LogLevel.FINEST, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(final char message) { log(LogLevel.FINEST, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(@NotNull final char[] message) { log(LogLevel.FINEST, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(final double message) { log(LogLevel.FINEST, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(final float message) { log(LogLevel.FINEST, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(final int message) { log(LogLevel.FINEST, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(final long message) { log(LogLevel.FINEST, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(@NotNull final Object message) { log(LogLevel.FINEST, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finest(@NotNull final String message) { log(LogLevel.FINEST, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(final boolean message) { log(LogLevel.FINER, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(final char message) { log(LogLevel.FINER, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(@NotNull final char[] message) { log(LogLevel.FINER, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(final double message) { log(LogLevel.FINER, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(final float message) { log(LogLevel.FINER, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(final int message) { log(LogLevel.FINER, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(final long message) { log(LogLevel.FINER, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(@NotNull final Object message) { log(LogLevel.FINER, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finer(@NotNull final String message) { log(LogLevel.FINER, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(final boolean message) { log(LogLevel.FINE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(final char message) { log(LogLevel.FINE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(@NotNull final char[] message) { log(LogLevel.FINE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(final double message) { log(LogLevel.FINE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(final float message) { log(LogLevel.FINE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(final int message) { log(LogLevel.FINE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(final long message) { log(LogLevel.FINE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(@NotNull final Object message) { log(LogLevel.FINE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fine(@NotNull final String message) { log(LogLevel.FINE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(final boolean message) { log(LogLevel.CONFIG, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(final char message) { log(LogLevel.CONFIG, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(@NotNull final char[] message) { log(LogLevel.CONFIG, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(final double message) { log(LogLevel.CONFIG, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(final float message) { log(LogLevel.CONFIG, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(final int message) { log(LogLevel.CONFIG, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(final long message) { log(LogLevel.CONFIG, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(@NotNull final Object message) { log(LogLevel.CONFIG, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(@NotNull final String message) { log(LogLevel.CONFIG, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final boolean message) { log(LogLevel.INFO, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final char message) { log(LogLevel.INFO, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(@NotNull final char[] message) { log(LogLevel.INFO, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final double message) { log(LogLevel.INFO, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final float message) { log(LogLevel.INFO, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final int message) { log(LogLevel.INFO, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final long message) { log(LogLevel.INFO, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(@NotNull final Object message) { log(LogLevel.INFO, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(@NotNull final String message) { log(LogLevel.INFO, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(final boolean message) { log(LogLevel.WARNING, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(final char message) { log(LogLevel.WARNING, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(@NotNull final char[] message) { log(LogLevel.WARNING, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(final double message) { log(LogLevel.WARNING, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(final float message) { log(LogLevel.WARNING, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(final int message) { log(LogLevel.WARNING, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(final long message) { log(LogLevel.WARNING, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(@NotNull final Object message) { log(LogLevel.WARNING, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(@NotNull final String message) { log(LogLevel.WARNING, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(final boolean message) { log(LogLevel.SEVERE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(final char message) { log(LogLevel.SEVERE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(@NotNull final char[] message) { log(LogLevel.SEVERE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(final double message) { log(LogLevel.SEVERE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(final float message) { log(LogLevel.SEVERE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(final int message) { log(LogLevel.SEVERE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(final long message) { log(LogLevel.SEVERE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(@NotNull final Object message) { log(LogLevel.SEVERE, String.valueOf(message)); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void severe(@NotNull final String message) { log(LogLevel.SEVERE, String.valueOf(message)); }

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
