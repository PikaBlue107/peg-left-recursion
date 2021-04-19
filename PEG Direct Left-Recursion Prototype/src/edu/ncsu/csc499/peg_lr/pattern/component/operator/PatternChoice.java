/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.component.operator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.ncsu.csc499.peg_lr.event.pattern.OrderedChoiceEvent;
import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternComponent;
import edu.ncsu.csc499.peg_lr.structure.InputContext;
import edu.ncsu.csc499.peg_lr.structure.Result;

/**
 * @author Melody Griesen
 *
 */
public class PatternChoice extends PatternComponent {

	/** List of patterns to match in ordered choice alternatives. */
	private List<Pattern> patterns;

	{
		// Initialize the List
		patterns = new ArrayList<>();
	}

	/**
	 * Constructs a PatternChoice from all patterns provided. This pattern will be
	 * matched by an ordered choice of all patterns provided.
	 * 
	 * @param patterns the set of possible patterns to choose from
	 */
	public PatternChoice(final Pattern... patterns) {
		// For each Pattern in the list, add it to the ArrayList
		for (final Pattern p : patterns) {
			this.patterns.add(p);
		}
	}

	/**
	 * Constructs a PatternChoice from all patterns provided. This pattern will be
	 * matched by an ordered choice of all patterns provided.
	 * 
	 * @param patterns the set of possible patterns to choose from
	 */
	public PatternChoice(final List<Pattern> patterns) {
		this.patterns.addAll(patterns);
	}

	/**
	 * Chain method of adding patterns in subsequent method calls. Adds an extra
	 * pattern choice to consider if all previous patterns fail.
	 * 
	 * @param toAdd the new pattern to consider after all previous patterns have
	 *              failed
	 * @return this PatternChoice object for multiple chainings of this method
	 */
	public PatternChoice add(final Pattern toAdd) {
		// Add the new pattern to the end of the list
		patterns.add(toAdd);
		// Return this object for method chaining
		return this;
	}

	/**
	 * Matches an ordered choice of Patterns by initially assuming a non-match with
	 * the given derivation then trying each Pattern in the list until one matches.
	 * Only returns a positive Result if at least one Pattern in the list matches.
	 * 
	 * @param derivation the input to start on
	 * @return a positive match after any Pattern succeeds, a negative match if none
	 *         of them do
	 */
	@Override
	protected Result match(final InputContext context) {
		// Track the initial starting position
		final Result choice = new Result(context.getPosition());

		// Run through the list of patterns to match
		// Keep track of the previous pattern's result
		Result result;
		// Choice index for event reporting
		int choiceIdx = 0;
		// Loop over all patterns
		for (final Pattern p : patterns) {
			// Log attempt to match
			context.addHistory(new OrderedChoiceEvent(context, choiceIdx, p));

			// Attempt to match this pattern
			result = p.lazyMatch(context);
			// Report result
			context.addHistory(new OrderedChoiceEvent(context, choiceIdx, p, result));

			// If success, save and return the choice result
			if (result.isSuccess()) {
				choice.addChild(result);
				return choice;
			}

			// Otherwise, try the next pattern instead. Reset the context!
			context.setPosition(choice.getStartIdx());
			// Increment choice index for next step
			choiceIdx++;
		}

		// Looped over all patterns successfully, none matched. Send it back!
		return Result.FAIL(context.getPosition());
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
	 * Declares that two PatternChoice objects are only equal if they have the same
	 * sequence of Patterns.
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
		final PatternChoice other = (PatternChoice) obj;
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
	 * {@inheritDoc} Represents each choice in the form ( [definition] ) / (
	 * [definition] )
	 */
	@Override
	public String getDefinition(final boolean component) {

		// Use StringBuilder to compile definition
		final StringBuilder definition = new StringBuilder();

		// Loop over all patterns we have
		for (final Pattern p : patterns) {

			// Append grouped sub-definition
			definition.append("(");
			definition.append(p.getDefinition(true));
			definition.append(")");

			// If non-final element, append ordered choice operator
			if (p != patterns.get(patterns.size() - 1)) {
				definition.append(" / ");
			}
		}

		// Return final definition string
		return definition.toString();
	}

	/**
	 * {@inheritDoc} Returns a copied list of all ordered choices in this pattern.
	 */
	@Override
	public List<Pattern> getPatternComponents() {
		return new ArrayList<>(patterns);
	}

	/**
	 * {@inheritDoc} Returns a copied list of all ordered choices in this pattern.
	 */
	@Override
	protected Iterator<Pattern> getPossibleLeftmostComponents() {
		return new ArrayList<>(patterns).iterator();
	}

	/**
	 * {@inheritDoc} Returns true if any of its choices are nullable.
	 */
	@Override
	public boolean isNullable() {
		// Iterate over our choices
		for (final Pattern choice : patterns) {
			// If this pattern is nullable
			if (choice.isNullable()) {
				// The overall pattern is nullable
				return true;
			}
		}
		// If none of our choices are nullable, then we aren't either.
		return false;
	}

}
