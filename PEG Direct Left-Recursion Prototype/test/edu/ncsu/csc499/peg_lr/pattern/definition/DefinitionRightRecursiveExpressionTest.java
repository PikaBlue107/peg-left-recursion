/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.definition;

import static edu.ncsu.csc499.peg_lr.util.PatternTestUtils.assertMatches;
import static edu.ncsu.csc499.peg_lr.util.PatternTestUtils.assertRejects;
import static edu.ncsu.csc499.peg_lr.util.PatternTestUtils.showExample;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;

/**
 * @author Melody Griesen
 *
 */
public class DefinitionRightRecursiveExpressionTest {

	private static final Pattern PATTERN = new DefinedRightRecursiveExpression();

	@Test
	public void showExampleLeftRecursion() {
		showExample(PATTERN, "1+2", "Valid - Simple Expression");
		showExample(PATTERN, "8+13+217", "Valid - Complex expression");
		showExample(PATTERN, "a", "Invalid - Just not an Expression");
		showExample(PATTERN, "1+a", "Invalid - Partial expression, invalid text");
	}

	@Test
	public void testMatchesNumber() {
		// Matches number
		assertMatches(PATTERN, "1");
		assertMatches(PATTERN, "12345");
		assertMatches(PATTERN, "1230190123981");
	}

	@Test
	public void testMatchesExpression() {
		// Matches expressions
		assertMatches(PATTERN, "1+1");
		assertMatches(PATTERN, "598+29382");
		assertMatches(PATTERN, "1+2+34+567");
	}

	@Test
	public void testRejects() {
		assertRejects(PATTERN, "");
		assertRejects(PATTERN, "a");
		assertRejects(PATTERN, " ");
		assertRejects(PATTERN, "-");
		assertRejects(PATTERN, "+5");
	}

	@Test
	public void testProperties() {
		Assert.assertTrue(PATTERN.isLeftRecursive());
		Assert.assertFalse(PATTERN.isNullable());
		Assert.assertFalse(PATTERN.isInvisible());
	}

}
