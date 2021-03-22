package patterns.definition;

import patterns.general.Pattern;
import structure.InputContext;
import structure.Result;

public class SimpleNumber extends Pattern {

	/** The Type of this pattern (display/reference name) */
	private static final String TYPE = "Number";

	@Override
	protected Result match(final InputContext context) {
		if (context.isAtEnd() || !Character.isDigit(context.currentChar())) {
			return Result.FAIL();
		}

		final Result priorResult = new Result(true, context.currentChar(), context.getPosition());
//		priorResult.setType("Number");

		char match = context.next();

		// Indicate that we matched at least one number
		context.addHistory("Matched [" + match + "]");

		// While next step is a valid character
		while (context.checkChar(Character::isDigit)) {

			// Save the next match and step the context
			match = context.next();

			// Indicate that we matched that digit
			context.addHistory("Matched [" + match + "]");

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

	/**
	 * Provides the Type of this pattern
	 */
	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public String getDefinition(final boolean component) {
		if (component) {
			return getType();
		} else {
			return "[0-9]+";
		}
	}

}
