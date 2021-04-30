/**
 * 
 */
package edu.ncsu.csc499.peg_lr.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import edu.ncsu.csc499.peg_lr.event.pattern.PatternEvent;
import edu.ncsu.csc499.peg_lr.pattern.Pattern;

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
		entry.setHistoryIdx(history.size());
		this.history.add(entry);
	}

	/**
	 * Provides the context's history in Iterable form, allowing a user to construct
	 * a string with each entry in this context's history in their desired format.
	 * 
	 * @return an Iterable of String objects stored in this context's history
	 */
	public Stream<ParseEvent> stream() {
		return history.stream();
	}

	/**
	 * Provides a static Predicate provider to filter streams for elements that only
	 * include the given class filter
	 *
	 * @param classFilter the class to filter by. Only ParseEvents that are the same
	 *                    class or a subclass of this filter will be included.
	 * @return a predicate to filter the Event stream by only events that match the
	 *         given class filter
	 */
	public static Predicate<ParseEvent> includeEventType(final Class<? extends ParseEvent> classFilter) {
		return (final ParseEvent e) -> {
			return classFilter.isAssignableFrom(e.getClass());
		};
	}

	/**
	 * Provides a static Predicate to filter streams for elements that concern the
	 * specified Pattern class. Events that do not concern any Pattern are included
	 * - to exclude them, use includeEventType.
	 * 
	 * @param classFilter the Pattern type to include
	 * @return a Predicate to filter the Event stream by only events that match the
	 *         given pattern filter
	 */
	public static Predicate<ParseEvent> includePattern(final Class<? extends Pattern> classFilter) {
		return includePattern(classFilter, false);
	}

	/**
	 * Provides a static Predicate to filter streams for elements that concern the
	 * specified Pattern class. Events that do not concern any Pattern are included
	 * - to exclude them, use includeEventType.
	 * 
	 * @param classFilter the Pattern type to include
	 * @param strict      whether to include subclasses of the specified pattern.
	 *                    false - only exact class matches; true - subclasses too
	 * @return a Predicate to filter the Event stream by only events that match the
	 *         given pattern filter
	 */
	public static Predicate<ParseEvent> includePattern(final Class<? extends Pattern> classFilter,
			final boolean strict) {
		return (final ParseEvent e) -> {
			// Determine if this event is a PatternEvent
			if (e instanceof PatternEvent) {
				final PatternEvent patEvent = (PatternEvent) e;
				// If we're performing strict filtering
				if (strict) {
					return classFilter.equals(patEvent.getPattern().getClass());
				}
				// Loose filtering: include subtypes
				else {
					// only include it if its pattern is castable from the given class
					return (patEvent.getPattern() == null)
							|| classFilter.isAssignableFrom(patEvent.getPattern().getClass());
				}
			}
			// Not a PatternEvent
			else {
				// Include it
				return true;
			}
		};
	}

	/**
	 * Creates a String of the form "%4d:\t%s\n" for each entry in history, where
	 * the number is a continuous incrementing counter, and the string is
	 * historyEntry.toString().
	 * 
	 * @param classFilter only history events that are inherited from this class
	 *                    will be printed
	 * @return a String containing all history entries
	 */
	public static String printHistory(final Stream<ParseEvent> stream) {
		// Set up string builder for history
		final StringBuilder historyString = new StringBuilder();

		stream.forEach((final ParseEvent historyEntry) -> {
			// Append a new line with formatted columns for step and full event details
			historyString.append(String.format("%4d:\t%s\n", historyEntry.getHistoryIdx(), historyEntry.toString()));
		});

		// Return resulting string
		return historyString.toString();
	}
}
