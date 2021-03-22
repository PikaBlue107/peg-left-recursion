/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.component;

import static edu.ncsu.csc499.peg_lr.util.PatternTestUtils.assertMatchesExact;
import static edu.ncsu.csc499.peg_lr.util.PatternTestUtils.assertRejects;

import org.junit.Test;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternPredicate;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternString;

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

}
