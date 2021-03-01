package patterns.definition;

import patterns.general.Pattern;
import patterns.general.PatternDigit;
import patterns.general.PatternRepetition;

public class DefinitionNumber extends PatternDefinition {

	/** Pattern definition states that a number is one or more Digits. */
	private static final PatternRepetition NUMBER = new PatternRepetition(new PatternDigit(), 1, -1);

	/** Pattern type to indicate display and reference name. */
	private static final String TYPE = "Number";

	@Override
	protected Pattern getDefinition() {
		return NUMBER;
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
