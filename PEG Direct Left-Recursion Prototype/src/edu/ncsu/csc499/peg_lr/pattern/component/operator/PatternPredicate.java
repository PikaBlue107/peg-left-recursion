/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.component.operator;

import java.util.Iterator;
import java.util.List;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternComponent;
import edu.ncsu.csc499.peg_lr.structure.InputContext;
import edu.ncsu.csc499.peg_lr.structure.Result;

/**
 * @author Melody Griesen
 *
 */
public class PatternPredicate extends PatternComponent {

	/** Pattern to test for predication. */
	private final Pattern pattern;

	/**
	 * Whether the predicate should expect the predicate pattern to match or reject.
	 */
	private final boolean expectSuccess;

	/**
	 * A PatternPredicate must be constructed with a given pattern to test.
	 * 
	 * @param pattern the pattern to match
	 */
	public PatternPredicate(final Pattern pattern, final boolean expectSuccess) {
		if (pattern == null) {
			throw new IllegalArgumentException("Pattern for Repetition cannot be null!");
		}
		this.pattern = pattern;
		this.expectSuccess = expectSuccess;
	}

	/**
	 * Accepts if the pattern has the same match status against context as the
	 * expected success variable. Resets the InputContext to its original position
	 * regardless of result.
	 * 
	 * @param context the current context of the match
	 */
	@Override
	protected Result match(final InputContext context) {

		// Create a Result for the predication
		final Result pred = new Result(context.getPosition());

		// Save the start position
		final int startPos = context.getPosition();

		// Try matching the pattern
		final Result result = pattern.lazyMatch(context);

		// If it matches the expected success
		if (expectSuccess == result.isSuccess()) {
			// Success!
			pred.addChild(result);
			// Okay, but undo adding the data and the end index.
			pred.setData("");
			pred.setEndIdx(pred.getStartIdx());
		} else {
			// Failure.
			pred.setSuccess(false);
		}

		// Reset InputContext
		context.setPosition(startPos);

		// Return result
		return pred;
	}

	/**
	 * {@inheritDoc} Prepends > or ! based on expected success.
	 */
	@Override
	public String getDefinition(final boolean component) {
		return (expectSuccess ? ">" : "!") + pattern.getDefinition(true);
	}

	/**
	 * {@inheritDoc} Returns the predicate's match pattern.
	 */
	@Override
	public List<Pattern> getPatternComponents() {
		return List.of(pattern);
	}

	/**
	 * {@inheritDoc} Returns the predicate's match pattern.
	 */
	@Override
	protected Iterator<Pattern> getPossibleLeftmostComponents() {
		return List.of(pattern).iterator();
	}

	/**
	 * {@inheritDoc} Always returns true, as a predicate does not consume any input.
	 */
	@Override
	public boolean isNullable() {
		return true;
	}

}
