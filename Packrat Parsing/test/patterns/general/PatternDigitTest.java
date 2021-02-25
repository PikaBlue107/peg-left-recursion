package patterns.general;

import org.junit.Test;

import util.PatternTestUtils;

public class PatternDigitTest {

	private static final Pattern PATTERN = new PatternDigit();

	@Test
	public void testAccepts() {
		for (int i = 0; i < 10; i++) {
			PatternTestUtils.assertMatches(PATTERN, String.valueOf(i));
		}
	}

	@Test
	public void testRejects() {
		PatternTestUtils.assertRejects(PATTERN, "");
		PatternTestUtils.assertRejects(PATTERN, "a");
		PatternTestUtils.assertRejects(PATTERN, "Z");
		PatternTestUtils.assertRejects(PATTERN, "-");
		PatternTestUtils.assertRejects(PATTERN, "_");
		PatternTestUtils.assertRejects(PATTERN, "zero");
		PatternTestUtils.assertRejects(PATTERN, "+");
		PatternTestUtils.assertRejects(PATTERN, "!");
	}

}
