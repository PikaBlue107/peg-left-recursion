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
public class PatternDigit extends Pattern {

	/**
	 * Matches a single digit as determeind by Character.isDigit()
	 */
	@Override
	protected Result<?> match(Derivation derivation) {
		// Ensure input remaining
		if(!derivation.getChResult().isSuccess())
			return Result.FAIL();
		// Retrieve character
		char ch = derivation.getChResult().getValue();
		// Determines if the derivation character is a digit
		if(Character.isDigit(ch)) {
			// Is a digit! Success
			System.out.println("Matched [" + ch + "]");
			return new Result<Integer>(true, Integer.parseInt("" + ch), derivation.getChResult().getDerivation());
		} else {
			// Not a digit. Failure
			return Result.FAIL();
		}
	}
}
