/**
 * 
 */
package event.control;

import patterns.Pattern;
import structure.InputContext;
import structure.Result;

/**
 * @author Melody Griesen
 *
 */
public class MemoryEvent extends ControlEvent {

	/**
	 * Possible types for a PositionEvent.
	 * 
	 * @author Melody Griesen
	 *
	 */
	public enum MemoryEventType {
		/** Memory was checked. */
		CHECK("Check"),
		/** Memory was saved. */
		SAVE("Save");

		/** Stores a display-friendly name for this enum. */
		private String displayName;

		/**
		 * Constructs each Enum value with a display-friendly name
		 *
		 * @param name the name to display for this Enum value
		 */
		MemoryEventType(final String name) {
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
	private final MemoryEventType type;

	/**
	 * Creates a MemoryEvent to be logged for checking, saving, or assuming a match
	 * in memory. Should be called *before* making any position changes as part of
	 * an ASSUME operation.
	 * 
	 * @param context the InputContext storing the saved results
	 * @param type    whether this memory event is a check, a save, or an assume
	 * @param pattern the pattern that this memory event is using
	 * @param result  the Result saved in memory (may be null)
	 */
	public MemoryEvent(final InputContext context, final MemoryEventType type, final Pattern pattern,
			final Result result) {
		this(context, type, pattern, result, context.getPosition());
	}

	/**
	 * Creates a MemoryEvent to be logged for checking, saving, or assuming a match
	 * in memory. Should be called *before* making any position changes as part of
	 * an ASSUME operation.
	 * 
	 * @param context the InputContext storing the saved results
	 * @param type    whether this memory event is a check, a save, or an assume
	 * @param pattern the pattern that this memory event is using
	 * @param result  the Result saved in memory (may be null)
	 * @param index   the index at whilch the MemoryEvent is occurring
	 */
	public MemoryEvent(final InputContext context, final MemoryEventType type, final Pattern pattern,
			final Result result, final int index) {
		super(context, index, (result == null ? 0 : result.getData().length()),
				"grow(idx: " + index + ", pat:" + pattern.getType() + ") = " + result);
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
