/**
 * 
 */
package edu.ncsu.csc499.peg_lr.event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tracks a history of {@link ParseEvent}s that record the process of matching a
 * pattern against an input string.
 * 
 * @author Melody Griesen
 *
 */
public class EventHistory {

	/**
	 * List of all "events" that have happened in the context (matching,
	 * backtracking, etc.)
	 */
	private final List<ParseEvent> history = new ArrayList<>();

	/**
	 * Instantiates a new EventHistory with an empty history log.
	 */
	public EventHistory() {
		super();
	}

	/**
	 * Adds the given entry into the context's history list.
	 *
	 * @param entry the entry to add to the end of the history.
	 */
	public void addHistory(final ParseEvent entry) {
		this.history.add(entry);
	}

	/**
	 * Provides the context's history in Iterable form, allowing a user to construct
	 * a string with each entry in this context's history in their desired format.
	 * 
	 * @return an Iterable of String objects stored in this context's history
	 */
	public Iterable<ParseEvent> getHistory() {
		return getHistory(ParseEvent.class);
	}

	/**
	 * Returns a filtered list of the context history in Iterable form, allowing a
	 * user to construct a string with exactly the entries desired in the format
	 * they desire.
	 * 
	 * @param filterName the class name used for filtering. Will accept sub-classes,
	 *                   too.
	 * @return a filtered list of all ParseEvents that match the class name given
	 *         (or subclasses)
	 */
	public Iterable<ParseEvent> getHistory(final Class<? extends ParseEvent> classFilter) {

		if (classFilter == null) {
			throw new NullPointerException("Cannot filter by a null filter!");
		}

		final List<ParseEvent> filtered = history.stream()
				.filter((final ParseEvent p) -> classFilter.isAssignableFrom(p.getClass()))
				.collect(Collectors.toList());

		return filtered;

	}

	/**
	 * Creates a String of the form "%4d:\t%s\n" for each entry in history, where
	 * the number is a continuous incrementing counter, and the string is
	 * historyEntry.toString()
	 * 
	 * @return a String containing all history entries
	 */
	public String printHistory() {
		return printHistory(ParseEvent.class);
	}

	/**
	 * Creates a String of the form "%4d:\t%s\n" for each entry in history, where
	 * the number is a continuous incrementing counter, and the string is
	 * historyEntry.toString(). Only includes entries that are valid objects or
	 * sub-objects of the given filter class.
	 * 
	 * @param classFilter only history events that are inherited from this class
	 *                    will be printed
	 * @return a String containing all history entries
	 */
	public String printHistory(final Class<? extends ParseEvent> classFilter) {
		// Set up string builder for history
		final StringBuilder historyString = new StringBuilder();

		// Track which step of history we're on
		int historyIdx = 0;

		// Loop over all history events, from earliest to latest
		for (final ParseEvent historyEntry : getHistory()) {
			// Append a new line with formatted columns for step and full event details
			historyString.append(String.format("%4d:\t%s\n", historyIdx++, historyEntry.toString()));
		}

		// Return resulting string
		return historyString.toString();
	}
}
