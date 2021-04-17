package edu.ncsu.csc499.peg_lr.structure;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc499.peg_lr.structure.Result.LeftRecursionStatus;

public class ResultTest {

	/** Test object */
	private Result result;

	@Before
	public void setUp() throws Exception {
		result = new Result("asdf", 1);
	}

	@Test
	public void testFAIL() {
		// The failed Result should:
		final Result FAIL = Result.FAIL(3);

		// not be successful
		Assert.assertFalse(FAIL.isSuccess());

		// have no Type
		Assert.assertEquals(null, FAIL.getType());
		// not be an alias
		Assert.assertEquals(false, FAIL.isAlias());
		// be hidden
		Assert.assertTrue(FAIL.isHidden());

		// hold an empty string of data
		Assert.assertEquals("", FAIL.getData());

		// go from idx to idx
		Assert.assertEquals(3, FAIL.getStartIdx());
		Assert.assertEquals(3, FAIL.getEndIdx());

		// not be left-recursive
		Assert.assertEquals(LeftRecursionStatus.IMPOSSIBLE, FAIL.getLRStatus());

		// be unique from another call to FAIL:
		Assert.assertNotSame(Result.FAIL(3), FAIL);

	}

	@Test
	public void testConstructorIndex() {
		// Construct result with no data at idx 1
		result = new Result(1);

		// Ensure desired properties
		Assert.assertTrue(result.isSuccess());
		Assert.assertEquals(1, result.getStartIdx());
		Assert.assertEquals(1, result.getEndIdx());
		Assert.assertEquals("", result.getData());
		Assert.assertTrue(result.isHidden());
		Assert.assertFalse(result.isAlias());
		Assert.assertNull(result.getType());
		Assert.assertEquals(LeftRecursionStatus.POSSIBLE, result.getLRStatus());
	}

	@Test
	public void testConstructorSingleCharacter() {
		// Construct result with single character 'a' at idx 1
		result = new Result('a', 1);

		// Ensure desired properties
		Assert.assertTrue(result.isSuccess());
		Assert.assertEquals(1, result.getStartIdx());
		Assert.assertEquals(2, result.getEndIdx());
		Assert.assertEquals("a", result.getData());
		Assert.assertTrue(result.isHidden());
		Assert.assertFalse(result.isAlias());
		Assert.assertNull(result.getType());
		Assert.assertEquals(LeftRecursionStatus.POSSIBLE, result.getLRStatus());
	}

	@Test
	public void testConstructorData() {
		// Construct result with data "asdf" at idx 1
		result = new Result("asdf", 1);

		// Ensure desired properties
		Assert.assertTrue(result.isSuccess());
		Assert.assertEquals(1, result.getStartIdx());
		Assert.assertEquals(5, result.getEndIdx());
		Assert.assertEquals("asdf", result.getData());
		Assert.assertTrue(result.isHidden());
		Assert.assertFalse(result.isAlias());
		Assert.assertNull(result.getType());
		Assert.assertEquals(LeftRecursionStatus.POSSIBLE, result.getLRStatus());
	}

	@Test
	public void testConstructorFull() {
		// Construct successful result with data "asdf" at idx 1
		result = new Result(true, "asdf", 1, LeftRecursionStatus.IMPOSSIBLE);

		// Ensure desired properties
		Assert.assertTrue(result.isSuccess());
		Assert.assertEquals(1, result.getStartIdx());
		Assert.assertEquals(5, result.getEndIdx());
		Assert.assertEquals("asdf", result.getData());
		Assert.assertTrue(result.isHidden());
		Assert.assertFalse(result.isAlias());
		Assert.assertNull(result.getType());
		Assert.assertEquals(LeftRecursionStatus.IMPOSSIBLE, result.getLRStatus());

		// Construct failed result with data "" at idx 1
		result = new Result(false, "", 1, LeftRecursionStatus.IMPOSSIBLE);

		// Ensure desired properties
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(1, result.getStartIdx());
		Assert.assertEquals(1, result.getEndIdx());
		Assert.assertEquals("", result.getData());
		Assert.assertTrue(result.isHidden());
		Assert.assertFalse(result.isAlias());
		Assert.assertNull(result.getType());
		Assert.assertEquals(LeftRecursionStatus.IMPOSSIBLE, result.getLRStatus());
	}

	@Test
	public void testHidden() {
		// Default result is null-type not-alias: hidden
		Assert.assertNull(result.getType());
		Assert.assertFalse(result.isAlias());
		Assert.assertTrue(result.isHidden());

		// Set to true alias
		result.setAlias(true);

		// Result is now null-type alias: hidden
		Assert.assertNull(result.getType());
		Assert.assertTrue(result.isAlias());
		Assert.assertTrue(result.isHidden());

		// Set type to something valid
		result.setType("type");

		// Result is now non-null-type alias: hidden
		Assert.assertNotNull(result.getType());
		Assert.assertTrue(result.isAlias());
		Assert.assertTrue(result.isHidden());

		// Set alias to false
		result.setAlias(false);

		// Result is now non-null-type not-alias: not hidden
		Assert.assertNotNull(result.getType());
		Assert.assertFalse(result.isAlias());
		Assert.assertFalse(result.isHidden());

		// Restore initial state by setting type to null
		result.setType(null);
		// Default result is null-type not-alias: hidden
		Assert.assertNull(result.getType());
		Assert.assertFalse(result.isAlias());
		Assert.assertTrue(result.isHidden());
	}

	@Test
	public void testAddChar() {
		// Initial result is "asdf" idx 1-5

		// Add another character 'g'
		result.addChar('g');

		// Test updates
		Assert.assertEquals("asdfg", result.getData());
		Assert.assertEquals(6, result.getEndIdx());

		// Test start stayed the same
		Assert.assertEquals(1, result.getStartIdx());
	}

	@Test
	public void testAddChild() {
		// Start off with empty string
		result = new Result(1);

		// Add a child that holds "as". Parent result should be returned.
		Assert.assertSame(result, result.addChild(new Result("as", 1)));

		// Parent result should now hold child's info
		Assert.assertEquals(1, result.getStartIdx());
		Assert.assertEquals("as", result.getData());
		Assert.assertEquals(3, result.getEndIdx());

		// Add a second child that holds "df". Parent result should be returned.
		Assert.assertSame(result, result.addChild(new Result("df", 3)));

		// Parent result should now hold child's info
		Assert.assertEquals(1, result.getStartIdx());
		Assert.assertEquals("asdf", result.getData());
		Assert.assertEquals(5, result.getEndIdx());

		// Invalid cases

		// Shouldn't be able to add a child with an end index that doesn't match the
		// parent's current end index

		// Direct test
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			result.addChild(new Result("j", 7));
		});

		// After adding a character
		result.addChar('g');
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			result.addChild(new Result("j", 7));
		});

	}

	@Test
	public void testToString() {
		// Ensure that string contains the data we expect it to show
		final String toString = result.toString();

		Assert.assertTrue(toString.contains(result.getData()));
		Assert.assertTrue(toString.contains("" + result.getType()));
		Assert.assertTrue(toString.contains("" + result.getStartIdx()));
		Assert.assertTrue(toString.contains("" + result.getEndIdx()));
		Assert.assertTrue(toString.contains(result.getLRStatus().toString()));
	}

}
