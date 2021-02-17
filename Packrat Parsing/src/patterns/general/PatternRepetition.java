/**
 * 
 */
package patterns.general;

import structure.Derivation;
import structure.Result;

/**
 * @author Melody Griesen
 *
 */
public class PatternRepetition extends Pattern {

	/** Minimum number of times that the pattern must be present to match. */
	private int lowerBound;
	/** Maximum number of times that the pattern will match. If -1, no max limit. */
	private int upperBound;
	/** Pattern to repeat. */
	private Pattern pattern;

	/**
	 * A PatternRepetition must be constructed with a given pattern to repeat, as
	 * well as the lower and upper bounds for the repetition.
	 * 
	 * @param pattern the pattern to match
	 * @param lowerBound the minimum number of times that the pattern must match
	 * @param upperBound the maximum number of times that the pattern may match
	 */
	public PatternRepetition(Pattern pattern, int lowerBound, int upperBound) {
		this.pattern = pattern;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	/**
	 * Matches a sequence of instances of the Pattern, with upper and lower bounds specified by fields.
	 */
	@Override
	protected Result match(Derivation derivation) {
		// Track the previous result so that we know where to start each loop from and what to return if we fail
		Result previousResult = new Result(true, null, derivation);
		// Track the number of successful iterations
		int matches = 0;
		// Begin iterating over the Derivation
		while(matches != upperBound) {
			// Attempt to match at this iteration
			Result result = pattern.lazyMatch(previousResult.getDerivation());
			
			// If we succeeded in matching
			if(result.isSuccess()) {
				// Increment our match count
				matches++;
				// Save as previous result
				previousResult = result;
			}
			// Otherwise, we failed in matching
			else {
				// Did we meet the minimum count?
				if(matches >= lowerBound) {
					// If yes, return a success
					return previousResult;
				}
				// We did not meet the minimum count.
				else {
					// Return a failure.
					return Result.FAIL();
				}
			}
		}
		
		// We exited matching because we reached our maximum number of matches. Success!
		return previousResult;
	}

	/**
	 * A PatternRepetition is declared unique by all of its fields - Pattern, lower bound, and upper bound.
	 * 
	 * @return a unique hashCode for this PatternRepetition
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + lowerBound;
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		result = prime * result + upperBound;
		return result;
	}

	/**
	 * Declares that PatternRepetitions are equal if their pattern, lower bound, and upper bound match.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PatternRepetition other = (PatternRepetition) obj;
		if (lowerBound != other.lowerBound)
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		if (upperBound != other.upperBound)
			return false;
		return true;
	}
	
	

}
