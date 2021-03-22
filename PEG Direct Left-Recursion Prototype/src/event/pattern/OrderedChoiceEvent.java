package event.pattern;

import patterns.general.Pattern;
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
	public OrderedChoiceEvent(final InputContext context, final int choiceIdx, final Pattern pattern) {
		super(context, context.getPosition(), 0, "ordered choice option " + choiceIdx + ": " + pattern.toString());
		this.type = PatternEventType.ATTEMPT;
	}

	/**
	 * Constructs all necessary data for this Event from the match Result. Used for
	 * constructing ACCEPT and REJECT OrderedChoiceEvents.
	 * 
	 * @param context     the InputContext used to feed to the pattern
	 * @param choiceIdx   the numbered ordered choice being attempted, e.g. first
	 *                    choice, third choice, etc. (0-indexed)
	 * @param pattern     TODO
	 * @param matchResult the result of the match applied
	 */
	public OrderedChoiceEvent(final InputContext context, final int choiceIdx, final Pattern pattern,
			final Result matchResult) {
		super(context, matchResult.getStartIdx(), matchResult.getData().length(),
				"ordered choice option " + choiceIdx + ": " + pattern.toString());
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
