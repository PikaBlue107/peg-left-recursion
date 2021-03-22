/**
 * 
 */
package event.pattern;

import structure.InputContext;

/**
 * @author Melody Griesen
 *
 */
public class CharacterAcceptEvent extends PatternEvent {

	/**
	 * Constructs a CharacterEvent with a context reference and the idx of the
	 * character accepted.
	 *
	 * @param context the context holding the input string
	 * @param idx     the index of the accepted character in the input string
	 */
	public CharacterAcceptEvent(final InputContext context, final int idx) {
		super(context, idx, 1, null);
	}

	/**
	 * All character events are simply accepting one character.
	 */
	@Override
	public String getType() {
		return PatternEventType.ACCEPT.getDisplayName();
	}

}
