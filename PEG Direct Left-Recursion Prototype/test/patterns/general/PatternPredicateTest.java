/**
 * 
 */
package patterns.general;

import org.junit.Test;

/**
 * @author Melody Griesen
 *
 */
public class PatternPredicateTest {

//	Pattern pattern;

//	private static final Pattern digit = new PatternPredicate();

	@Test
	public void testData() {

//		// Test improper calls
//		Assert.assertThrows(IllegalArgumentException.class, () -> {
//			new PatternRepetition(null, 0, 1);
//		});
//		Assert.assertThrows(IllegalArgumentException.class, () -> {
//			new PatternRepetition(digit, -1, 1);
//		});
//		Assert.assertThrows(IllegalArgumentException.class, () -> {
//			new PatternRepetition(digit, 0, -2);
//		});

	}

	@Test
	public void testStar() {
//		// Pattern allows 0-infinity matches
//		pattern = new PatternRepetition(digit, 0, -1);
//
//		PatternTestUtils.assertMatches(pattern, "");
//		PatternTestUtils.assertMatches(pattern, "1");
//		PatternTestUtils.assertMatches(pattern, "12");
//		PatternTestUtils.assertMatches(pattern, "123");
//		PatternTestUtils.assertMatches(pattern, "1234");
	}

	@Test
	public void testPlus() {
//		// Pattern allows 1-infinity matches
//		pattern = new PatternRepetition(digit, 1, -1);
//
//		PatternTestUtils.assertRejects(pattern, "");
//		PatternTestUtils.assertMatches(pattern, "1");
//		PatternTestUtils.assertMatches(pattern, "12");
//		PatternTestUtils.assertMatches(pattern, "123");
//		PatternTestUtils.assertMatches(pattern, "1234");

	}

	@Test
	public void testRange() {
//		// Pattern allows 1-3 matches
//		pattern = new PatternRepetition(digit, 1, 3);
//
//		PatternTestUtils.assertRejects(pattern, "");
//		PatternTestUtils.assertMatches(pattern, "1");
//		PatternTestUtils.assertMatches(pattern, "12");
//		PatternTestUtils.assertMatches(pattern, "123");
//		PatternTestUtils.assertMatchesPrefix(pattern, "1234");

	}

	@Test
	public void testExact() {
//		// Pattern requires exactly 3 repetitions
//		pattern = new PatternRepetition(digit, 3, 3);
//
//		PatternTestUtils.assertRejects(pattern, "");
//		PatternTestUtils.assertRejects(pattern, "1");
//		PatternTestUtils.assertRejects(pattern, "12");
//		PatternTestUtils.assertMatches(pattern, "123");
//		PatternTestUtils.assertMatchesPrefix(pattern, "1234");

	}

}
