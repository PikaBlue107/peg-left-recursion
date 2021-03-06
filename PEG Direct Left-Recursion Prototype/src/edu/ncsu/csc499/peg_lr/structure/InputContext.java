/**
 * 
 */
package edu.ncsu.csc499.peg_lr.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ncsu.csc499.peg_lr.event.ParseEvent;
import edu.ncsu.csc499.peg_lr.event.control.MemoryEvent;
import edu.ncsu.csc499.peg_lr.event.control.MemoryEvent.MemoryEventType;
import edu.ncsu.csc499.peg_lr.event.control.PositionEvent;
import edu.ncsu.csc499.peg_lr.event.control.PositionEvent.PositionEventType;
import edu.ncsu.csc499.peg_lr.pattern.Pattern;

/**
 * Represents the current position of the parser in the input string. Allows
 * operations on the input string such as character retrieval and position
 * navigation.
 * 
 * @author Melody Griesen
 *
 */
public class InputContext {

	/*
	 * ----------------------------- FIELDS -------------------------------
	 */

	// Parse fields

	/** Original input string, stored for useful methods like substring. */
	private final String inputString;

	/**
	 * Tracks the current "position" of the Context. The character at [position] is
	 * the next one to be consumed.
	 */
	private int position;
	{
		// Position always starts initially at 0
		position = 0;
	}

	/** The Growing Map of Results identified by Pattern. */
	private Map<Integer, Map<Pattern, Result>> patterns;
	{
		this.patterns = new HashMap<>();
	}

	// Display fields

	/**
	 * List of all "events" that have happened in the context (matching,
	 * backtracking, etc.)
	 */
	private final List<ParseEvent> history = new ArrayList<>();

	/**
	 * How far the InputContext will print to either side when running toString()
	 */
	private int printRange;

	/**
	 * The default number of characters that toString() will print to either side.
	 */
	private static final int DEFAUT_PRINT_RANGE = 10;

	/** Lowercase epsilon, representing the end of the string. */
	public static final String CHAR_EPSILON = "\u03B5";

	/*
	 * ----------------------------- CONSTRUCTORS -------------------------------
	 */

	/**
	 * Constructs an {@link InputContext} object with all necessary metadata for
	 * each index.
	 * 
	 * @param input the input string that this object will contain
	 */
	public InputContext(final String input) {
		// Save the raw string
		this.inputString = input;

		// For each character of the input String, create an empty HashMap for the
		// growing map
		for (int i = 0; i < input.length(); i++) {
			// Set an empty hash map for each index
			patterns.put(i, new HashMap<>());
		}

		// For the last index, set it to an empty map
		patterns.put(input.length(), new HashMap<>());

		// Start the print range off at the default
		printRange = DEFAUT_PRINT_RANGE;
	}

	/*
	 * ----------------------------- BEHAVIOR -------------------------------
	 */

	// Position

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(final int position) {
		this.position = position;
		addHistory(new PositionEvent(this, PositionEventType.SET));
	}

	/**
	 * Determines if this InputContext is at the end of its string.
	 *
	 * @return true if the current position is at the end of the string (no active
	 *         character to consume)
	 */
	public boolean isAtEnd() {
		return this.position == this.inputString.length();
	}

	/**
	 * Returns the total length of the input string.
	 *
	 * @return the number of characters in the input string
	 */
	public int length() {
		return inputString.length();
	}

	/**
	 * Advances the InputContext by one step and returns the same Context to check.
	 *
	 * @return the Context, advanced one position.
	 * @throws IllegalStateException if already at the end of the input string
	 */
	public InputContext advance() {
		if (isAtEnd()) {
			throw new IllegalStateException("Cannot advance when already at end of input");
		}
		position++;
		addHistory(new PositionEvent(this, PositionEventType.ADVANCE));
		return this;
	}

	/**
	 * Advances the InputContext by one step and returns the previously current
	 * character.
	 *
	 * @return the previous character
	 */
	public char next() {
		final char c = currentChar();
		advance();
		return c;
	}

	// Input string

	/**
	 * Retrieves the input string that this InputContext holds.
	 *
	 * @return the inputString
	 */
	public String getInputString() {
		return getInputString(false);
	}

	/**
	 * Retrieves the input string, optionally adding an epsilon character to
	 * represent the end of input.
	 * 
	 * @param addEpsilon whether to add a trailing epsilon character representing
	 *                   the end of input
	 * @return the input string that this context is holding, with an optional
	 *         end-of-string epsilon character
	 */
	public String getInputString(final boolean addEpsilon) {
		return inputString + (addEpsilon ? CHAR_EPSILON : "");
	}

	/**
	 * Provides the current character under inspection in the Context.
	 * 
	 * @return the current character
	 * @throws IllegalStateException if at the end of the input string
	 */
	public char currentChar() {
		if (isAtEnd()) {
			throw new IllegalStateException();
		}
		return this.inputString.charAt(position);
	}

	/**
	 * TODO: Document
	 * 
	 * @param checker
	 * @return
	 */
	public boolean checkChar(final CharCheckable checker) {
		if (isAtEnd()) {
			return false;
		}
		return checker.check(currentChar());
	}

	/**
	 * TODO: Document
	 *
	 * @author Melody Griesen
	 *
	 */
	public interface CharCheckable {
		/**
		 * TODO: Document
		 *
		 * @param c
		 * @return
		 */
		boolean check(char c);
	}

	// Growing Map Results

	// Setters

