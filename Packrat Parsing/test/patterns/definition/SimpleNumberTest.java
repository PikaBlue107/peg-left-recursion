/**
 * 
 */
package patterns.definition;

import org.junit.Before;
import org.junit.Test;

import patterns.general.Pattern;
import util.PatternMatchingTestUtils;

/**
 * @author Melody Griesen
 *
 */
public class SimpleNumberTest {

	private static final Pattern PATTERN = new SimpleNumber();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testValidMatches() {
		PatternMatchingTestUtils.assertPatternMatches(PATTERN, "1");
		PatternMatchingTestUtils.assertPatternMatches(PATTERN, "12345");
		PatternMatchingTestUtils.assertPatternMatches(PATTERN, "1230190123981");
	}

	@Test
	public void testRejectsEmpty() {
		PatternMatchingTestUtils.assertPatternRejects(PATTERN, "");
		PatternMatchingTestUtils.assertPatternRejects(PATTERN, "a");
		PatternMatchingTestUtils.assertPatternRejects(PATTERN, " ");
		PatternMatchingTestUtils.assertPatternRejects(PATTERN, "-");
	}

}
