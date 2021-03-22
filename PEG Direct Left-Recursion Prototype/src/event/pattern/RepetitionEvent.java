/**
 * 
 */
package event.pattern;

import patterns.general.Pattern;
import structure.InputContext;
import structure.Result;

/**
 * @author Melody Griesen
 *
 */
public class RepetitionEvent extends PatternEvent {

	/**
	 * Type of meta match event this object represents. One possible step in the
	 * meta-matching process.
	 */
	private final PatternEventType type;

	/**
	 * Constructs a general RepetitionEvent for attempting a pattern repetition at a
	 * given index. The index is assumed to be the context's current position.
	 *
	 * @param context       the context used for matching
	 * @param pattern       the pattern being matched
	 * @param index         the index at which matching occurs
	 * @param repetitionIdx the number of this repetition, ex. 0th repetition, 1st
	 *                      repetition, etc.
	 */
	public RepetitionEvent(final InputContext context, final Pattern pattern, final int repetitionNum) {
		super(context, context.getPosition(), 0, "repetition #" + repetitionNum + " of pattern " + pattern.getType());
		this.type = PatternEventType.ATTEMPT;
	}

	/**
	 * Constructs a RepetitionEvent for accepting, rejecting, or closing a
	 * repetition.
	 *
	 * @param context the context that's being matched
	 * @param pattern the pattern that's used for matching
	 * @param result  the result that was acquired
	 * @param type    the PatternEventType for this RepetitionEvent. Should be
	 *                either ACCEPT or CLOSE.
	 */
	public RepetitionEvent(final InputContext context, final Pattern pattern, final int repetitionNum,
			final Result result, final PatternEventType type) {
		super(context, result.getStartIdx(), result.getData().length(), (type == PatternEventType.LIMIT ? "at " : "")
				+ "repetition #" + repetitionNum + " of pattern " + pattern.getType());
		this.type = type;
	}

	/**
	 * Returns the display name of this MetaMatchEvent's type
	 * 
	 * @return the String display name for the type
	 */
	@Override
	public String getType() {
		return type.getDisplayName();
	}

}
