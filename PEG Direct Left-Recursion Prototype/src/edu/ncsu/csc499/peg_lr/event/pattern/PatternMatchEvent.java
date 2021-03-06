package edu.ncsu.csc499.peg_lr.event.pattern;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.structure.InputContext;
import edu.ncsu.csc499.peg_lr.structure.Result;

public class PatternMatchEvent extends PatternEvent {

	/** PatternEventType indicating whether this was an accept or reject event. */
	private final PatternEventType type;

	/**
	 * Constructs all necessary data for this Event from the match Result. Used for
	 * constructing
	 * 
	 * @param matchResult
	 * @param detail
	 * @param type
	 */
	public PatternMatchEvent(final InputContext context, final Result matchResult, final Pattern pattern) {
		super(context, matchResult.getStartIdx(), matchResult.getData().length(), pattern.getClass().getSimpleName()
				+ (matchResult.isSuccess() ? " matches \"" + matchResult.getData() + "\"" : " does not match"));
		this.type = matchResult.isSuccess() ? PatternEventType.ACCEPT : PatternEventType.REJECT;
	}

	/**
	 * Constructs an ATTEMPT PatternMatchEvent by using the Pattern's class name and
	 * the provided index.
	 *
	 * @param idx     the index at which this pattern is attempted
	 * @param pattern the pattern that this Event occurs for
	 */
	public PatternMatchEvent(final InputContext context, final int idx, final Pattern pattern) {
		super(context, idx, 0, pattern.toString());
		type = PatternEventType.ATTEMPT;
	}

	/**
	 * Provides whether this event is an attempt, accept, or reject event.
	 * 
	 * @return PatternEventType ATTEMPT, ACCEPT, or REJECT indicating which this
	 *         event represents.
	 */
	@Override
	public String getType() {
		return type.getDisplayName();
	}

}
