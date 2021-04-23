package edu.ncsu.csc499.peg_lr.controller;

import edu.ncsu.csc499.peg_lr.event.ParseEvent;
import edu.ncsu.csc499.peg_lr.event.pattern.PatternMatchEvent;
import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternString;
import edu.ncsu.csc499.peg_lr.pattern.component.operator.PatternChoice;
import edu.ncsu.csc499.peg_lr.pattern.component.operator.PatternSequence;
import edu.ncsu.csc499.peg_lr.pattern.definition.DefinedNumber;
import edu.ncsu.csc499.peg_lr.pattern.definition.DefinedPattern;
import edu.ncsu.csc499.peg_lr.structure.InputContext;
import edu.ncsu.csc499.peg_lr.structure.Result;

@SuppressWarnings("unused")
/**
 * Main launch point for the program
 * 
 * @author Melody Griesen
 *
 */
public class PackratDriver {

	/** Test string to use for each run. */
	private static final String TEST_STRING = "(((1)+23))";

	private static Pattern plus = new PatternString("+");
	private static Pattern number = new DefinedNumber();

	/** Pattern used to match the test string. */
	private static final Pattern MATCH_PATTERN = new DefinedPattern("Expression") {

		private final Pattern pattern = new PatternChoice(new PatternSequence(this, plus, number),
				new PatternSequence(new PatternString("("), this, new PatternString(")")), number);
		{
			super.setDefinition(pattern);
		}
	};

	/**
	 * Main launch point of the program. Sandbox. Playground. Laboratory.
	 * 
	 * @param args command-line arguments, unused.
	 */
	public static void main(final String[] args) {
		doMatch(TEST_STRING, MATCH_PATTERN);
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
		System.out.println("Definition: " + matcher.getDefinition());
//
//		System.out.println("Hidden tree:");
//		System.out.println(result.printResultTree());
//		System.out.println("\n\n\n\n\n");

		System.out.println("Full tree:");
		System.out.println(result.printResultTree(true));
		System.out.println("\n\n\n\n\n");

		// Print out just the history events having to do with the growing map
		int historyIdx = 0;
		for (final ParseEvent e : input.getHistory(PatternMatchEvent.class)) {
			System.out.printf("%4d:\t%s\n", historyIdx++, e.toString());
		}
	}
}
