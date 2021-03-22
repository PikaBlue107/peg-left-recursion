/**
 * 
 */
package pattern.component;

import pattern.Pattern;

/**
 * Allows for components of Patterns that skip memoization and result printing
 * to be built by extending from this class.
 * 
 * This allows for component classes that implement operations like
 * alternations, sequences, predicates, etc. without interfering with direct
 * left recursion or printing extra entries to the Results tree.
 * 
 * @author Melody Griesen
 *
 */
public abstract class PatternComponent extends Pattern {

	/**
	 * Declares all Pattern Components to be aliases, thus having a name but being
	 * filtered from the main result display.
	 */
	@Override
	public boolean isAlias() {
		return true;
	}

	/**
	 * Signals to Pattern that this object should not be considered for memoization
	 * or produce Results that should be printed in the output tree.
	 */
	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}

}
