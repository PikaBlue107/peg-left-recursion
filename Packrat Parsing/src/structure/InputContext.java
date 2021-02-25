/**
 * 
 */
package structure;

/**
 * Represents the current position of the parser in the input string. Allows
 * operations on the input string such as character retrieval and position
 * navigation.
 * 
 * @author Melody Griesen
 *
 */
public class InputContext {

	/** Indexable array of Derivations, each holding metadata about its position. */
	private final Derivation[] inputData;

	/** Original input string, stored for useful methods like substring. */
	private final String inputString;

	/**
	 * How far the InputContext will print to either side when running toString()
	 */
	private int printRange;
	/**
	 * The default number of characters that toString() will print to either side.
	 */
	private static final int DEFAUT_PRINT_RANGE = 10;

	/**
	 * Tracks the current "position" of the Context. The character at [position] is
	 * the next one to be consumed.
	 */
	private int position;
	{
		// Position always starts initially at 0
		position = 0;
	}

	/**
	 * Constructs an {@link InputContext} object with all necessary metadata for
	 * each index.
	 * 
	 * @param input the input string that this object will contain
	 */
	public InputContext(final String input) {
		// Save the raw string
		this.inputString = input;

		// Ensure the inputData can hold the full String plus 1 null entry
		inputData = new Derivation[input.length() + 1];

		// For each character of the input String, create a Derivation object with all
		// necessary data
		for (int i = 0; i < input.length(); i++) {
			inputData[i] = new Derivation(input.charAt(i), i);
		}

		// For the last index, set it to null
		inputData[input.length()] = null;

		// Start the print range off at the default
		printRange = DEFAUT_PRINT_RANGE;
	}

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
	 * @return the inputString
	 */
	public String getInputString() {
		return inputString;
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
	 * Returns the Derivation at a given index.
	 *
	 * @param idx the index to be inspected
	 * @return the Derivation at that index, holding the character and associated
	 *         data
	 */
	public Derivation getDerivation(final int idx) {
		return inputData[idx];
	}

	/**
	 * Returns the currently active Derivation.
	 *
	 * @return the Derivation for the next character to be consumed.
	 */
	public Derivation currentDeriv() {
		return inputData[position];
	}

	/**
	 * Advances the InputContext by one step and returns the same Context to check.
	 *
	 * @return the Context, advanced one position.
	 */
	public InputContext advance() {
		position++;
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

	public boolean checkChar(final CharCheckable checker) {
		if (isAtEnd()) {
			return false;
		}
		return checker.check(currentDeriv().getChResult().getData().charAt(0));
	}

	public interface CharCheckable {
		boolean check(char c);
	}

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
