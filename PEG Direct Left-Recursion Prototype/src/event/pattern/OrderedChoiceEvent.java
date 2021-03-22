package event.pattern;

import structure.InputContext;
import structure.Result;

public class OrderedChoiceEvent extends PatternEvent {

	/** PatternEventType indicating whether this was an accept or reject event. */
	private final PatternEventType type;

	/**
	 * Constructs all necessary data for this Event from the match Result. Used for
	 * constructing ATTEMPT OrderedChoiceEvents. The index of attempting is assumed
	 * to be the context's current index.
	 * 
	 * @param context   the InputContext used to feed to the pattern
	 * @param choiceIdx the numbered ordered choice being attempted, e.g. first
	 *                  choice, third choice, etc. (0-indexed)
	 */
	public OrderedChoiceEvent(final InputContext context, final int choiceIdx) {
		super(context, context.getPosition(), 0, "ordered choice option " + choiceIdx);
		this.type = PatternEventType.ATTEMPT;
	}

	/**
	 * Constructs all necessary data for this Event from the match Result. Used for
	 * constructing ACCEPT and REJECT OrderedChoiceEvents.
	 * 
	 * @param context     the InputContext used to feed to the pattern
	 * @param matchResult the result of the match applied
	 * @param choiceIdx   the numbered ordered choice being attempted, e.g. first
	 *                    choice, third choice, etc. (0-indexed)
	 */
	public OrderedChoiceEvent(final InputContext context, final Result matchResult, final int choiceIdx) {
		super(context, matchResult.getStartIdx(), matchResult.getData().length(), "ordered choice option " + choiceIdx);
		this.type = matchResult.isSuccess() ? PatternEventType.COMMIT : PatternEventType.REJECT;
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
