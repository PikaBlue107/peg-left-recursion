/**
 * 
 */
package patterns.definition;

import patterns.general.Pattern;
import structure.InputContext;
import structure.Result;

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
