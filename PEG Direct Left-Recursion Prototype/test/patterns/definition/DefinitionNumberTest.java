/**
 * 
 */
package patterns.definition;

import org.junit.Before;
import org.junit.Test;

import patterns.general.Pattern;
import util.PatternTestUtils;

/**
 * @author Melody Griesen
 *
 */
public class DefinitionNumberTest {

	private static final Pattern PATTERN = new DefinitionNumber();

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
