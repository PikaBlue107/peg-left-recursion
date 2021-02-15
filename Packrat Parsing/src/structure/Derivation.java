package structure;

import java.util.HashMap;
import java.util.Map;

import patterns.general.Pattern;

public class Derivation implements Comparable<Derivation> {

	/** The character held at this derivation. */
	private Result<Character> ch;

	/** The length until the end of the string. */
	private int charsRemaining;

	/** The Map of Results identified by Pattern. */
	private Map<Pattern, Result<?>> patterns;
	{
		this.patterns = new HashMap<>();
	}

	/**
	 * Constructs a Derivation with the String that it must represent. This
	 * Derivation takes one character and constructs a new Derivation with one less
	 * character to contain, all the way until a Derivation holding the empty string
	 * is created.
	 * 
	 * @param remaining the remaining input String that this Derivation must
	 *                  represent
	 */
	public Derivation(String remaining) {
		charsRemaining = remaining.length();
		if ("".equals(remaining)) {
			ch = new Result<Character>(false, null, null);
		} else {
			ch = new Result<Character>(true, remaining.charAt(0), new Derivation(remaining.substring(1)));
		}
	}

	/**
	 * Provides the number of characters until the end of the String, from this
	 * Derivation.
	 * 
	 * @return this Derivation's number of characters remaining
	 */
	public int getCharsRemaining() {
		return charsRemaining;
	}

	/**
	 * Retrieves the result providing the next character and Derivation in the
	 * String. If this is the last derivation for the input (representing the empty
	 * string), the Result will be false.
	 * 
	 * @return the Result for this Derivation, indicating its character and next
	 *         Derivation.
	 */
	public Result<Character> getChResult() {
		return ch;
	}

	/**
	 * Saves the Result for a given Pattern in this Derivation.
	 * 
	 * @param pattern
	 * @param result
	 */
	public void setResultFor(Pattern pattern, Result<?> result) {
		patterns.put(pattern, result);
	}

	/**
	 * Retrieves the Result that this Derivation knows of for the specified pattern.
	 * 
	 * @return the Result (if known) for the given Pattern, otherwise null if the
	 *         pattern's result is not known.
	 */
	public Result<?> resultFor(Pattern p) {
		return patterns.get(p);
	}

	/**
	 * Determines if this Derivation has already saved the Result for the given
	 * Pattern.
	 * 
	 * @param p the Pattern to check
	 * @return true if Pattern has a saved Result, else false
	 */
	public boolean hasSaved(Pattern p) {
		return patterns.containsKey(p);
	}

	/**
	 * Compares Derivations by their point in the input. A Derivation with greater
	 * characters remaining is sorted less than a derivation with fewer characters
	 * remaining.
	 */
	@Override
	public int compareTo(Derivation o) {
		return o.getCharsRemaining() - this.getCharsRemaining();
	}

}
