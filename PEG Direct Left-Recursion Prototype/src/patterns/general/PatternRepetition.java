/**
 * 
 */
package patterns.general;

import structure.InputContext;
import structure.Result;

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

			// Attempt to match at this iteration
			final Result result = pattern.lazyMatch(context);

			// If we succeeded in matching
			if (result.isSuccess()) {
				// Increment our match count
				matches++;
				// Add as a child of our matches
				repetition.addChild(result);
			}
			// Otherwise, we failed in matching
			else {
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
					return Result.FAIL();
				}
			}
		}

		// We exited matching because we reached our maximum number of matches. Success!
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

}
