/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.component;

import static edu.ncsu.csc499.peg_lr.util.PatternTestUtils.assertMatchesExact;
import static edu.ncsu.csc499.peg_lr.util.PatternTestUtils.assertRejects;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.component.charclass.PatternDigit;
import edu.ncsu.csc499.peg_lr.pattern.component.operator.PatternPredicate;
import edu.ncsu.csc499.peg_lr.pattern.definition.DefinedPattern;

/**
 * @author Melody Griesen
 *
 */
public class PatternPredicateTest {

	/** Test object used for testing. */
	Pattern pattern;

	/** String used in predicate. */
	private static final String PREDICATE = "do";

	/** Pattern used as predicate. */
	private static final Pattern string = new PatternString(PREDICATE);

	/**
	 * Tests that variations of the And predicate accept strings that begin with the
	 * predicate, but don't match any input. Strings that don't begin with the
	 * predicate are rejected.
	 */
	@Test
	public void testAndPredicate() {

		pattern = new PatternPredicate(string, true);

		assertMatchesExact(pattern, "doing", "");
		assertMatchesExact(pattern, "double", "");
		assertMatchesExact(pattern, "doubt", "");
		assertMatchesExact(pattern, "do", "");

		assertRejects(pattern, "da");
		assertRejects(pattern, "oo");
		assertRejects(pattern, "");
		assertRejects(pattern, "d");
		assertRejects(pattern, "ado");

	}

	/**
	 * Tests that variations of the Not predicate accept strings that do not begin
	 * with the predicate, but don't match any input. Strings that do begin with the
	 * predicate are rejected.
	 */
	@Test
	public void testNotPredicate() {

		pattern = new PatternPredicate(string, false);

		assertRejects(pattern, "doing");
		assertRejects(pattern, "double");
		assertRejects(pattern, "doubt");
		assertRejects(pattern, "do");

		assertMatchesExact(pattern, "da", "");
		assertMatchesExact(pattern, "oo", "");
		assertMatchesExact(pattern, "", "");
		assertMatchesExact(pattern, "d", "");
		assertMatchesExact(pattern, "ado", "");

	}

	@Test
	public void testNullable() {
		// A predicate with a non-nullable pattern is nullable
		Assert.assertTrue(new PatternPredicate(new PatternDigit(), true).isNullable());
		Assert.assertTrue(new PatternPredicate(new PatternDigit(), false).isNullable());

		// A predicate with a nullable pattern is nullable
		Assert.assertTrue(new PatternPredicate(new PatternString(""), true).isNullable());
		Assert.assertTrue(new PatternPredicate(new PatternString(""), false).isNullable());
	}

	@Test
	public void testLeftRecursive() {
		// A predicate should be able to be left-recursive

		// First choice should hold left recursion
		// If it's false
		final Pattern recursiveFalsePredicate = new DefinedPattern("TestFirstChoiceLR") {

			private final Pattern pat = new PatternPredicate(this, true);
			{
				super.setDefinition(pat);
			}

		};
		Assert.assertTrue(recursiveFalsePredicate.isLeftRecursive());

		// And if it's true
		final Pattern recursiveTruePredicate = new DefinedPattern("TestFirstChoiceLR") {

			private final Pattern pat = new PatternPredicate(this, true);
			{
				super.setDefinition(pat);
			}

		};
		Assert.assertTrue(recursiveTruePredicate.isLeftRecursive());

		// The pattern isn't left recursive on its own
		Assert.assertFalse(new PatternPredicate(new PatternDigit(), true).isLeftRecursive());
		final Pattern notLRDefinition = new DefinedPattern("TestNotLRDefinition",
				new PatternPredicate(new PatternString(""), false));
		Assert.assertFalse(notLRDefinition.isLeftRecursive());
	}

}
