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
public class PatternSequenceTest {

	Pattern pattern;

	private static final Pattern one = new PatternString("one");
	private static final Pattern two = new PatternDigit();
	private static final Pattern three = new PatternString("three");

	private static final Pattern PAT_NULLABLE = new PatternString("");
	private static final Pattern PAT_NON_NULLABLE = new PatternString("asdf");

	@Before
	public void setup() {
		pattern = new PatternSequence(one, two, three);
	}

	@Test
	public void testAccepts() {
		PatternTestUtils.assertMatches(pattern, "one2three");
		PatternTestUtils.assertMatches(pattern, "one5three");
		PatternTestUtils.assertMatches(pattern, "one0three");
	}

	@Test
	public void testRejects() {
		PatternTestUtils.assertRejects(pattern, "onetwothree");
		PatternTestUtils.assertRejects(pattern, "one5thre");
		PatternTestUtils.assertRejects(pattern, "2three");
		PatternTestUtils.assertRejects(pattern, "onethree");
		PatternTestUtils.assertRejects(pattern, "one2");
	}

	@Test
	public void testNullable() {

		// If no sequence elements are nullable, the sequence isn't nullable
		Assert.assertFalse(new PatternSequence(PAT_NON_NULLABLE, PAT_NON_NULLABLE).isNullable());

		// If the first sequence element is nullable, the sequence isn't nullable
		Assert.assertFalse(new PatternSequence(PAT_NULLABLE, PAT_NON_NULLABLE).isNullable());

		// If the second sequence element is nullable, the sequence isn't nullable
		Assert.assertFalse(new PatternSequence(PAT_NON_NULLABLE, PAT_NULLABLE).isNullable());

		// if both sequence elements are nullable, the sequence is nullable
		Assert.assertTrue(new PatternSequence(PAT_NULLABLE, PAT_NULLABLE).isNullable());
	}

	@Test
	public void testLeftRecursive() {
		// Only those sequence elements where all leftward elements are nullable can be
		// left-recursive.

		// First element can hold LR
		final Pattern firstElemLR = new DefinedPattern() {

			private final Pattern pat = new PatternSequence(this, PAT_NON_NULLABLE);

			@Override
			protected Pattern getPattern() {
				return pat;
			}

			@Override
			public String getType() {
				return "TestFirstElemLR";
			}

		};
		Assert.assertTrue(firstElemLR.isLeftRecursive());

		// If first element is non-nullable, second element shouldn't be left-recursive
		final Pattern firstNonNullableSecondRecursive = new DefinedPattern() {

			private final Pattern pat = new PatternSequence(PAT_NON_NULLABLE, this);

			@Override
			protected Pattern getPattern() {
				return pat;
			}

			@Override
			public String getType() {
				return "TestFirstNonNullableSecondRecursive";
			}

		};
		Assert.assertFalse(firstNonNullableSecondRecursive.isLeftRecursive());

		// If first element is nullable, second element should be left-recursive
		final Pattern firstNullableSecondRecursive = new DefinedPattern() {

			private final Pattern pat = new PatternSequence(PAT_NULLABLE, this);

			@Override
			protected Pattern getPattern() {
				return pat;
			}

			@Override
			public String getType() {
				return "TestFirstNullableSecondRecursive";
			}

		};
		Assert.assertTrue(firstNullableSecondRecursive.isLeftRecursive());

		// If first and second elements are non-nullable, third element shouldn't be
		// left-recursive
		final Pattern firstTwoNonNullableThirdRecursive = new DefinedPattern() {

			private final Pattern pat = new PatternSequence(PAT_NON_NULLABLE, PAT_NON_NULLABLE, this);

			@Override
			protected Pattern getPattern() {
				return pat;
			}

			@Override
			public String getType() {
				return "TestFirstTwoNonNullableThirdRecursive";
			}

		};
		Assert.assertFalse(firstTwoNonNullableThirdRecursive.isLeftRecursive());

		// If first is nullable, second is non-nullable, then third should not be
		// left-recursive
		final Pattern firstTwoNullableThenNonNullableThirdRecursive = new DefinedPattern() {

			private final Pattern pat = new PatternSequence(PAT_NULLABLE, PAT_NON_NULLABLE, this);

			@Override
			protected Pattern getPattern() {
				return pat;
			}

			@Override
			public String getType() {
				return "TestFirstTwoNonNullableThirdRecursive";
			}

		};
		Assert.assertFalse(firstTwoNullableThenNonNullableThirdRecursive.isLeftRecursive());

		// If first is non-nullable, second is nullable, then third should not be
		// left-recursive
		final Pattern firstTwoNonNullableThenNullableThirdRecursive = new DefinedPattern() {

			private final Pattern pat = new PatternSequence(PAT_NON_NULLABLE, PAT_NULLABLE, this);

			@Override
			protected Pattern getPattern() {
				return pat;
			}

			@Override
			public String getType() {
				return "TestFirstTwoNonNullableThenNullableThirdRecursive";
			}

		};
		Assert.assertFalse(firstTwoNonNullableThenNullableThirdRecursive.isLeftRecursive());

		// If first is nullable, second is non-nullable, then third should not be
		// left-recursive
		final Pattern firstTwoNullableThirdRecursive = new DefinedPattern() {

			private final Pattern pat = new PatternSequence(PAT_NULLABLE, PAT_NULLABLE, this);

			@Override
			protected Pattern getPattern() {
				return pat;
			}

			@Override
			public String getType() {
				return "TestFirstTwoNullableThirdRecursive";
			}

		};
		Assert.assertTrue(firstTwoNullableThirdRecursive.isLeftRecursive());

		// The pattern isn't left recursive on its own
		Assert.assertFalse(new PatternSequence(new PatternDigit(), new PatternString("")).isLeftRecursive());
		final Pattern notLRDefinition = new DefinedPattern() {

			private final Pattern pat = new PatternSequence(new PatternDigit(), new PatternString(""));

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
