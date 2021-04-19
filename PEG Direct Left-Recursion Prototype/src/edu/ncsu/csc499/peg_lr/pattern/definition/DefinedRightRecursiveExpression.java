package edu.ncsu.csc499.peg_lr.pattern.definition;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternString;
import edu.ncsu.csc499.peg_lr.pattern.component.operator.PatternChoice;
import edu.ncsu.csc499.peg_lr.pattern.component.operator.PatternSequence;

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

	/**
	 * Instantiates a RightRecursiveExpression with its pattern type name and
	 * definition
	 */
	protected DefinedRightRecursiveExpression() {
		super(TYPE);
		super.setDefinition(pattern);
	}

}
