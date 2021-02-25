/**
 * 
 */
package patterns.definition;

import org.junit.Test;

import patterns.general.Pattern;
import util.PatternMatchingTestUtils;

/**
 * @author Melody Griesen
 *
 */
public class SimpleExpressionTest {

	private static final Pattern PATTERN = new SimpleExpression();

	@Test
	public void testMatchesNumber() {
		// Matches number
		PatternMatchingTestUtils.assertPatternMatches(PATTERN, "1");
		PatternMatchingTestUtils.assertPatternMatches(PATTERN, "12345");
		PatternMatchingTestUtils.assertPatternMatches(PATTERN, "1230190123981");
	}

	@Test
	public void testMatchesExpression() {
		// Matches expressions
		PatternMatchingTestUtils.assertPatternMatches(PATTERN, "1+1");
		PatternMatchingTestUtils.assertPatternMatches(PATTERN, "598+29382");
		PatternMatchingTestUtils.assertPatternMatches(PATTERN, "1+2+34+567");
	}

	@Test
	public void testRejects() {
		PatternMatchingTestUtils.assertPatternRejects(PATTERN, "");
		PatternMatchingTestUtils.assertPatternRejects(PATTERN, "a");
		PatternMatchingTestUtils.assertPatternRejects(PATTERN, " ");
		PatternMatchingTestUtils.assertPatternRejects(PATTERN, "-");
		PatternMatchingTestUtils.assertPatternRejects(PATTERN, "+5");
	}

}
