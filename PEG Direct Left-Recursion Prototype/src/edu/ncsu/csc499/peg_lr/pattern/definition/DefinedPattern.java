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
public class DefinedPattern extends Pattern {

	/** Internal definition used when matching. */
	private Pattern definition;

	/** Pattern type to provide the display or reference name. */
	private String type;

	/**
	 * Creates a DefinedPattern with the given pattern definition and type name.
	 *
	 * @param type       the type name of the Pattern being defined
	 * @param definition the internal Pattern that will be used upon matching
	 */
	public DefinedPattern(final String type, final Pattern definition) {
		setDefinition(definition);
		setType(type);
	}

	/**
	 * Internal constructor that allows a user to skip providing a pattern. This
	 * allows them to instantiate and then set a recursive pattern if so desired.
	 *
	 * @param type the type name of the Pattern being defined
	 */
	protected DefinedPattern(final String type) {
		setType(type);
	}

	/**
	 * Implements the match() method by requiring a Pattern definition to be
	 * provided.
	 */
	@Override
	protected Result match(final InputContext context) {
		// Create a Result for the pattern we'll match
		final Result overallResult = new Result(context.getPosition());
		// Delegate to the pattern we created.
		final Result definitionResult = getPattern().lazyMatch(context);
		// If it was successful
		if (definitionResult.isSuccess()) {
			// Add that result to our own
			overallResult.addChild(definitionResult);
			// Return the overall result for this definition
			return overallResult;
		}
		// Otherwise, it wasn't successful
		else {
			// Just return the definition match
			return definitionResult;
		}
	}

	/**
	 * Sets this Pattern's type.
	 *
	 * @param type the type to set. Cannot be null or blank.
	 */
	protected void setType(final String type) {
		if ((type == null) || type.isBlank()) {
			throw new IllegalArgumentException("Pattern type cannot be null or empty.");
		}
		this.type = type;
	}

	/**
	 * Sets this Pattern's definition, the pattern that will be used for its
	 * matching.
	 *
	 * @param definition the pattern that this one will delegate to upon matching
	 */
	protected void setDefinition(final Pattern definition) {
		if (definition == null) {
			throw new IllegalArgumentException("Pattern definition cannot be null.");
		}
		this.definition = definition;
	}

	/**
	 * Retrieves this Pattern's type
	 * 
	 * @return the type name of this Pattern.
	 */
	@Override
	public String getType() {
		return type;
	}

	/**
	 * Provides access to the pattern definition used for matching.
	 * 
	 * @return the Pattern that shall be used to determine matching.
	 */
	public Pattern getPattern() {
		return definition;
	}

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
