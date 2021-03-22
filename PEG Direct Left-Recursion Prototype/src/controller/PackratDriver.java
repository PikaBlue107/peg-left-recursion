package controller;

import pattern.Pattern;
import pattern.definition.DefinedExpression;
import structure.InputContext;
import structure.Result;

@SuppressWarnings("unused")
/**
 * Main launch point for the program
 * 
 * @author Melody Griesen
 *
 */
public class PackratDriver {

	/** Test string to use for each run. */
	private static final String TEST_STRING = "1+25+7";

	/** Pattern used to match the test string. */
	private static final Pattern MATCH_PATTERN = new DefinedExpression();

	/**
	 * Main launch point of the program. Sandbox. Playground. Laboratory.
	 * 
	 * @param args command-line arguments, unused.
	 */
	public static void main(final String[] args) {
//		doMatch(TEST_STRING, MATCH_PATTERN);
		System.out.println(InputContext.CHAR_EPSILON);
	}

	/**
	 * Do the specified match
	 *
	 * @param inputString the string to match
	 * @param matcher     the pattern to match with
	 */
	public static void doMatch(final String inputString, final Pattern matcher) {

		// Create the Input context
		final InputContext input = new InputContext(inputString);

		// Attempt to match
		final Result result = matcher.lazyMatch(input);

		// Print out the result
		System.out.println(result.toString());

		System.out.println("Hidden tree:");
		System.out.println(result.printResultTree());
		System.out.println("\n\n\n\n\n");

		System.out.println("Full tree:");
		System.out.println(result.printResultTree(true));
		System.out.println("\n\n\n\n\n");
	}
}
