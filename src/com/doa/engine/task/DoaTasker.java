package com.doa.engine.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.doa.utils.DoaUtils;

/**
 * Abstracts threading and all it's related functionality from the common {@code DoaEngine} user.
 * This class' methods are not a must to use. If required, any user of {@code DoaEngine} can bring
 * their own threading libraries. This class exists purely to provide a sufficient enough tool to
 * write decent programs.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 2.7
 */
public final class DoaTasker {

	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

	private DoaTasker() {}

	/**
	 * Causes a separate thread to guard the execution of a code segment. When a code segment is being
	 * guarded by a {@code DoaTaskGuard}, the segment can only be executed once every waitTime
	 * milliseconds.
	 *
	 * @param guard any task guard that was true before this method's invocation
	 * @param waitTime the amount of delay in milliseconds for the guard to stop guarding
	 * @see java.lang.Thread
	 */
	public static void guard(final DoaTaskGuard guard, final long waitTime) {
		guard.set(false);
		EXECUTOR.execute(() -> {
			DoaUtils.sleepFor(waitTime);
			guard.set(true);
		});
	}

	/**
	 * Causes a separate thread to guard the execution of a {@code Runnable}. When a {@code Runnable} is
	 * being guarded by a {@code DoaTaskGuard}, the {@code Runnable} can only be executed once every
	 * waitTime milliseconds.
	 *
	 * @param task the runnable which contains the instructions that should be executed on a separate
	 *        thread
	 * @param guard any task guard that was true before this method's invocation
	 * @param waitTime the amount of delay in milliseconds for the guard to stop guarding
	 * @see java.lang.Thread
	 */
	public static void guardExecution(final Runnable task, final DoaTaskGuard guard, final long waitTime) {
		if (guard.get()) {
			guard.set(false);
			EXECUTOR.execute(() -> {
				task.run();
				DoaUtils.sleepFor(waitTime);
				guard.set(true);
			});
		}
	}

	/**
	 * Causes a separate thread to execute the argument immediately.
	 *
	 * @param task the runnable which contains the instructions that should be executed on a separate
	 *        thread
	 * @see java.lang.Thread
	 */
	public static void executeNow(final Runnable task) {
		EXECUTOR.execute(task);
	}

	/**
	 * Causes a separate thread to execute the arguments.
	 *
	 * @param task the runnable which contains the instructions that should be executed on a separate
	 *        thread
	 * @param after the runnable which contains the instructions that should be executed after a certain
	 *        delay
	 * @param waitTime the amount of delay in milliseconds between the two tasks
	 * @see java.lang.Thread
	 */
	public static void executeAndWait(final Runnable task, final Runnable after, final long waitTime) {
		EXECUTOR.execute(() -> {
			task.run();
			DoaUtils.sleepFor(waitTime);
			after.run();
		});
	}

	/**
	 * Causes a separate thread to execute the argument after a certain delay.
	 *
	 * @param task the runnable which contains the instructions that should be executed after a certain
	 *        delay
	 * @param waitTime the amount of delay in milliseconds
	 * @see java.lang.Thread
	 */
	public static void executeLater(final Runnable task, final long waitTime) {
		EXECUTOR.execute(() -> {
			DoaUtils.sleepFor(waitTime);
			task.run();
		});
	}
}