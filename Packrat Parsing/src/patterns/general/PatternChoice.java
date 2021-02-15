/**
 * 
 */
package patterns.general;

import java.util.ArrayList;
import java.util.List;

import structure.Derivation;
import structure.Result;

/**
 * @author Melody Griesen
 *
 */
public class PatternChoice extends Pattern {

	/** List of patterns to match in ordered choice alternatives. */
	private List<Pattern> patterns;

	{
		// Initialize the List
		patterns = new ArrayList<>();
	}

	/**
	 * Constructs a PatternChoice from all patterns provided. This pattern will be
	 * matched by an ordered choice of all patterns provided.
	 * 
	 * @param patterns the set of possible patterns to choose from
	 */
	public PatternChoice(Pattern... patterns) {
		// For each Pattern in the list, add it to the ArrayList
		for (Pattern p : patterns)
			this.patterns.add(p);
	}

	/**
	 * Constructs a PatternChoice from all patterns provided. This pattern will be
	 * matched by an ordered choice of all patterns provided.
	 * 
	 * @param patterns the set of possible patterns to choose from
	 */
	public PatternChoice(List<Pattern> patterns) {
		this.patterns.addAll(patterns);
	}

	/**
	 * Chain method of adding patterns in subsequent method calls. Adds an extra
	 * pattern choice to consider if all previous patterns fail.
	 * 
	 * @param toAdd the new pattern to consider after all previous patterns have
	 *              failed
	 * @return this PatternChoice object for multiple chainings of this method
	 */
	public PatternChoice add(Pattern toAdd) {
		// Add the new pattern to the end of the list
		patterns.add(toAdd);
		// Return this object for method chaining
		return this;
	}

	/**
	 * Matches an ordered choice of Patterns by initially assuming a non-match with
	 * the given derivation then trying each Pattern in the list until one matches.
	 * Only returns a positive Result if at least one Pattern in the list matches.
	 * 
	 * @param derivation the input to start on
	 * @return a positive match after any Pattern succeeds, a negative match if none
	 *         of them do
	 */
	@Override
	protected Result<?> match(Derivation derivation) {
		// Run through the list of patterns to match
		// Keep track of the previous pattern's result
		Result<?> result;
		// Loop over all patterns
		for (Pattern p : patterns) {
			// Attempt to match this pattern
			result = p.lazyMatch(derivation);
			// If success, return it
			if (result.isSuccess())
				return result;
			// Otherwise, try the next pattern instead.
		}

		// Looped over all patterns successfully, none matched. Send it back!
		return new Result<>(false, null, null);
	}

	/**
	 * Assigns a unique hash code based on the contents of the patterns list.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((patterns == null) ? 0 : patterns.hashCode());
		return result;
	}

	/**
	 * Declares that two PatternChoice objects are only equal if they have the same sequence of Patterns.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PatternChoice other = (PatternChoice) obj;
		if (patterns == null) {
			if (other.patterns != null)
				return false;
		} else if (!patterns.equals(other.patterns))
			return false;
		return true;
	}

}
