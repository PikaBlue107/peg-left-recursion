package patterns.definition;

import patterns.general.Pattern;
import patterns.general.PatternChoice;
import patterns.general.PatternSequence;
import patterns.general.PatternString;

/**
 * expr <- { expr + num / num }
 * 
 * @author Melody Griesen
 *
 */
public class DefinitionExpression extends PatternDefinition {

	/** Internal definition used when matching. */
	private final Pattern pattern = new PatternChoice(
			new PatternSequence(this, new PatternString("+"), new DefinitionNumber()),
			new DefinitionNumber()
			);

	/**
	 * Returns the pattern we create in Expression.
	 */
	@Override
	protected Pattern getDefinition() {
		return pattern;
	}

}
