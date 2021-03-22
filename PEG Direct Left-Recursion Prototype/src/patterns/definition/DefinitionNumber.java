package patterns.definition;

import patterns.Pattern;
import patterns.general.PatternDigit;
import patterns.general.PatternRepetition;

public class DefinitionNumber extends DefinedPattern {

	/** Pattern definition states that a number is one or more Digits. */
	private static final PatternRepetition NUMBER = new PatternRepetition(new PatternDigit(), 1, -1);

	/** Pattern type to indicate display and reference name. */
	private static final String TYPE = "Number";

	@Override
	protected Pattern getPattern() {
		return NUMBER;
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
