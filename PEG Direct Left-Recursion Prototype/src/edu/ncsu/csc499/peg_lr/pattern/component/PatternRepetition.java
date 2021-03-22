/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.component;

import edu.ncsu.csc499.peg_lr.event.pattern.RepetitionEvent;
import edu.ncsu.csc499.peg_lr.event.pattern.PatternEvent.PatternEventType;
import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.structure.InputContext;
import edu.ncsu.csc499.peg_lr.structure.Result;

/**
 * @author Melody Griesen
 *
 */
public class PatternRepetition extends PatternComponent {

	/** Minimum number of times that the pattern must be present to match. */
	private final int lowerBound;
	/** Maximum number of times that the pattern will match. If -1, no max limit. */
	private final int upperBound;
	/** Pattern to repeat. */
	private final Pattern pattern;

	/**
	 * A PatternRepetition must be constructed with a given pattern to repeat, as
	 * well as the lower and upper bounds for the repetition.
	 * 
	 * @param pattern    the pattern to match
	 * @param lowerBound the minimum number of times that the pattern must match
	 * @param upperBound the maximum number of times that the pattern may match
	 */
	public PatternRepetition(final Pattern pattern, final int lowerBound, final int upperBound) {
		if (pattern == null) {
			throw new IllegalArgumentException("Pattern for Repetition cannot be null!");
		}
		if (lowerBound < 0) {
			throw new IllegalArgumentException("Lower bound for Repetition cannot be negative!");
		}
		if (upperBound < -1) {
			throw new IllegalArgumentException("Upper bound for Repetition cannot be below -1!");
		}
		this.pattern = pattern;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	/**
	 * Matches a sequence of instances of the Pattern, with upper and lower bounds
	 * specified by fields.
	 */
	@Override
	protected Result match(final InputContext context) {
		// Create an overall result to track starting position
		final Result repetition = new Result(true, "", context.getPosition());
		// Track the number of successful iterations
		int matches = 0;

		// Begin iterating over the context
		while (matches != upperBound) {

			// Log attempt to match
			context.addHistory(new RepetitionEvent(context, pattern, matches));

			// Attempt to match at this iteration
			final Result result = pattern.lazyMatch(context);

			// If we succeeded in matching
			if (result.isSuccess()) {
				// Log that we expanded this repetition
				context.addHistory(new RepetitionEvent(context, pattern, matches, repetition, PatternEventType.EXPAND));
				// Increment our match count
				matches++;
				// Add as a child of our matches
				repetition.addChild(result);
			}
			// Otherwise, we failed in matching
			else {
				// Fail this repeition
				context.addHistory(new RepetitionEvent(context, pattern, matches, repetition, PatternEventType.REJECT));

				// Did we meet the minimum count?
				if (matches >= lowerBound) {
					// If yes, return a success
					return repetition;
				}
				// We did not meet the minimum count.
				else {
					// Reset the context
					context.setPosition(repetition.getStartIdx());
					// Return a failure.
					return Result.FAIL(context.getPosition());
				}
			}
		}

		// We exited matching because we reached our maximum number of matches.
		// Log this to the history
		context.addHistory(new RepetitionEvent(context, pattern, matches - 1, repetition, PatternEventType.LIMIT));

		// Success!
		return repetition;
	}

	/**
	 * A PatternRepetition is declared unique by all of its fields - Pattern, lower
	 * bound, and upper bound.
	 * 
	 * @return a unique hashCode for this PatternRepetition
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + lowerBound;
		result = (prime * result) + ((pattern == null) ? 0 : pattern.hashCode());
		result = (prime * result) + upperBound;
		return result;
	}

	/**
	 * Declares that PatternRepetitions are equal if their pattern, lower bound, and
	 * upper bound match.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PatternRepetition other = (PatternRepetition) obj;
		if (lowerBound != other.lowerBound) {
			return false;
		}
		if (pattern == null) {
			if (other.pattern != null) {
				return false;
			}
		} else if (!pattern.equals(other.pattern)) {
			return false;
		}
		if (upperBound != other.upperBound) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc} Surrounds repeated pattern in token boundaries, and appends a
	 * repetition indicator (one of '+', '+', '*', or a form of {n,m})
	 */
	@Override
	public String getDefinition(final boolean component) {

		// Use string builder for definition
		final StringBuilder definition = new StringBuilder();

		// Start by encapsulating repeated pattern in token boundary parenthesis
		definition.append("(");
		definition.append(pattern.getDefinition(true));
		definition.append(")");

		// Detect cases for repetition indicator
		// {0,1} := ?
		if ((lowerBound == 0) && (upperBound == 1)) {
			definition.append("?");
		}
		// {0,inf} := *
		else if ((lowerBound == 0) && (upperBound == -1)) {
			definition.append("*");
		}
		// {1,inf} := +
		else if ((lowerBound == 1) && (upperBound == -1)) {
			definition.append("+");
		}
		// some form of {n,m}
		else {
			// Always start with bracket
			definition.append("{");

			// If the lower bound is 0, skip it; otherwise, print it
			if (lowerBound != 0) {
				definition.append(lowerBound);
			}

			// If bounds don't match
			if (lowerBound != upperBound) {
				// Add comma
				definition.append(",");
				// If upper bound isn't inf, add
				if (upperBound != -1) {
					definition.append(upperBound);
				}
			}

			// Always end with bracket
			definition.append("}");
		}

		// Provide finished result
		return definition.toString();

	}

}
