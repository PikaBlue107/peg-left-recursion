/**
 * 
 */
package patterns.general;

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
	 * Signals to Pattern that this object should not be considered for memoization
	 * or produce Results that should be printed in the output tree.
	 */
	@Override
	public String getType() {
		return null;
	}

}
