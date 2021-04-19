package edu.ncsu.csc499.peg_lr.pattern.component;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.component.charset.PatternDigit;
import edu.ncsu.csc499.peg_lr.util.PatternTestUtils;

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

	@Test
	public void testProperties() {
		Assert.assertFalse(PATTERN.isNullable());
		Assert.assertTrue(PATTERN.isHidden());
		Assert.assertFalse(PATTERN.isLeftRecursive());
	}

}
