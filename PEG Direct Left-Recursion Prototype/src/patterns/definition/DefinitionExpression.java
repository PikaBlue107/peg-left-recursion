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
			new PatternSequence(this, new PatternString("+"), new DefinitionNumber()), new DefinitionNumber());
	/** Pattern type to provide the display or reference name. */
	private static final String TYPE = "Expression";

	@Override
	protected Pattern getDefinition() {
		return pattern;
	}

	@Override
	public String getType() {
		return TYPE;
	}

}