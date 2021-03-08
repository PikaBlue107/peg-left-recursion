package controller;

import patterns.definition.DefinitionExpression;
import patterns.general.Pattern;
import structure.InputContext;
import structure.Result;

public class PackratDriver {

	/** Test string to use for each run. */
	private static final String TEST_STRING = "1+25+7";

	/**
	 * Main launch point of the program. Sandbox. Playground. Laboratory.
	 * 
	 * @param args command-line arguments, unused.
	 */
	public static void main(final String[] args) {

		// Create the Input context
		final InputContext input = new InputContext(TEST_STRING);

		// Create the pattern
		final Pattern matcher = new DefinitionExpression();

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
