/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.component;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.component.operator.PatternChoice;
import edu.ncsu.csc499.peg_lr.pattern.component.operator.PatternRepetition;
import edu.ncsu.csc499.peg_lr.pattern.definition.DefinedPattern;
import edu.ncsu.csc499.peg_lr.util.PatternTestUtils;

/**
 * @author Melody Griesen
 *
 */
public class PatternRepetitionTest {

	Pattern pattern;

	private static final Pattern digit = new PatternDigit();

	@Test
	public void testData() {

		// Test improper calls
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new PatternRepetition(null, 0, 1);
		});
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new PatternRepetition(digit, -1, 1);
		});
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new PatternRepetition(digit, 0, -2);
		});

	}

	@Test
	public void testStar() {
		// Pattern allows 0-infinity matches
		pattern = new PatternRepetition(digit, 0, -1);

		PatternTestUtils.assertMatches(pattern, "");
		PatternTestUtils.assertMatches(pattern, "1");
		PatternTestUtils.assertMatches(pattern, "12");
		PatternTestUtils.assertMatches(pattern, "123");
		PatternTestUtils.assertMatches(pattern, "1234");
	}

	@Test
	public void testPlus() {
		// Pattern allows 1-infinity matches
		pattern = new PatternRepetition(digit, 1, -1);

		PatternTestUtils.assertRejects(pattern, "");
		PatternTestUtils.assertMatches(pattern, "1");
		PatternTestUtils.assertMatches(pattern, "12");
		PatternTestUtils.assertMatches(pattern, "123");
		PatternTestUtils.assertMatches(pattern, "1234");

	}

	@Test
	public void testRange() {
		// Pattern allows 1-3 matches
		pattern = new PatternRepetition(digit, 1, 3);

		PatternTestUtils.assertRejects(pattern, "");
		PatternTestUtils.assertMatches(pattern, "1");
		PatternTestUtils.assertMatches(pattern, "12");
		PatternTestUtils.assertMatches(pattern, "123");
		PatternTestUtils.assertMatchesPrefix(pattern, "1234");

	}

	@Test
	public void testExact() {
		// Pattern requires exactly 3 repetitions
		pattern = new PatternRepetition(digit, 3, 3);

		PatternTestUtils.assertRejects(pattern, "");
		PatternTestUtils.assertRejects(pattern, "1");
		PatternTestUtils.assertRejects(pattern, "12");
		PatternTestUtils.assertMatches(pattern, "123");
		PatternTestUtils.assertMatchesPrefix(pattern, "1234");

	}

	@Test
	public void testNullable() {
		// A repetition with a non-nullable pattern is only nullable if it allows 0
		// repetitions
		Assert.assertFalse(new PatternRepetition(new PatternString("a"), 1, -1).isNullable());
		Assert.assertFalse(new PatternRepetition(new PatternString("a"), 1, 3).isNullable());
		Assert.assertFalse(new PatternRepetition(new PatternString("a"), 1, 1).isNullable());
		Assert.assertTrue(new PatternRepetition(new PatternString("a"), 0, 1).isNullable());

		// A repetition with a nullable pattern is always nullable
		Assert.assertTrue(new PatternRepetition(new PatternString(""), 1, 3).isNullable());
		Assert.assertTrue(new PatternRepetition(new PatternString(""), 1, 1).isNullable());
		Assert.assertTrue(new PatternRepetition(new PatternString(""), 0, 1).isNullable());
	}

	@Test
	public void testLeftRecursive() {
		// Any choice should be valid to hold an instance of left recursion

		// First choice should hold left recursion
		final Pattern firstChoiceLR = new DefinedPattern("TestFirstChoiceLR") {

			private final Pattern pat = new PatternChoice(this, new PatternDigit());
			{
				super.setDefinition(pat);
			}

		};
		Assert.assertTrue(firstChoiceLR.isLeftRecursive());

		// Second choice should also hold left recursion
		final Pattern secondChoiceLR = new DefinedPattern("TestSecondChoiceLR") {

			private final Pattern pat = new PatternChoice(new PatternDigit(), this);
			{
				super.setDefinition(pat);
			}

		};
		Assert.assertTrue(secondChoiceLR.isLeftRecursive());

		// The pattern isn't left recursive on its own
		Assert.assertFalse(new PatternRepetition(new PatternString(""), 0, 1).isLeftRecursive());
		final Pattern notLRDefinition = new DefinedPattern("TestNotLRDefinition",
				new PatternRepetition(new PatternString("a"), 1, -1));
		Assert.assertFalse(notLRDefinition.isLeftRecursive());
	}

}
