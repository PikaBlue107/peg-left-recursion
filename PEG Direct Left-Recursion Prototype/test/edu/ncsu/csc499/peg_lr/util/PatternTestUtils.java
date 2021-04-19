/**
 * 
 */
package edu.ncsu.csc499.peg_lr.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import org.junit.Assert;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.structure.InputContext;
import edu.ncsu.csc499.peg_lr.structure.Result;

/**
 * @author Melody
 *
 */
public class PatternTestUtils {

	/**
	 * Time (in milliseconds) after which the test runner will kill any matching
	 * threads seemingly stuck in an infinite loop.
	 */
	private static final int TIMEOUT = 10;

	private static final String PATH_EXAMPLES = "examples";

	/**
	 * Ensures that the provided pattern matches against the input string. Pattern
	 * must accept a prefix of the input string.
	 *
	 * @param p pattern to attempt to match
	 * @param s input string to use
	 * @deprecated Users should instead use
	 *             {@link PatternTestUtils.assertMatchesExact}.
	 */
	@Deprecated
	public static void assertMatchesPrefix(final Pattern p, final String s) {
		// null expected string indicates no expectation
		assertPatternAgainstExpected(p, s, null, true);
	}

	/**
	 * Ensures that the provided pattern matches against the input string. Pattern
	 * must accept exactly the String expected.
	 *
	 * @param p        pattern to attempt to match
	 * @param s        input string to use
	 * @param expected the expected match String
	 */
	public static void assertMatchesExact(final Pattern p, final String s, final String expected) {
		assertPatternAgainstExpected(p, s, expected, true);
	}

	/**
	 * Ensures that the provided pattern completely matches the input string.
	 * Pattern must accept exactly the input string and exhaust the input.
	 * 
	 * @param p pattern to attempt to match
	 * @param s input string to use
	 */
	public static void assertMatches(final Pattern p, final String s) {
		// Input string and expected match string are same
		assertPatternAgainstExpected(p, s, s, true);
	}

	/**
	 * Ensures that the provided pattern rejects the input string.
	 * 
	 * @param p pattern to attempt to match
	 * @param s input string to use
	 */
	public static void assertRejects(final Pattern p, final String s) {
		assertPatternAgainstExpected(p, s, null, false);
	}

