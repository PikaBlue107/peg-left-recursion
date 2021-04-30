/**
 * 
 */
package edu.ncsu.csc499.peg_lr.event.pattern;

import edu.ncsu.csc499.peg_lr.event.ParseEvent;
import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.structure.InputContext;

/**
 * These types of events apply to specific patterns and the process of
 * accepting, rejecting, or progressing through them.
 * 
 * @author Melody Griesen
 *
 */
public abstract class PatternEvent extends ParseEvent {

	/**
	 * Possible types for a PatternEvent.
	 * 
	 * @author Melody Griesen
	 *
	 */
	public enum PatternEventType {
		/** Pattern or component was attempted. */
		ATTEMPT("Attempt"),
		/** Pattern or component was accepted. */
		ACCEPT("Accept"),
		/** Pattern or component was rejected. */
		REJECT("Reject"),
		/** Ordered choice was committed to. */
		COMMIT("Commit"),
		/** Repetition was expanded. */
		EXPAND("Expand"),
		/** Repetition was capped at max length. */
		LIMIT("Limit");

		/** Stores a display-friendly name for this enum. */
		private String displayName;

		/**
		 * Constructs each Enum value with a display-friendly name
		 *
		 * @param name the name to display for this Enum value
		 */
		PatternEventType(final String name) {
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

	/** The Pattern that this PatternEvent concerns. */
	private final Pattern pattern;

	public PatternEvent(final InputContext context, final int startIdx, final int length, final String detail,
			final Pattern pattern) {
		super(context, startIdx, length, detail);
		this.pattern = pattern;
	}

	public Pattern getPattern() {
		return pattern;
	}

}
