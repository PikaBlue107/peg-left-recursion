/**
 * 
 */
package util;

import java.lang.management.ManagementFactory;

import org.junit.Assert;

import patterns.general.Pattern;
import structure.InputContext;
import structure.Result;

/**
 * @author Melody
 *
 */
public class PatternTestUtils {

	/**
	 * Time (in miliseconds) after which the test runner will kill any matching
	 * threads seemingly stuck in an infinite loop.
	 */
	private static final int TIMEOUT = 10;

	/**
	 * Ensures that the provided pattern matches against the input string. Pattern
	 * must accept a prefix of the input string.
	 *
	 * @param p pattern to attempt to match
	 * @param s input string to use
	 */
	public static void assertMatchesPrefix(final Pattern p, final String s) {
		assertPatternAgainstExpected(p, s, true, false);
	}

	/**
	 * Ensures that the provided pattern completely matches the input string.
	 * Pattern must accept exactly the input string and exhaust the input.
	 * 
	 * @param p pattern to attempt to match
	 * @param s input string to use
	 */
	public static void assertMatches(final Pattern p, final String s) {
		assertPatternAgainstExpected(p, s, true, true);
	}

	/**
	 * Ensures that the provided pattern rejects the input string.
	 * 
	 * @param p pattern to attempt to match
	 * @param s input string to use
	 */
	public static void assertRejects(final Pattern p, final String s) {
		assertPatternAgainstExpected(p, s, false, false);
	}

	/**
	 * Hack-y method to check if we're running in the debugger. Used to only kill
	 * matching threads if we're *not* in the Debugger.
	 * 
	 * @return true if we're debugging, false if not
	 */
	static boolean isDebug() {
		for (final String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
			if (arg.contains("jdwp=")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * General method to test the results of some Pattern against some input String.
	 * 
	 * If expectedSuccess is true, pattern must accept and match some prefix of the
	 * input string. Otherwise if expectedSuccess is false, pattern must simply
	 * reject.
	 * 
	 * If requireFullMatch is true, result must exactly match input string.
	 * 
	 * @param p                pattern to attempt to match
	 * @param s                input string to use
	 * @param expectedSuccess  whether the match should succeed or fail
	 * @param requireFullMatch whether the result should exactly match the input
	 *                         string
	 */
	@SuppressWarnings("deprecation") // we don't have any other way to forcefully kill a thread that is stuck in an
										// infinite loop
	private static void assertPatternAgainstExpected(final Pattern p, final String s, final boolean expectedSuccess,
			final boolean requireFullMatch) {

		final StringBuilder inputStringBuilder = new StringBuilder();
		inputStringBuilder.append("Test case:\n").append("\tInput: [").append(s).append("]\n").append("\tPattern: ")
				.append(p.toString()).append("\n").append("\tPattern should: ")
				.append((expectedSuccess) ? "accept" : "reject").append("\n").append("\tExpecting full match: ")
				.append(requireFullMatch).append("\n");
		String inputString = inputStringBuilder.toString();

		// Create match thread to protect ourselves against infinite loops
		final KillablePatternMatcher matcher = new KillablePatternMatcher(s, p);
		try {
			// Start the thread and give it a chance to finish
			synchronized (matcher) {
				matcher.start();
				if (isDebug()) {
					matcher.wait();
				} else {
					matcher.wait(TIMEOUT);
				}
			}
		} catch (final InterruptedException e) {
			// Shouldn't ever happen
			e.printStackTrace();
		}

		// If it's stuck in an infinite loop, then fail by that reason.
		if (matcher.isAlive() && !matcher.workDone) {
			matcher.stop();
			Assert.fail(inputString + "Test timed out after " + TIMEOUT + " miliseconds.");
		}

		// If the exception knows its cause of death, then print that exception.
		if (matcher.causeOfDeath != null) {

			// If it died because of an Exception or other natural cause, fail by that
			// reason instead.
			System.out.println(
					"The above exception caused the matcher thread to fail for the test with the following conditions.\n"
							+ inputString);
//			Assert.fail(inputString + "The given test case killed the matching thread with an unhandled exception.");
			throw matcher.causeOfDeath; //
		}

		inputString += "\tMatch: [" + matcher.r.getData() + "]\n";

		// If we're here, we have a valid run! Check our conditions.

		// Expect success or failure
		if (expectedSuccess) {
			// Ensure positive match
			Assert.assertTrue(inputString + "Match should succeed.", matcher.r.isSuccess());
			// Ensure the match is a prefix of the original string
			Assert.assertTrue(inputString + "Match should be less or equal to input in length.",
					matcher.r.getData().length() <= s.length());
			Assert.assertEquals(inputString + "Match should be a prefix of input.",
					s.substring(0, matcher.r.getData().length()), matcher.r.getData());
			// Ensure match's type matches pattern's type
			Assert.assertEquals(inputString + "Match Result's type should match Pattern's type.", p.getType(),
					matcher.r.getType());
		} else {
			// Ensure no match
			Assert.assertFalse(inputString + "Match should not succeed.", matcher.r.isSuccess());
			// Ensure context was properly reset
			Assert.assertEquals(inputString + "Failed match should reset to original position.", 0,
					matcher.context.getPosition());
		}

		// If we need a full match, ensure the context is at the end and the strings
		// match exactly
		if (requireFullMatch) {
			Assert.assertTrue(inputString + "Input string should be exhausted.", matcher.context.isAtEnd());
			Assert.assertEquals(inputString + "Result data should match input.", s, matcher.r.getData());
		}
	}

	/**
	 * Attempts to match a pattern against an input string. If the match results in
	 * an infinite loop, the thread can be killed to allow a test suite to fail the
	 * test and continue running.
	 * 
	 * @author Melody
	 *
	 */
	private static class KillablePatternMatcher extends Thread {
		/** The context used when matching the string. */
		InputContext context;
		/** The Pattern used to match the input. */
		Pattern p;
		/** The Result of matching the pattern. */
		Result r;
		/** Stores any Exceptions that caused this matching thread's death. */
		RuntimeException causeOfDeath;

		boolean workDone = false;

		/**
		 * Constructs this matcher thread by providing the input string and pattern to
		 * match.
		 *
		 * @param p pattern to attempt to match
		 * @param s input string to use
		 */
		public KillablePatternMatcher(final String s, final Pattern p) {
			context = new InputContext(s);
			this.p = p;
		}

		/**
		 * Attempts to match the pattern onto the input string. Saves the result for
		 * later inspection.
		 * 
		 * When done, sets workDone to true and calls notifyAll() on this thread,
		 * signaling any waiting threads that its task is complete.
		 */
		@Override
		public void run() {
			try {
				r = p.lazyMatch(context);
				workDone = true;
				synchronized (this) {
					this.notifyAll();
				}
			} catch (final RuntimeException e) {
				causeOfDeath = e;
				// Re-throw the exception so that we get that lovely stderr
				throw e;
			}
		}
	}

}
