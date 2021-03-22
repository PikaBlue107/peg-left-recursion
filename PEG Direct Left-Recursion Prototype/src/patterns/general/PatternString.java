/**
 * 
 */
package patterns.general;

import event.pattern.CharacterAcceptEvent;
import structure.InputContext;
import structure.Result;

/**
 * @author Melody Griesen
 *
 */
public class PatternString extends PatternComponent {

	/** The string we're expecting to match. */
	private final String matchString;

	/**
	 * Constructs a PatternString with a string to match.
	 * 
	 * @param matchString
	 */
	public PatternString(final String matchString) {
		this.matchString = matchString;
	}

	/**
	 * Matches this pattern string by recursively matching one character at a time.
	 */
	@Override
	protected Result match(final InputContext context) {

		// Save initial position
		final int initialPosition = context.getPosition();

		// Use iterative solution
		final Result match = new Result(true, "", initialPosition);

		// Loop for each character of the target string
		for (final char c : matchString.toCharArray()) {
			// If the next character matches, accept
			if (!context.isAtEnd() && (c == context.next())) {
				// Add character to Result
				match.addChar(c);
				context.addHistory(new CharacterAcceptEvent(c, context.getPosition() - 1));
			}
			// Else, character doesn't match.
			else {
				// Pack up, go home.
				// Reset context back to start
				context.setPosition(initialPosition);
				// Return failure
				return Result.FAIL(context.getPosition());
			}
		}

		// Return successful match
		return match;
	}

	/**
	 * Generates a display string that shows the PatternString's match string
	 */
	@Override
	public String toString() {
		return "PatternString [matchString=" + matchString + "]";
	}

	/**
	 * Assigns a unique ID based on the pattern string.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + ((matchString == null) ? 0 : matchString.hashCode());
		return result;
	}

	/**
	 * Declares that two PatternString objects are equal if their String matches.
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
		final PatternString other = (PatternString) obj;
		if (matchString == null) {
			if (other.matchString != null) {
				return false;
			}
		} else if (!matchString.equals(other.matchString)) {
			return false;
		}
		return true;
	}

}
