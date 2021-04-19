/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.component.operator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import edu.ncsu.csc499.peg_lr.event.pattern.SequenceEvent;
import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternComponent;
import edu.ncsu.csc499.peg_lr.structure.InputContext;
import edu.ncsu.csc499.peg_lr.structure.Result;

/**
 * @author Melody Griesen
 *
 */
public class PatternSequence extends PatternComponent {

	/** List of patterns to match one after another. */
	private List<Pattern> patterns;

	{
		// Initialize the List
		patterns = new ArrayList<>();
	}

	/**
	 * Constructs a PatternSequence from all patterns provided. This pattern will be
	 * matched by a sequence of all patterns provided.
	 * 
	 * @param patterns the sequence of patterns to match
	 */
	public PatternSequence(final Pattern... patterns) {
		// For each Pattern in the list, add it to the ArrayList
		for (final Pattern p : patterns) {
			this.patterns.add(p);
		}
	}

	/**
	 * Constructs a PatternSequence from all patterns provided. This pattern will be
	 * matched by a sequence of all patterns provided.
	 * 
	 * @param patterns the sequence of patterns to match
	 */
	public PatternSequence(final List<Pattern> patterns) {
		this.patterns.addAll(patterns);
	}

	/**
	 * Chain method of adding patterns in sequence. Adds an extra pattern to match
	 * on the end of the sequence.
	 * 
	 * @param toAdd the new pattern to match after all previous patterns
	 * @return this PatternSequence object for multiple chainings of this method
	 */
	public PatternSequence add(final Pattern toAdd) {
		// Add the new pattern to the end of the list
		patterns.add(toAdd);
		// Return this object for method chaining
		return this;
	}

	/**
	 * Matches a sequence of Patterns by initially assuming a match with the given
	 * derivation then advancing that match for each Pattern that matches. Only
	 * returns a positive Result if all Patterns in the Sequence match.
	 * 
	 * @param derivation the input to start on
	 * @return a positive match after the last Pattern if success, a negative match
	 *         if failure
	 */
	@Override
	protected Result match(final InputContext context) {
		// Run through the list of patterns to match
		// Keep track of the previous pattern's result
		final Result sequence = new Result(context.getPosition());
		Result result;
		// Sequence index for event reporting
		int sequenceIdx = 0;
		// Loop over all patterns
		for (final Pattern p : patterns) {
			// Log attempt to match
			context.addHistory(new SequenceEvent(context, sequenceIdx, p));
			// Attempt to match this pattern
			result = p.lazyMatch(context);
			// Report result
			context.addHistory(new SequenceEvent(context, sequenceIdx, p, result));
			// If fail, return it
			if (!result.isSuccess()) {
				// Reset the context first
				context.setPosition(sequence.getStartIdx());
				return result;
				// Otherwise, use its result to try the next one.
			}
			// Otherwise, add its contents into the Result
			sequence.addChild(result);
			// Increment sequence index for next step
			sequenceIdx++;
		}

		// Looped over all patterns successfully. Send result!
		return sequence;
	}

	/**
	 * Assigns a unique hash code based on the contents of the patterns list.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + ((patterns == null) ? 0 : patterns.hashCode());
		return result;
	}

	/**
	 * Declares that two PatternSequence objects are only equal if they have the
	 * same sequence of Patterns.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PatternSequence other = (PatternSequence) obj;
		if (patterns == null) {
			if (other.patterns != null) {
				return false;
			}
		} else if (!patterns.equals(other.patterns)) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc} Represents each sequence elemnet in the form ( [definition] ) (
	 * [definition] )
	 */
	@Override
	public String getDefinition(final boolean component) {

		// Use StringBuilder to compile definition
		final StringBuilder definition = new StringBuilder();

		// Loop over all patterns we have
		for (final Pattern p : patterns) {

			// Append grouped sub-definition
//			definition.append("( ");
			definition.append(p.getDefinition(true));
//			definition.append(" )");

			// If non-final element, append space
			if (p != patterns.get(patterns.size() - 1)) {
				definition.append(" ");
			}
		}

		// Return final definition string
		return definition.toString();
	}

	/**
	 * {@inheritDoc} Retrieves the list of sequence components.
	 */
	@Override
	public List<Pattern> getPatternComponents() {
		return new ArrayList<>(patterns);
	}

	/**
	 * {@inheritDoc} Retrieves the first sequence pattern, and all subsequent
	 * patterns that are preceded by all nullable patterns.
	 */
	@Override
	protected Iterator<Pattern> getPossibleLeftmostComponents() {

		// Return a custom Iterator over the patterns in this sequence.
		// Elements are returned from next() if all leftward elements are nullable.
		return new Iterator<>() {

			/** List to return elements from */
			List<Pattern> sequenceComponents = patterns;

			/** Current position in the list */
			int idx = 0;

			/**
			 * The next element is returnable if it's at the front of the list, or the
			 * element to its left is nullable
			 * 
			 * @return true if the iterator is at the front of the list, or if the previous
			 *         element is nullable.
			 */
			@Override
			public boolean hasNext() {
				return (idx == 0) || patterns.get(idx - 1).isNullable();
			}

			/**
			 * If there is another element to retrieve, returns it and advances the iterator
			 * one position.
			 * 
			 * @return the next possibly left-recursive element in the list
			 * @throws NoSuchElementException if there are no more left-recursive elements
			 *                                in the list
			 */
			@Override
			public Pattern next() {
				// Check for no more elements
				if (!hasNext()) {
					throw new NoSuchElementException("No more left-recursive elements in sequence");
				}

				// Return the next element and increment the position
				return sequenceComponents.get(idx++);
			}
		};
	}

	/**
	 * {@inheritDoc} Returns true only if all elements in the sequence are nullable.
	 */
	@Override
	public boolean isNullable() {
		// Loop over the sequence
		for (final Pattern pattern : patterns) {
			// If this pattern is left-recursive, skip it
			if (pattern.isLeftRecursive()) {
				continue;
			}
			// If this pattern is not nullable
			if (!pattern.isNullable()) {
				// The overall sequence isn't nullable
				return false;
			}
		}

		// All of our patterns are nullable, so the sequence is too.
		return true;
	}

}
