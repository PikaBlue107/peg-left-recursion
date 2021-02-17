package structure;

import java.util.HashMap;
import java.util.Map;

import patterns.general.Pattern;

public class Derivation implements Comparable<Derivation> {

	/** The character held at this derivation. */
	private Result ch;

	/** The index of this Derivation in the String, 1-indexed. */
	private int index;

	/** The Map of Results identified by Pattern. */
	private Map<Pattern, Result> patterns;
	{
		this.patterns = new HashMap<>();
	}

	/**
	 * Implements a public-facing constructor by delegating to the private indexed
	 * constructor.
	 * 
	 * @param str the input String that this Derivation will track
	 */
	public Derivation(String str) {
		this(str, 1);
	}

	/**
	 * Constructs a Derivation with the String that it must represent. This
	 * Derivation takes the full string and the index that this Derivation occupies
	 * in the string, saves its character in a Result, and constructs the next
	 * Derivation in that Result.
	 * 
	 * @param str the input String that this Derivation will track
	 * @param index the index of the String that this Derivation will occupy
	 */
	private Derivation(String str, int index) {
		this.index = index;
		if (index == str.length()) {
			ch = new Result(false, null, null);
		} else {
			ch = new Result(true, "" + str.charAt(index), new Derivation(str, index + 1));
		}
	}

//	/**
//	 * Provides the number of characters until the end of the String, from this
//	 * Derivation.
//	 * 
//	 * @return this Derivation's number of characters remaining
//	 */
//	public int getCharsRemaining() {
//		return charsRemaining;
//	}

	/**
	 * Provides the index of this Derivation in the input String
	 * 
	 * @return the index of this Derivation
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Retrieves the result providing the next character and Derivation in the
	 * String. If this is the last derivation for the input (representing the empty
	 * string), the Result will be false.
	 * 
	 * @return the Result for this Derivation, indicating its character and next
	 *         Derivation.
	 */
	public Result getChResult() {
		return ch;
	}

	/**
	 * Saves the Result for a given Pattern in this Derivation.
	 * 
	 * @param pattern
	 * @param result
	 */
	public void setResultFor(Pattern pattern, Result result) {
		patterns.put(pattern, result);
	}

	/**
	 * Retrieves the Result that this Derivation knows of for the specified pattern.
	 * 
	 * @return the Result (if known) for the given Pattern, otherwise null if the
	 *         pattern's result is not known.
	 */
	public Result resultFor(Pattern p) {
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
		return this.getIndex() - o.getIndex();
	}

}