	/**
	 * Saves the Result for a given Pattern at the current index in the growing map
	 * of this InputContext.
	 * 
	 * @param pattern the pattern that will be used to access this result
	 * @param result  the result that the given Pattern achieves
	 * @return the previous Result stored at the location in the growing map
	 */
	public Result setResultFor(final Pattern pattern, final Result result) {
		return setResultFor(pattern, result, getPosition());
	}

	/**
	 * Saves the Result for a given Pattern in the growing map of this InputContext.
	 * 
	 * @param pattern the pattern that will be used to access this result
	 * @param result  the result that the given Pattern achieves
	 * @param index   the index at which to save the result
	 * @return the previous Result stored at the location in the growing map
	 */
	public Result setResultFor(final Pattern pattern, final Result result, final int index) {
		addHistory(new MemoryEvent(this, MemoryEventType.SAVE, pattern, result, index));
		return patterns.get(index).put(pattern, result);
	}

	// Getters

	/**
	 * Retrieves the Result that the Growing Map knows of at the current position
	 * for the specified pattern.
	 * 
	 * @param p the pattern that should be used to retrieve a growing result
	 * @return the Result (if known) for the given Pattern, otherwise null if the
	 *         pattern's result is not known.
	 */
	public Result resultFor(final Pattern p) {
		return resultFor(p, getPosition());
	}

	/**
	 * Retrieves the Result that the Growing Map knows of at the specified position
	 * for the specified pattern.
	 *
	 * @param p     the pattern that should be used to retrieve a growing result
	 * @param index the index that should be used to retrieve a growing result
	 * @return the Result saved for the specified index and pattern
	 */
	public Result resultFor(final Pattern p, final int index) {
		final Result r = this.patterns.get(index).get(p);
		addHistory(new MemoryEvent(this, MemoryEventType.CHECK, p, r, index));
		return r;
	}

	/**
	 * Clears the specified pattern from the growing map at the specified index.
	 *
	 * @param p the pattern to clear
	 */
	public void clearResult(final Pattern p) {
		clearResult(p, getPosition());
	}

	/**
	 * Clears the specified pattern from the growing map at the specified index.
	 *
	 * @param p     the pattern to clear
	 * @param index the index to clear of the pattern
	 */
	public void clearResult(final Pattern p, final int index) {
		addHistory(new MemoryEvent(this, MemoryEventType.CLEAR, p, null, index));
		patterns.get(index).remove(p);
	}

	/**
	 * Returns the number of patterns that are registered with seeds at the current
	 * position
	 *
	 * @return the number of patterns that have a saved seed at this index
	 */
	public int getResultCount() {
		return getResultCount(getPosition());
	}

	/**
	 * Returns the number of patterns that are registered with seeds at the given
	 * position
	 *
	 * @param index the index to check
	 * @return the number of patterns that have a saved seed at this index
	 */
	public int getResultCount(final int index) {
		return patterns.get(index).size();
	}

	/**
	 * Determines if the Growing Map has already saved the Result for the given
	 * Pattern.
	 * 
	 * @param p the Pattern to check
	 * @return true if Pattern has a saved Result, else false
	 */
	public boolean hasSaved(final Pattern p) {
		return resultFor(p) != null;
	}

	/**
	 * Determines if the Growing Map has already saved the Result for the given
	 * Pattern and index.
	 * 
	 * @param p     the Pattern to check
	 * @param index the index to check
	 * @return true if Pattern has a saved Result, else false
	 */
	public boolean hasSaved(final Pattern p, final int index) {
		return resultFor(p, index) != null;
	}

	// Print range

	/**
	 * Retrieves the print range, a display setting indicating how far on either
	 * side of the current position a call to toString() will show.
	 * 
	 * @return the printRange
	 */
	public int getPrintRange() {
		return printRange;
	}

	/**
	 * Sets the print range, indicating how many characters to either side of the
	 * current position a call to toString() should print.
	 * 
	 * @param printRange the printRange to set
	 */
	public void setPrintRange(final int printRange) {
		this.printRange = printRange;
	}

	// History

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

	// Overall

	/**
	 * Generates a String displaying the current position of the InputContext to the
	 * user. Shows which character of the input string is currently active, as well
	 * as a number of characters to either side equal to the printRange setting.
	 * 
	 * @return a display-friendly string indicating where the context's position is
	 *         resting in the input string.
	 */
	@Override
	public String toString() {

		// Keep a StringBuilder to assemble the final String
		final StringBuilder builder = new StringBuilder();

		// We're gonna print brackets just at the start and end of the String

		// Determine the start index to print from
		final int beginIndex = Integer.max(-1, this.position - this.printRange);
		// Determine the end index to print to
		final int endIndex = Integer.min(inputString.length(), this.position + this.printRange);

		// Main loop from beginning to end of the print range
		for (int i = beginIndex; i <= endIndex; i++) {
			// If it's before the start of the string, do a left bracket
			if (i == -1) {
				builder.append("[");
			}
			// If it's after the end of the string, do a right bracket
			else if (i == inputString.length()) {
				builder.append("]");
			}
			// If it's somewhere in the string, just print that character
			else {
				builder.append(inputString.charAt(i));
			}
		}

		// Add a newline so we can print a carat indicating where we are
		builder.append("\n");

		// Calculate the number of spaces before the current position
		final int numSpacesBefore = this.position - beginIndex;

		// Print that many spaces
		for (int i = 0; i < numSpacesBefore; i++) {
			builder.append(" ");
		}

		// Append a carat indicating where we are
		builder.append("^");

		// Append one extra newline at the end
		builder.append("\n");

		// Return the final String
		return builder.toString();
	}

}
