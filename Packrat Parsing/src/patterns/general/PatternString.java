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
public class PatternString extends Pattern {

	/** The string we're expecting to match. */
	private String matchString;

	/**
	 * Constructs a PatternString with a string to match.
	 * 
	 * @param matchString
	 */
	public PatternString(String matchString) {
		this.matchString = matchString;
	}

	/**
	 * Matches this pattern string by recursively matching one character at a time.
	 */
	@Override
	protected Result match(Derivation derivation) {
		// Use recursive helper
		Result match = matchRemaining(matchString, derivation);
		if (match.isSuccess()) {
			// Success
			match.setData(matchString);
		}
		return match;
	}

	/**
	 * Recursive method that matches a sequence of characters.
	 * 
	 * @param remainingPattern the String to match
	 * @param remainingInput   the input to match against
	 * @return a Result with empty value indicating a true/false match and, if true,
	 *         the derivation we ended at.
	 */
	private Result matchRemaining(String remainingPattern, Derivation remainingInput) {
		// Base case
		if ("".equals(remainingPattern)) {
			return new Result(true, null, remainingInput);
		}
		// Recursive case
		else {
			// Match the one character
			if (remainingInput.getChResult().isSuccess()
					&& remainingPattern.charAt(0) == remainingInput.getChResult().getData().charAt(0)) {
				// Success
				System.out.println("Matched [" + remainingPattern.charAt(0) + "]");
				return matchRemaining(remainingPattern.substring(1), remainingInput.getChResult().getDerivation());
			} else {
				// Failure
				System.out.println("Failed to match " + remainingPattern.charAt(0));
				return Result.FAIL();
			}
		}

	}

	/**
	 * Assigns a unique ID based on the pattern string.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((matchString == null) ? 0 : matchString.hashCode());
		return result;
	}

	/**
	 * Declares that two PatternString objects are equal if their String matches.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PatternString other = (PatternString) obj;
		if (matchString == null) {
			if (other.matchString != null)
				return false;
		} else if (!matchString.equals(other.matchString))
			return false;
		return true;
	}

}
