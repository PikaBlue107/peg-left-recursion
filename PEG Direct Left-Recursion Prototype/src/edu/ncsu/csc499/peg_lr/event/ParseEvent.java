/**
 * 
 */
package edu.ncsu.csc499.peg_lr.event;

import edu.ncsu.csc499.peg_lr.structure.InputContext;

/**
 * Stores information about a single instant event that occurred during an
 * attempt to match a Pattern over a string held by an InputContext.
 * 
 * This class should be extended for each type of parse event.
 * 
 * @author Melody Griesen
 *
 */
public abstract class ParseEvent {

	// TODO: New feature - timestamps for profiling?

	/** The portion of the input string that this Event occurs over. */
	private final String affected;

	/** The start index of the substring that this Event occurs over. */
	private final int startIdx;

	/** The end index of the substring that this Event occurs over. */
	private final int endIdx;

	/** The extra details of this ParseEvent. If null, won't be printed. */
	private final String detail;

	// Constants

	/** Number of characters to print the event name */
	private static final int TOSTRING_WIDTH_NAME = 25;

	/** Number of characters to print the event name */
	private static final int TOSTRING_WIDTH_TYPE = 18;

	/** Number of characters to print the event name */
	private static final int TOSTRING_WIDTH_POSITION = 25;

	/**
	 * Constructs this ParseEvent with the given range and detail. If detail is
	 * null, will skip printing it.
	 * 
	 * @param context  the InputContext used for parsing. Is used here to calculate
	 *                 the substring of the input text that's affected
	 * @param startIdx the first index of the input string that this Event occurs
	 *                 over
	 * @param length   the number of characters that this event spans
	 * @param detail   optional clarifying details about this ParseEvent
	 */
	public ParseEvent(final InputContext context, final int startIdx, final int length, final String detail) {
		// Set fields directly
		this.startIdx = startIdx;
		this.endIdx = startIdx + length;
		this.detail = detail;
		this.affected = context.getInputString(true).substring(startIdx, startIdx + length);
	}

	/**
	 * Constructs this ParseEvent with the given range.
	 * 
	 * @param context  the InputContext used for parsing. Is used here to calculate
	 *                 the substring of the input text that's affected
	 * @param startIdx the first index of the input string that this Event occurs
	 *                 over
	 * @param endIdx   the last index of the input string that this Event occurs
	 *                 over
	 */
	public ParseEvent(final InputContext context, final int startIdx, final int endIdx) {
		this(context, startIdx, endIdx, null);
	}

	/**
	 * Constructs this ParseEvent with the given index and detail. If detail is
	 * null, will skip printing it.
	 * 
	 * @param context the InputContext used for parsing. Is used here to calculate
	 *                the substring of the input text that's affected
	 * @param idx     the index of the input string that this Event occurs over
	 * @param detail  optional clarifying details about this ParseEvent
	 */
	public ParseEvent(final InputContext context, final int idx, final String detail) {
		this(context, idx, idx, detail);
	}

	/**
	 * Constructs this ParseEvent with the given index.
	 * 
	 * @param context the InputContext used for parsing. Is used here to calculate
	 *                the substring of the input text that's affected
	 * @param index   the first index of the input string that this Event occurs
	 *                over
	 */
	public ParseEvent(final InputContext context, final int idx) {
		this(context, idx, idx, null);
	}

	/**
	 * Determines this EventName from the class name
	 *
	 * @return this class' name (or subclass' name)
	 */
	public String getEventName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * Returns the subtype of this event, i.e. Accent / Reject, Save / Use, Expand /
	 * Close / Fail, etc.
	 *
	 * @return the specific subtype of the event that occurred
	 */
	public abstract String getType();

	/**
	 * Retrieves the index that this Event starts its effects at
	 * 
	 * @return the first index that this event affects
	 */
	public final int getStartIdx() {
		return startIdx;
	}

	/**
	 * Retrieves the index that this Event ends its effects at
	 * 
	 * @return the last index that this event affects
	 */
	public final int getEndIdx() {
		return endIdx;
	}

	/**
	 * Returns the location at which this event occurred. For single characters,
	 * this should be their index. For ranges (matches, repetitions, etc.) it should
	 * indicate the index range (e.g.
	 *
	 * @return
	 */
	public final String getLocation() {
		if (startIdx == endIdx) {
			return "index " + startIdx;
		} else {
			return "indices " + startIdx + "-" + endIdx;
		}
	}

	/**
	 * Returns the substring of text that this Event affects, calculated by startIdx
	 * and endIdx.
	 *
	 * @return the component of the input string that this Event is applicable to
	 */
	public final String getAffectedPart() {
		return affected;
	}

	/**
	 * Retrieves the details for this ParseEvent, describing the specific
	 * circumstances and intricacies of what happened.
	 *
	 * @return a String indicating the full details of this ParseEvent.
	 */
	public final String getDetail() {
		return detail;
	}

	/**
	 * Prints this event as a display-friendly String indicating the location, type,
	 * and details of the event.
	 * 
	 * @return a display-friendly String stating what happened in this Event.
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		int target = 0;

		// Add name
		builder.append(getEventName());
		// Padding
		target += TOSTRING_WIDTH_NAME;
		pad(builder, target);

		// Add index
		builder.append(" at ").append(getLocation());
		// If affected part isn't empty, add it
		if (!"".equals(getAffectedPart())) {
			builder.append(", \"").append(getAffectedPart()).append("\"");
		}
		// Add colon
		builder.append(":");
		// Padding
		target += TOSTRING_WIDTH_POSITION;
		pad(builder, target);

		// Add type
		builder.append(getType());
		// Padding
		target += TOSTRING_WIDTH_TYPE;
		pad(builder, target);

		// If there's more to print afterwards
		if (getDetail() != null) {
			// If detail isn't null, add it
			builder.append(getDetail());
		}

		// Return final string
		return builder.toString();
	}

	private void pad(final StringBuilder builder, final int ideal) {
		while ((builder.length()) < ideal) {
			builder.append(" ");
		}
	}
}
