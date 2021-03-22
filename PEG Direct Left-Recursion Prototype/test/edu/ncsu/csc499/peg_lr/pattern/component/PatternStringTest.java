/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.component;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternString;
import edu.ncsu.csc499.peg_lr.util.PatternTestUtils;

/**
 * @author Melody Griesen
 *
 */
public class PatternStringTest {

	private static final String TARGET_STRING = "Hello, World!";

	private Pattern pattern;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Sets up each test to default to a pattern that matches the target string
		// given above
		pattern = new PatternString(TARGET_STRING);
	}

	@Test
	public void testAcceptRegular() {
		PatternTestUtils.assertMatches(pattern, TARGET_STRING);
		PatternTestUtils.assertMatchesPrefix(pattern, TARGET_STRING);
		PatternTestUtils.assertMatchesPrefix(pattern, TARGET_STRING + "asdf");
	}

	@Test
	public void testAcceptEmpty() {
		// Re-define pattern to match empty string
		pattern = new PatternString("");

		PatternTestUtils.assertMatches(pattern, "");
		PatternTestUtils.assertMatchesPrefix(pattern, TARGET_STRING);
	}

	@Test
	public void testRejects() {
		PatternTestUtils.assertRejects(pattern, "");
		PatternTestUtils.assertRejects(pattern, "asdf");
		PatternTestUtils.assertRejects(pattern, TARGET_STRING.substring(0, TARGET_STRING.length() - 1));
	}

}
