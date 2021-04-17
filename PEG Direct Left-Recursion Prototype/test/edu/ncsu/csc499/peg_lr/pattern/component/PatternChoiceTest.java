/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.component;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.definition.DefinedPattern;
import edu.ncsu.csc499.peg_lr.util.PatternTestUtils;

/**
 * @author Melody Griesen
 *
 */
public class PatternChoiceTest {

	Pattern pattern;

	private static final Pattern one = new PatternString("one");
	private static final Pattern two = new PatternDigit();
	private static final Pattern three = new PatternString("three");

	@Before
	public void setup() {
		pattern = new PatternChoice(one, two, three);
	}

	@Test
	public void testAcceptChoice() {
		// Ensure it can match any choice
		PatternTestUtils.assertMatches(pattern, "one");
		for (int i = 0; i < 10; i++) {
			PatternTestUtils.assertMatches(pattern, String.valueOf(i));
		}
		PatternTestUtils.assertMatches(pattern, "three");
	}

	@Test
	public void testAcceptEmpty() {
		// Define PatternChoice with an extra, 4th ordered choice as epsilon (the empty
		// string)
		pattern = new PatternChoice(one, two, three, new PatternString(""));

		// Ensure the first 3 patterns will be prioritized (they will be consumed if
		// present)
		PatternTestUtils.assertMatches(pattern, "one");
		for (int i = 0; i < 10; i++) {
			PatternTestUtils.assertMatches(pattern, String.valueOf(i));
		}
		PatternTestUtils.assertMatches(pattern, "three");

		// Ensure it matches exactly the empty string
		PatternTestUtils.assertMatches(pattern, "");

		// Ensure it accepts prefixes to anything that doesn't exactly match
		PatternTestUtils.assertMatchesPrefix(pattern, "only");
		PatternTestUtils.assertMatchesPrefix(pattern, "different");
		PatternTestUtils.assertMatchesPrefix(pattern, "-1982");
	}

	@Test
	public void testAcceptNearPrefix() {
		// Define PatternChoice with two choices, both starting with "on"
		pattern = new PatternChoice(one, new PatternString("only"));

		// Ensure both are matchable, and "on" is rejected
		PatternTestUtils.assertMatches(pattern, "one");
		PatternTestUtils.assertMatches(pattern, "only");
		PatternTestUtils.assertRejects(pattern, "on");
	}

	@Test
	public void testExactPrefix() {
		// Define PatternChoice with two choices where the second is an extension of the
		// first
		pattern = new PatternChoice(new PatternString("on"), new PatternString("only"));

		// Ensure first is matchable and second is not
		PatternTestUtils.assertMatches(pattern, "on");
		// No clean way to test for ensuring that it *only* matches a prefix
		// TODO: Make a better way to test for explicit prefixes
//		PatternTestUtils.assertRejects(pattern, "only");
	}

	@Test
	public void testReject() {
		PatternTestUtils.assertRejects(pattern, "");
		PatternTestUtils.assertRejects(pattern, "on");
		PatternTestUtils.assertRejects(pattern, "thre");
	}

	@Test
	public void testNullable() {
		// A choice of 2 non-nullable elements is itself non-nullable
		Assert.assertFalse(new PatternChoice(new PatternString("a"), new PatternDigit()).isNullable());

		// A choice of 1 nullable and 1 non-nullable element is nullable
		Assert.assertTrue(new PatternChoice(new PatternString(""), new PatternDigit()).isNullable());
		Assert.assertTrue(new PatternChoice(new PatternDigit(), new PatternString("")).isNullable());

		// A choice of 2 nullable elements is itself nullable
		Assert.assertTrue(new PatternChoice(new PatternString(""), new PatternString("")).isNullable());
	}

	@Test
	public void testLeftRecursive() {
		// Any choice should be valid to hold an instance of left recursion

		// First choice should hold left recursion
		final Pattern firstChoiceLR = new DefinedPattern() {

			private final Pattern pat = new PatternChoice(this, new PatternDigit());

			@Override
			protected Pattern getPattern() {
				return pat;
			}

			@Override
			public String getType() {
				return "TestFirstChoiceLR";
			}

		};
		Assert.assertTrue(firstChoiceLR.isLeftRecursive());

		// Second choice should also hold left recursion
		final Pattern secondChoiceLR = new DefinedPattern() {

			private final Pattern pat = new PatternChoice(new PatternDigit(), this);

			@Override
			protected Pattern getPattern() {
				return pat;
			}

			@Override
			public String getType() {
				return "TestSecondChoiceLR";
			}

		};
		Assert.assertTrue(secondChoiceLR.isLeftRecursive());

		// The pattern isn't left recursive on its own
		Assert.assertFalse(new PatternChoice(new PatternDigit(), new PatternString("")).isLeftRecursive());
		final Pattern notLRDefinition = new DefinedPattern() {

			private final Pattern pat = new PatternChoice(new PatternDigit(), new PatternString(""));

			@Override
			public String getType() {
				return "TestNotLRDefinition";
			}

			@Override
			protected Pattern getPattern() {
				return pat;
			}
		};
		Assert.assertFalse(notLRDefinition.isLeftRecursive());
	}
}
