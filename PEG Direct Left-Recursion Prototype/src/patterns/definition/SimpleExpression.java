package patterns.definition;

import event.pattern.CharacterAcceptEvent;
import patterns.general.Pattern;
import structure.InputContext;
import structure.Result;

public class SimpleExpression extends Pattern {

	private static final SimpleNumber num = new SimpleNumber();

	private static final String TYPE = "Expression";

	@Override
	protected Result match(final InputContext context) {
		Result result;
		// Ordered choice
		final int initialPos = context.getPosition();

		// Attempt full expression
		result = tryFullExpression(context);
		if (result.isSuccess()) {
			return result;
		}

		// On failure, reset to beginning
		context.setPosition(initialPos);

		// Match num
		result = num.lazyMatch(context);
		if (!result.isSuccess()) {
			return result;
		}

		final Result expression = new Result(true, result.getData(), result.getStartIdx());
		expression.setType("Expression");
		expression.addChild(result); // TODO: Restructure addChild() to automatically add in data and index
										// information. Possibly a new constructor as well?
		return expression;
	}

	private Result tryFullExpression(final InputContext context) {

		// Create the possible Result found by matching this Expression
		final Result expression = new Result(true, "", context.getPosition());
		expression.setType("Expression");

		// Create a placeholder for the Results returned by each sub-match
		Result result;

		// Sequence

		// Match recursive Expression
		result = this.lazyMatch(context);
		// If not successful, return that Expression's failure
		if (!result.isSuccess()) {
			return result;
		}
		// Successful match. Add the child result to the overall Expression result
		expression.addChild(result);

		// Match plus
		result = this.matchPlus(context);
		// If unsuccessful, return that failure
		if (!result.isSuccess()) {
			return result;
		}
		// Successful match. Append the child match's data
		// TODO: Add the child result and flag it as "invisible"?
		expression.addChild(result);

		// Match Num
		result = num.lazyMatch(context);
		// If unsuccessful, return that failure
		if (!result.isSuccess()) {
			return result;
		}
		// Successful match. Add the child result to the overall Expression result
		expression.addChild(result);
		return expression;
	}

	private Result matchPlus(final InputContext context) {
		if (!context.isAtEnd() && context.currentDeriv().getChResult().isSuccess()
				&& (context.currentDeriv().getChResult().getData().charAt(0) == '+')) {
			context.addHistory(new CharacterAcceptEvent(context.currentChar(), context.getPosition()));
			context.advance();
			return new Result(true, '+', context.getPosition() - 1);
		} else {
			return Result.FAIL();
		}
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
	 * Returns the type (name) of this Pattern.
	 * 
	 * @return the String type of this Pattern
	 */
	@Override
	public String getType() {
		return TYPE;
	}

}
