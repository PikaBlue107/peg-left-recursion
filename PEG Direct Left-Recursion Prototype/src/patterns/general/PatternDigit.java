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
public class PatternDigit extends PatternComponent {

	/**
	 * Matches a single digit as determeind by Character.isDigit()
	 */
	@Override
	protected Result match(final InputContext context) {
		// Ensure input remaining
		if (context.isAtEnd()) {
			return Result.FAIL();
		}
		// Retrieve character
		final char ch = context.currentChar();
		// Determines if the derivation character is a digit
		if (Character.isDigit(ch)) {
			// Is a digit! Success
			context.addHistory("Matched [" + ch + "]");
			context.advance();
			return new Result(true, ch, context.getPosition() - 1);
		} else {
			// Not a digit. Failure
			return Result.FAIL();
		}
	}
}
