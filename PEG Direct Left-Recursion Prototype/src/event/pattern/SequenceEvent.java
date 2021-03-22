package event.pattern;

import structure.InputContext;
import structure.Result;

public class SequenceEvent extends PatternEvent {

	/** PatternEventType indicating whether this was an accept or reject event. */
	private final PatternEventType type;

	/**
	 * Constructs all necessary data for this Event from the match Result. Used for
	 * constructing ATTEMPT SequenceEvents. The index of attempting is assumed to be
	 * the context's current index.
	 * 
	 * @param context     the InputContext used to feed to the pattern
	 * @param sequenceIdx the numbered sequence component being attempted, e.g.
	 *                    first component, third component, etc. (0-indexed)
	 */
	public SequenceEvent(final InputContext context, final int sequenceIdx) {
		super(context, context.getPosition(), 0, "sequence step " + sequenceIdx);
		this.type = PatternEventType.ATTEMPT;
	}

	/**
	 * Constructs all necessary data for this Event from the match Result. Used for
	 * constructing ACCEPT and REJECT SequenceEvents.
	 * 
	 * @param context     the InputContext used to feed to the pattern
	 * @param matchResult the result of the match applied
	 * @param sequenceIdx the numbered sequence component being attempted, e.g.
	 *                    first component, third component, etc. (0-indexed)
	 */
	public SequenceEvent(final InputContext context, final Result matchResult, final int sequenceIdx) {
		super(context, matchResult.getStartIdx(), matchResult.getData().length(), "sequence step " + sequenceIdx);
		this.type = matchResult.isSuccess() ? PatternEventType.ACCEPT : PatternEventType.REJECT;
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
