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
public class DefinedExpression extends DefinedPattern {

	/** Pattern type to provide the display or reference name. */
	private static final String TYPE = "Expression";

	/** Internal definition used when matching. */
	private final Pattern pattern = new PatternChoice(
			new PatternSequence(this, new PatternString("+"), new DefinedNumber()), new DefinedNumber());

	/**
	 * Expression constructor that provides the type and pattern definition.
	 */
	public DefinedExpression() {
		super(TYPE);
		super.setDefinition(pattern);
	}

}
