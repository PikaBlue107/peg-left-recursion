/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.definition;

import java.util.Iterator;
import java.util.List;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.structure.InputContext;
import edu.ncsu.csc499.peg_lr.structure.Result;

/**
 * @author Melody Griesen
 *
 */
public abstract class DefinedPattern extends Pattern {

	/**
	 * Implements the match() method by requiring a Pattern definition to be
	 * provided.
	 */
	@Override
	protected Result match(final InputContext context) {
		// Delegate to the pattern we created.
		return getPattern().lazyMatch(context);
	}

	/**
	 * Requires all subclasses to simply provide a Pattern definition.
	 * 
	 * @return the Pattern that shall be used to determine matching.
	 */
	protected abstract Pattern getPattern();

	/**
	 * {@inheritDoc} If component, returns pattern type. If non-component, delegates
	 * to inner pattern's definition.
	 * 
	 * @param component whether this pattern is a component of another pattern
	 *                  definition
	 * @return a string indicating what the Pattern matches
	 */
	@Override
	public String getDefinition(final boolean component) {
		// If component of another definition
		if (component) {
			// Return this defined pattern's name
			return getType();
		}
		// If this pattern is the top-level definition
		else {
			// Return the full definition from the pattern stored
			return getPattern().getDefinition(true);
		}
	}

	/**
	 * {@inheritDoc} Returns the definition for this DefinedPattern.
	 */
	@Override
	public List<Pattern> getPatternComponents() {
		return List.of(getPattern());
	}

	/**
	 * {@inheritDoc} Returns the definition for this DefinedPattern.
	 */
	@Override
	protected Iterator<Pattern> getPossibleLeftmostComponents() {
		return List.of(getPattern()).iterator();
	}

	/**
	 * {@inheritDoc} Delegates to this pattern's definition for nullability.
	 */
	@Override
	public boolean isNullable() {
		return getPattern().isNullable();
	}

	/**
	 * Implements hashCode to compare based on ID, as definitions should *always* be
	 * singly created.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		final int hash = (prime * super.hashCode()) + super.getID();
		return hash;
	}

	/**
	 * Definitions are always compared by instance and ID.
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
		final DefinedPattern other = (DefinedPattern) obj;
		if (this.getID() != other.getID()) {
			return false;
		}
		return true;
	}

}
