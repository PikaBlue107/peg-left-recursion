package controller;

import patterns.definition.SimpleExpression;
import patterns.general.Pattern;
import structure.InputContext;
import structure.Result;

public class PackratDriver {

	/** Test string to use for each run. */
	private static final String TEST_STRING = "1+2";

	/**
	 * Main launch point of the program. Sandbox. Playground. Laboratory.
	 * 
	 * @param args command-line arguments, unused.
	 */
	public static void main(final String[] args) {

		// Create the Derivations
		final InputContext input = new InputContext(TEST_STRING);

		// Create the pattern
		final Pattern matcher = new SimpleExpression();

		// Attempt to match
		final Result result = matcher.lazyMatch(input);

		// Print out the result
		System.out.println(result.toString());
		System.out.println(result.printResultTree());

	}
}
