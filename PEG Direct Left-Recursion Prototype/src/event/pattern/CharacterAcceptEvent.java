/**
 * 
 */
package event.pattern;

import event.ParseEvent;
import event.pattern.PatternEvent.PatternEventType;
import structure.InputContext;

/**
 * @author Melody Griesen
 *
 */
public class CharacterAcceptEvent extends ParseEvent {

	/**
	 * Constructs a CharacterEvent with a context reference and the idx of the
	 * character accepted.
	 *
	 * @param context the context holding the input string
	 * @param idx     the index of the accepted character in the input string
	 */
	public CharacterAcceptEvent(final InputContext context, final int idx) {
		super(context, idx);
	}

	/**
	 * Constructs a CharacterEvent with the character accepted and the idx of the
	 * character.
	 *
	 * @param c   the character accepted for this event
	 * @param idx the index of the accepted character in the input string
	 */
	public CharacterAcceptEvent(final char c, final int idx) {
		super("" + c, idx, 1, null);
	}

	/**
	 * All character events are simply accepting one character.
	 */
	@Override
	public String getType() {
		return PatternEventType.ACCEPT.getDisplayName();
	}

}
