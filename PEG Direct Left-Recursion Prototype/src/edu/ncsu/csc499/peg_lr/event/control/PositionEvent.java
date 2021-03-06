/**
 * 
 */
package edu.ncsu.csc499.peg_lr.event.control;

import edu.ncsu.csc499.peg_lr.structure.InputContext;

/**
 * @author Melody Griesen
 *
 */
public class PositionEvent extends ControlEvent {

	/**
	 * Possible types for a PositionEvent.
	 * 
	 * @author Melody Griesen
	 *
	 */
	public enum PositionEventType {
		/** Context was advanced. */
		ADVANCE("Advance"),
		/** Context position was manually set. */
		SET("Set");

		/** Stores a display-friendly name for this enum. */
		private String displayName;

		/**
		 * Constructs each Enum value with a display-friendly name
		 *
		 * @param name the name to display for this Enum value
		 */
		PositionEventType(final String name) {
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

	/** Which subtype this event falls under. */
	private final PositionEventType type;

	public PositionEvent(final InputContext context, final PositionEventType type) {
		super(context, context.getPosition(), 1, "position to index " + context.getPosition());
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
