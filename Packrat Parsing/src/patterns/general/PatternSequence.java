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
public class PatternSequence extends Pattern {

	/** List of patterns to match one after another. */
	private List<Pattern> patterns;

	{
		// Initialize the List
		patterns = new ArrayList<>();
	}

	/**
	 * Constructs a PatternSequence from all patterns provided. This pattern will be
	 * matched by a sequence of all patterns provided.
	 * 
	 * @param patterns the sequence of patterns to match
	 */
	public PatternSequence(Pattern... patterns) {
		// For each Pattern in the list, add it to the ArrayList
		for (Pattern p : patterns)
			this.patterns.add(p);
	}

	/**
	 * Constructs a PatternSequence from all patterns provided. This pattern will be
	 * matched by a sequence of all patterns provided.
	 * 
	 * @param patterns the sequence of patterns to match
	 */
	public PatternSequence(List<Pattern> patterns) {
		this.patterns.addAll(patterns);
	}

	/**
	 * Chain method of adding patterns in sequence. Adds an extra pattern to match
	 * on the end of the sequence.
	 * 
	 * @param toAdd the new pattern to match after all previous patterns
	 * @return this PatternSequence object for multiple chainings of this method
	 */
	public PatternSequence add(Pattern toAdd) {
		// Add the new pattern to the end of the list
		patterns.add(toAdd);
		// Return this object for method chaining
		return this;
	}

	/**
	 * Matches a sequence of Patterns by initially assuming a match with the given
	 * derivation then advancing that match for each Pattern that matches. Only
	 * returns a positive Result if all Patterns in the Sequence match.
	 * 
	 * @param derivation the input to start on
	 * @return a positive match after the last Pattern if success, a negative match
	 *         if failure
	 */
	@Override
	protected Result<?> match(Derivation derivation) {
		// Run through the list of patterns to match
		// Keep track of the previous pattern's result
		Result<?> result = new Result<>(true, null, derivation);
		// Loop over all patterns
		for (Pattern p : patterns) {
			// Attempt to match this pattern
			result = p.lazyMatch(result.getDerivation());
			// If fail, return it
			if (!result.isSuccess())
				return result;
			// Otherwise, use its result to try the next one.
		}

		// Looped over all patterns successfully. Send result!
		return result;
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
	 * Declares that two PatternSequence objects are only equal if they have the same sequence of Patterns.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PatternSequence other = (PatternSequence) obj;
		if (patterns == null) {
			if (other.patterns != null)
				return false;
		} else if (!patterns.equals(other.patterns))
			return false;
		return true;
	}

}
