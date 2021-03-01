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
public abstract class PatternDefinition extends Pattern {

	/**
	 * Implements the match() method by requiring a Pattern definition to be
	 * provided.
	 */
	@Override
	protected Result match(final InputContext context) {
		// Delegate to the pattern we created.
		return getDefinition().lazyMatch(context);
	}

	/**
	 * Requires all subclasses to simply provide a Pattern definition.
	 * 
	 * @return the Pattern that shall be used to determine matching.
	 */
	protected abstract Pattern getDefinition();

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
		final PatternDefinition other = (PatternDefinition) obj;
		if (this.getID() != other.getID()) {
			return false;
		}
		return true;
	}

}
