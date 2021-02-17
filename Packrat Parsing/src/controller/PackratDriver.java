package controller;

import patterns.definition.*;
import patterns.general.*;
import structure.Derivation;
import structure.Result;

public class PackratDriver {

	/** Test string to use for each run. */
	private static final String TEST_STRING = "1+2";

	/**
	 * Main launch point of the program. Sandbox. Playground. Laboratory.
	 * 
	 * @param args command-line arguments, unused.
	 */
	public static void main(String[] args) {

		// Create the Derivations
		Derivation base = new Derivation(TEST_STRING);

		// Create the pattern
		
		Pattern matcher = new SimpleExpression();

		// Attempt to match
		Result<?> result = matcher.lazyMatch(base);

		// Print out the result
		System.out.println(result.toString());
		System.out.println(result.printResultTree());

	}
}
