/**
 * 
 */
package edu.ncsu.csc499.peg_lr.pattern.component;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc499.peg_lr.pattern.Pattern;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternDigit;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternSequence;
import edu.ncsu.csc499.peg_lr.pattern.component.PatternString;
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

}