	/**
	 * Runs an example match, printing the results to stdout and to a file.
	 *
	 * @param p pattern to attempt to match
	 * @param s input string to use
	 */
	public static void showExample(final Pattern p, final String s, final String caseName) {

		// Error checking on case name
		if ((caseName == null) || "".equals(caseName)) {
			throw new IllegalArgumentException("Example test needs to provide a valid caseName.");
		}

		// Run the main match
		final KillablePatternMatcher matcher = runCase(p, s);

		// Build a string to save
		final StringBuilder exampleOutput = new StringBuilder();

		// Save the case information
		exampleOutput.append("Example case:\n\t" + caseName + "\n");
		exampleOutput.append(matcher.scenario);

		// Save the output of the test
		exampleOutput.append("Match history: \n");
		exampleOutput.append(matcher.context.printHistory());
		exampleOutput.append("Result tree:" + "\n");
		exampleOutput.append(matcher.r.printResultTree() + "\n");
		exampleOutput.append("\n");

		// Print the output to stdout
		System.out.println(exampleOutput);

		// Write to a file
		// Calculate path
		final String folderPath = PATH_EXAMPLES + "/" + p.getType();
		final String filePath = folderPath + "/" + caseName + ".txt";
		// Ensure folder path is written
		final File destinationFolder = new File(folderPath);
		destinationFolder.mkdirs();
		// Create a file writer, and write it out.
		try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));) {
			writer.append(exampleOutput);
		} catch (final IOException e) {
			// If the file can't be written to, complain.
			e.printStackTrace();
		}
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
	private static void assertPatternAgainstExpected(final Pattern p, final String s, final String expected,
			final boolean expectedSuccess) {

		final KillablePatternMatcher matcher = runCase(p, s);

		// If we're here, we have a valid run! Check our conditions.

		// Snag an easy reference to the input string
		final StringBuilder scenario = matcher.scenario;

		// Add match expected success or failure
		matcher.scenario.append("\tPattern should: ").append((expectedSuccess) ? "accept" : "reject").append("\n")
				.append("\tExpected match: [").append(expected).append("]\n");

		// Expect success or failure
		if (expectedSuccess) {
			// Ensure positive match
			Assert.assertTrue(scenario + "Failure: Match did not succeed.", matcher.r.isSuccess());
			// Ensure the match is a prefix of the original string
			Assert.assertTrue(scenario + "Failure: Match was greater than input in length.",
					matcher.r.getData().length() <= s.length());
			Assert.assertEquals(scenario + "Failure: Match was not a prefix of input.",
					s.substring(0, matcher.r.getData().length()), matcher.r.getData());
			// Ensure match's type matches pattern's type
			Assert.assertEquals(scenario + "Failure: Match Result's type did not match Pattern's type.", p.getType(),
					matcher.r.getType());
		} else {
			// Ensure no match
			Assert.assertFalse(scenario + "Failure: Match succeeded when it shouldn't have.", matcher.r.isSuccess());
			// Ensure context was properly reset
			Assert.assertEquals(scenario + "Failure: Failed match did not reset to original position.", 0,
					matcher.context.getPosition());
		}

		// If we have a specific match we're expecting, check it
		if (expected != null) {
			Assert.assertEquals(scenario + "Failure: Result match string did not match expected match string.",
					expected, matcher.r.getData());

			// If the expected string is the same length as the input, the input should be
			// exhausted
			if (expected.length() == s.length()) {
				Assert.assertTrue(scenario + "Failure: Input string was not exhausted.", matcher.context.isAtEnd());
			}
		}

		// Ensure that the context growing map was cleared out all the way
		for (int i = 0; i <= matcher.context.length(); i++) {
			Assert.assertEquals(0, matcher.context.getResultCount(i));
		}
	}

	/**
	 * Runs a test case, constructing a KillablePatternMatcher and allowing it to
	 * execute. If the matcher gets stuck in a loop, fails. If the matcher dies from
	 * an exception, fails. Otherwise, update the matcher with the result of the
	 * request and return the finished matcher.
	 *
	 * @param p the pattern to use for matching
	 * @param s the String to attempt to match
	 * @return the finished PatternMatcher thread with the results of the case.
	 */
	@SuppressWarnings("deprecation") // we don't have any other way to forcefully kill a thread that is stuck in an
	// infinite loop
	private static KillablePatternMatcher runCase(final Pattern p, final String s) {
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
			Assert.fail(matcher.scenario + "Test timed out after " + TIMEOUT + " miliseconds.");
		}

		// If the exception knows its cause of death, then print that exception.
		if (matcher.causeOfDeath != null) {

			// If it died because of an Exception or other natural cause, let the user know.
			System.out.println("The matcher thread for the following test case was killed by the above exception.");
			System.out.print(matcher.scenario);
//			matcher.causeOfDeath.printStackTrace();
			System.out.println();

			// Re-throw the exception for that lovely red X.
			throw matcher.causeOfDeath;
		}

		// Add the result of the match
		matcher.scenario.append("Test result:\n\t");
		if (matcher.r.isSuccess()) {
			matcher.scenario.append("Matched [" + matcher.r.getData() + "]");
		} else {
			matcher.scenario.append("Rejected");
		}
		matcher.scenario.append("\n");

		return matcher;
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
		/** String representing the case that this matcher was run on. */
		StringBuilder scenario;

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

			scenario = new StringBuilder();
			scenario.append("Test scenario:\n").append("\tInput: [").append(s).append("]\n").append("\tPattern type: ")
					.append(p.getType()).append("\n").append("\tPattern definition: ").append(p.getDefinition())
					.append("\n");
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
