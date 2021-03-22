/**
 * 
 */
package patterns.definition;

import static util.PatternTestUtils.showExample;

import org.junit.Before;
import org.junit.Test;

import pattern.Pattern;
import pattern.definition.DefinedNumber;
import util.PatternTestUtils;

/**
 * @author Melody Griesen
 *
 */
public class DefinitionNumberTest {

	private static final Pattern PATTERN = new DefinedNumber();

	@Test
	public void showExampleLeftRecursion() {
		showExample(PATTERN, "1", "Valid - Simple Number");
		showExample(PATTERN, "65535", "Valid - Complex Number");
		showExample(PATTERN, "12a", "Valid - Partial number, invalid text");
		showExample(PATTERN, "a", "Invalid - Just not a number");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testValidMatches() {
		PatternTestUtils.assertMatches(PATTERN, "1");
		PatternTestUtils.assertMatches(PATTERN, "12345");
		PatternTestUtils.assertMatches(PATTERN, "1230190123981");
	}

	@Test
	public void testRejectsEmpty() {
		PatternTestUtils.assertRejects(PATTERN, "");
		PatternTestUtils.assertRejects(PATTERN, "a");
		PatternTestUtils.assertRejects(PATTERN, " ");
		PatternTestUtils.assertRejects(PATTERN, "-");
	}

}
