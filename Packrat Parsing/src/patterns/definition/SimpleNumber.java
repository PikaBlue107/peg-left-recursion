package patterns.definition;

import patterns.general.Pattern;
import structure.InputContext;
import structure.Result;

public class SimpleNumber extends Pattern {

	@Override
	protected Result match(final InputContext context) {
		if (!context.currentDeriv().getChResult().isSuccess()
				|| !Character.isDigit(context.currentDeriv().getChResult().getData().charAt(0))) {
			return Result.FAIL();
		}

		final Result priorResult = new Result(true, context.currentChar(), context.getPosition());
		priorResult.setType("Number");

		char match = context.next();

		// Indicate that we matched at least one number
		System.out.println("Matched [" + match + "]");

		// While next step is a valid character
		while (context.checkChar(Character::isDigit)) {

			// Save the next match and step the context
			match = context.next();

			// Indicate that we matched that digit
			System.out.println("Matched [" + match + "]");

			// Add matched character into Result
			priorResult.addChar(match);
		}

		return priorResult;
	}

	/**
	 * Unique by instance
	 */
	@Override
	public int hashCode() {
		return this.getID();
	}

	/**
	 * Must be same instance
	 */
	@Override
	public boolean equals(final Object obj) {
		return this == obj;
	}

}
