/**
 * 
 */
package patterns.definition;

import org.junit.Test;

import patterns.Pattern;
import util.PatternTestUtils;

/**
 * @author Melody Griesen
 *
 */
public class SimpleExpressionTest {

	private static final Pattern PATTERN = new SimpleExpression();

	@Test
	public void testMatchesNumber() {
		// Matches number
		PatternTestUtils.assertMatches(PATTERN, "1");
		PatternTestUtils.assertMatches(PATTERN, "12345");
		PatternTestUtils.assertMatches(PATTERN, "1230190123981");
	}

	@Test
	public void testMatchesExpression() {
		// Matches expressions
		PatternTestUtils.assertMatches(PATTERN, "1+1");
		PatternTestUtils.assertMatches(PATTERN, "598+29382");
		PatternTestUtils.assertMatches(PATTERN, "1+2+34+567");
	}

	@Test
	public void testRejects() {
		PatternTestUtils.assertRejects(PATTERN, "");
		PatternTestUtils.assertRejects(PATTERN, "a");
		PatternTestUtils.assertRejects(PATTERN, " ");
		PatternTestUtils.assertRejects(PATTERN, "-");
		PatternTestUtils.assertRejects(PATTERN, "+5");
	}

}
