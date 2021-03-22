package edu.ncsu.csc499.peg_lr.pattern.definition;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternChoice;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternSequence;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternString;

/**
 * expr <- { expr + num / num }
 * 
 * @author Melody Griesen
 *
 */
public class DefinedRightRecursiveExpression extends DefinedPattern {

	/** Internal definition used when matching. */
	private final Pattern pattern = new PatternChoice(new PatternSequence(this, new PatternString("+"), this),
			new DefinedNumber());
	/** Pattern type to provide the display or reference name. */
	private static final String TYPE = "RightRecursiveExpression";

	@Override
	protected Pattern getPattern() {
		return pattern;
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
