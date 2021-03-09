/**
 * 
 */
package patterns.definition;

import static util.PatternTestUtils.assertMatches;
import static util.PatternTestUtils.assertRejects;
import static util.PatternTestUtils.showExample;

import org.junit.Test;

import patterns.general.Pattern;

/**
 * @author Melody Griesen
 *
 */
public class DefinitionExpressionTest {

	private static final Pattern PATTERN = new DefinitionExpression();

	@Test
	public void showExampleLeftRecursion() {
		showExample(PATTERN, "1+2", "Simple Expression");
		showExample(PATTERN, "a", "Just not an Expression");
		showExample(PATTERN, "1+a", "Partial expression, invalid text");
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

}
