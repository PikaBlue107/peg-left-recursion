package patterns.definition;

import patterns.general.Pattern;
import patterns.general.PatternDigit;
import patterns.general.PatternRepetition;

public class DefinitionNumber extends PatternDefinition {

	/** Pattern definition states that a number is one or more Digits. */
	private static final PatternRepetition number = new PatternRepetition(new PatternDigit(), 1, -1);

	/**
	 * Returns the pattern we define.
	 */
	@Override
	protected Pattern getDefinition() {
		return number;
	}

}
