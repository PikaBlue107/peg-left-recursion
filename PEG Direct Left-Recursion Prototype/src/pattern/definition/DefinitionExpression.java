package pattern.definition;

import pattern.Pattern;
import pattern.component.PatternChoice;
import pattern.component.PatternSequence;
import pattern.component.PatternString;

/**
 * expr <- { expr + num / num }
 * 
 * @author Melody Griesen
 *
 */
public class DefinitionExpression extends DefinedPattern {

	/** Internal definition used when matching. */
	private final Pattern pattern = new PatternChoice(
			new PatternSequence(this, new PatternString("+"), new DefinitionNumber()), new DefinitionNumber());
	/** Pattern type to provide the display or reference name. */
	private static final String TYPE = "Expression";

	@Override
	protected Pattern getPattern() {
		return pattern;
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
