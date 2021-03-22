/**
 * 
 */
package event.control;

import patterns.general.Pattern;
import structure.InputContext;

/**
 * @author Melody Griesen
 *
 */
public class GrowingEvent extends ControlEvent {

	/**
	 * Possible types for a RecursionGrowEvent.
	 * 
	 * @author Melody Griesen
	 *
	 */
	public enum GrowingEventType {
		/** Recursion growing for one step was attempted. */
		GROW_ATTEMPT("Attempt Grow"),
		/** Recursion growing for one step failed because the match failed. */
		GROW_FAIL("Fail Grow"),
		/** Recursion growing for one step was rejected because the match shortened. */
		GROW_REJECT("Reject Grow"),
		/** Recursion growing for one step was grown. */
		GROW_ACCEPT("Accept Grow"),
		/** Recursion growing was terminated. */
		TERMINATE("Terminate");

		/** Stores a display-friendly name for this enum. */
		private String displayName;

		/**
		 * Constructs each Enum value with a display-friendly name
		 *
		 * @param name the name to display for this Enum value
		 */
		GrowingEventType(final String name) {
			displayName = name;
		}

		/**
		 * Retrieves the display name for this enum
		 *
		 * @return this enum's display name
		 */
		public String getDisplayName() {
			return displayName;
		}
	}

	/** Which sub-type this event falls under. */
	private final GrowingEventType type;

	/**
	 * Creates a GrowingEvent based on the provided context, type, pattern, and
	 * index.
	 * 
	 * @param context   the InputContext storing the saved results
	 * @param type      whether this memory event is a check, a save, or an assume
	 * @param pattern   the pattern that this memory event is using
	 * @param index     the index at which this growing step is happening
	 * @param iteration the number of this iteration, 1-indexed i.e. 1st iteration,
	 *                  2nd iteration, etc. For a TERMINATE growing event, should be
	 *                  the total number of *valid* iterations.
	 */
	public GrowingEvent(final InputContext context, final GrowingEventType type, final Pattern pattern, final int index,
			final int iteration) {
		super(context, index, 0,
				"for " + pattern.toString()
						+ (type == GrowingEventType.TERMINATE ? " (total # valid grow iterations: " + iteration + ")"
								: " (iteration #" + iteration + ")"));
		this.type = type;
	}

	/**
	 * Returns this position event's PositionEventType.
	 * 
	 * @return the display name for this event type
	 */
	@Override
	public String getType() {
		return type.getDisplayName();
	}

}
