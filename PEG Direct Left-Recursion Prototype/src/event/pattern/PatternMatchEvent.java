package event.pattern;

import patterns.general.Pattern;
import structure.Result;

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
	public PatternMatchEvent(final Result matchResult, final Pattern pattern) {
		super(matchResult.getData(), matchResult.getStartIdx(), matchResult.getData().length(),
				"for pattern " + pattern.getClass().getSimpleName());
		this.type = matchResult.isSuccess() ? PatternEventType.ACCEPT : PatternEventType.REJECT;
	}

	/**
	 * Constructs an ATTEMPT PatternMatchEvent by using the Pattern's class name and
	 * the provided index.
	 *
	 * @param idx     the index at which this pattern is attempted
	 * @param pattern the pattern that this Event occurs for
	 */
	public PatternMatchEvent(final int idx, final Pattern pattern) {
		super("", idx, idx, pattern.getClass().getSimpleName());
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
