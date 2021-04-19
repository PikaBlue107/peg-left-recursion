/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.builder;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.definition.DefinedPattern;

/**
 * Offers static methods to interpret strings with the Rosie RPL syntax as
 * Patterns, and return them.
 * 
 * @author Melody Griesen
 *
 */
public class PatternBuilder {

	/**
	 * Interprets a full Pattern definition of the form:
	 * 
	 * "Number = [:digit:]+"
	 * 
	 * Parses this definition as a DefinedPattern, saving the resulting Pattern
	 * inside.
	 * 
	 * @param pattern the pattern definition string to parse and construct
	 * @return the DefinedPattern representing the provided pattern definition
	 */
	public static DefinedPattern compileDefinition(final String pattern) {
		return null;
	}

	/**
	 * Interprets a pattern string as the Pattern it represents, transforming a
	 * string like:
	 * 
	 * "[:digit:]+"
	 * 
	 * into a PatternRepetition owning a PatternDigit.
	 * 
	 * @param pattern the pattern string to parse and construct
	 * @return the Pattern that the input string describes
	 */
	public static Pattern compilePattern(final String pattern) {
		return null;
	}

}
