package edu.ncsu.csc499.peg_lr.pattern;

import java.util.Iterator;
import java.util.List;

import edu.ncsu.csc499.peg_lr.event.control.GrowingEvent;
import edu.ncsu.csc499.peg_lr.event.control.GrowingEvent.GrowingEventType;
import edu.ncsu.csc499.peg_lr.event.pattern.MetaMatchEvent;
import edu.ncsu.csc499.peg_lr.event.pattern.MetaMatchEvent.MetaMatchEventType;
import edu.ncsu.csc499.peg_lr.event.pattern.PatternMatchEvent;
import edu.ncsu.csc499.peg_lr.structure.InputContext;
import edu.ncsu.csc499.peg_lr.structure.Result;
import edu.ncsu.csc499.peg_lr.structure.Result.LeftRecursionStatus;

public abstract class Pattern {

	/** Generates a series of unique IDs for each Pattern created. */
	private static int nextPatternId = 0;

	/** Stores this Pattern's id. */
	private final int id;

	/**
	 * Constructs a Pattern by assigning a unique ID.
	 */
	public Pattern() {
		// Assign ID
		id = nextPatternId++;
	}

	/**
	 * Determines whether this Pattern is left-recursive. This Pattern is
	 * left-recursive if one of its productions begins with this same object.
	 * 
	 * @return true if this pattern is left recursive, else false.
	 */
	public boolean isLeftRecursive() {
		// Just check this pattern's components for left-recursion. We don't want to
		// check it against itself immediately!
		return isSubMatchLeftRecursiveOf(this);
	}

	/**
	 * Determines whether this Pattern is left-recursive with respect to the given
	 * pattern. This Pattern is left-recursive if one of its productions begins with
	 * the target object.
	 * 
	 * @param pattern the pattern to compare against this one for left recursion
	 * @return true if this pattern is left recursive, else false.
	 */
	private boolean isLeftRecursiveOf(final Pattern pattern) {
		// If this is the pattern itself, we know it's left recursive.
		if (this == pattern) {
			return true;
		}

		// Otherwise, check its components for left recursion
		return isSubMatchLeftRecursiveOf(pattern);
	}

	/**
	 * Checks all possible leftmost submatches of this pattern to see if they may be
	 * left-recursive of a given pattern.
	 *
	 * @param pattern the pattern to compare against this one for left recursion
	 * @return true if this pattern contains a left recursive component, else false.
	 */
	private boolean isSubMatchLeftRecursiveOf(final Pattern pattern) {

		// Get the list of all sub-patterns that could possibly be the first sub-match
		// of this pattern
		final Iterator<Pattern> components = getPossibleLeftmostComponents();

		// Run through this list. If any of those patterns are left-recursive of this
		// one, then we have an overall LR pattern.
		Pattern possibleLeftmost;
		// While there's still more elements to check
		while (components.hasNext()) {
			// Calculate and retrieve the next possibly left recursive element
			possibleLeftmost = components.next();
			// If it's left recursive, then we are too.
			if (possibleLeftmost.isLeftRecursiveOf(pattern)) {
				return true;
			}
		}

		// None of the possible leftmost patterns are left recursive.
		// Thus, this pattern isn't left recursive.
		return false;
	}

	/**
	 * Retrieves all sub-Patterns that make up this parent Pattern.
	 *
	 * @return a List of sub-patterns that are used to express this pattern. The
	 *         List may be empty for Patterns that are entirely independent.
	 */
	public abstract List<Pattern> getPatternComponents();

	/**
	 * Retrieves a subset of all sub-patterns that make up this parent Pattern. The
	 * Patterns in the set are only those that may end up as the left-most pattern
	 * in this pattern's definition.
	 * 
	 * For example, in an ordered choice, all patterns are included. In a sequence,
	 * the leftmost pattern is included; if that pattern is nullable, the one after
	 * it is also included, and so on for the rest of the sequence.
	 *
	 * @return
	 */
	protected abstract Iterator<Pattern> getPossibleLeftmostComponents();

	/**
	 * Determines whether this pattern can successfully match the empty string
	 * \u03B5 (epsilon).
	 *
	 * @return true if this pattern can match a 0-length string, else false
	 */
	public abstract boolean isNullable();

	/**
	 * Determines if this Pattern should be skipped in left-recursion memoization
	 * events and in the results tree.
	 *
	 * @return true if the pattern is either an alias or has no type
	 */
	public boolean isHidden() {
		return this.isAlias() || (this.getType() == null);
	}

	/**
	 * Retrieves this Pattern's unique ID.
	 * 
	 * @return
	 */
	public int getID() {
		return id;
	}

	/**
	 * Requires that subclasses declare whether they are defined as an alias
	 * (skipped in the Results tree printout, not considered for memoization)
	 * 
	 * @return true if the Pattern is an alias
	 */
	public boolean isAlias() {
		// Default behavior is to not be an alias
		return false;
	}

	/**
	 * Helper method to assign this Pattern's Type and Alias to any Results that
	 * subclasses provide us with.
	 *
	 * @param context the context over which we should attempt to match
	 * @return a Result returned by the subclass, set to have this Pattern's type
	 *         and alias
	 */
	private Result matchAndName(final InputContext context) {
		// Make an event saying we're attempting to match this pattern
		context.addHistory(new PatternMatchEvent(context, context.getPosition(), this));
		// Retrieve the result
		final Result r = this.match(context);
		// Copy type and alias status to the Result
		r.setType(getType());
		r.setAlias(isAlias());
		// Make an event saying whether it was accepted or rejected
		context.addHistory(new PatternMatchEvent(context, r, this));
		// Return the updated Result
		return r;
	}

	/**
	 * Declares the pattern Type (the name that this Pattern is referred to as in
	 * Result printouts and pattern references). If a Pattern's Type is null, then
	 * it doesn't have a display name, indicating that it should be skipped in the
	 * Results tree printout and not considered for memoization.
	 *
	 * @return the Pattern's display type
	 */
	public abstract String getType();

	/**
	 * Declares the pattern Definition (the string representing the form that this
	 * Pattern accepts). This should use the Rosie RPL syntax.
	 * 
	 * @return a string indicating what the Pattern matches
	 */
	public String getDefinition() {
		return getDefinition(false);
	}

	/**
	 * Declares the pattern Definition (the string representing the form that this
	 * Pattern accepts). This should use the Rosie RPL syntax.
	 * 
	 * @param component whether this pattern is a component of another pattern
	 *                  definition
	 * @return a string indicating what the Pattern matches
	 */
	public abstract String getDefinition(boolean component);

	/**
	 * Grows left-recursive rules from a seed by iteratively re-calculating them
	 * with more memoized results until they cannot match any more.
	 * 
	 * @param context the derivation to begin growing from. This derivation should
	 *                already have a seed planted for this pattern before calling
	 *                this method.
	 * @return the final Result of growing the left-recursive Pattern, also saved in
	 *         the Derivation.
	 */
	private Result growLeftRecursion(final InputContext context, final int initialPosition) {

		// Set up fields to be used in main loop
		Result attempt = null;
		int farthestMatchEndPos = context.resultFor(this, initialPosition).getEndIdx();
		int iteration = 1;

		// Loop until we find a special case
		while (true) {
			// Log beginning of step
			context.addHistory(
					new GrowingEvent(context, GrowingEventType.GROW_ATTEMPT, this, initialPosition, iteration));
			// Reset to the beginning to check this case
			context.setPosition(initialPosition);

			// Start matching from this current derivation we're given
			// Attempt to *match* the Pattern (this one) against the Derivation
			attempt = matchAndName(context);
			// Ensure that this attempt is labeled as left-recursive
			attempt.setLRStatus(LeftRecursionStatus.DETECTED);

			// If we didn't make any progress, then exit
			if (!attempt.isSuccess()) {
				context.addHistory(
						new GrowingEvent(context, GrowingEventType.GROW_FAIL, this, initialPosition, iteration));
				break;
			} else if (context.getPosition() <= farthestMatchEndPos) {
				context.addHistory(
						new GrowingEvent(context, GrowingEventType.GROW_REJECT, this, initialPosition, iteration));
				break;
			}

			// Add history event for succeeding this step
			context.addHistory(
					new GrowingEvent(context, GrowingEventType.GROW_ACCEPT, this, initialPosition, iteration));
			// Otherwise, update the Derivation's memoized Result with the one we just
			// calculated, and try to match again!
			context.setResultFor(this, attempt, initialPosition);
			// Check the farthest match we've gotten so far
			farthestMatchEndPos = attempt.getEndIdx();
			// Increment the iteration counter
			iteration++;
		}

		// Log exit from method. -1 ot iterations because last iteration is invalid
		context.addHistory(new GrowingEvent(context, GrowingEventType.TERMINATE, this, initialPosition, iteration - 1));

		// Set the context to rest at the end of the farthest match we were able to find
		context.setPosition(farthestMatchEndPos);

		// Return the result for the overall match
		return context.resultFor(this, initialPosition);
	}

	/**
	 * Lazily matches this pattern onto the provided Derivation. If the Derivation
	 * already expresses a result for this Pattern, returns that. Otherwise,
	 * calculates the Result of the match, saves it to the Derivation for future
	 * use, and returns it.
	 * 
	 * @param context
	 * @return
	 */
	public final Result lazyMatch(final InputContext context) {

		// If this pattern is hidden or not left-recursive, delegate immediately to
		// match()
		if (this.isHidden() | !this.isLeftRecursive()) {

			// Skip left-recursion and memoization
			return this.matchAndName(context);
		}
		// Otherwise, it's left-recursive.
		else {
			// Begin the main LR algorithm

			// Log it in the context
			context.addHistory(new MetaMatchEvent(context, this, context.getPosition(), MetaMatchEventType.BEGIN_PREP));

			final Result seed = context.resultFor(this);

			// If there's no result for this Pattern at this Position
			if (seed == null) {
				// We're at the start of a (possibly) left-recursive match

				// Log that we're going to have to manually run a match
				context.addHistory(
						new MetaMatchEvent(context, this, context.getPosition(), MetaMatchEventType.RUN_MATCH));

				// Save the initial position before matching in case we need to grow the match
				final int initialPosition = context.getPosition();

				// Set the current Result for this match equal to a fail result
				context.setResultFor(this, Result.FAIL(initialPosition));

				// Evaluate the Pattern at this index, possibly matching a seed
				final Result ans = matchAndName(context);

				// Update the retrieved answer as the new answer in the growing map
				context.setResultFor(this, ans, initialPosition);

				// If it was a successful match
				if (ans.isSuccess()) {
					// We have a seed! Time to attempt to grow

					// Log that we're going to start growing this left-recursive call
					context.addHistory(new MetaMatchEvent(context, this, ans, MetaMatchEventType.BEGIN_GROW));

					// Return the result of growing th left-recursive Pattern until it cannot be
					// re-evaluated to consume any more input
					return growLeftRecursion(context, initialPosition);

				} else {
					// Return the failed Result that we got from matching. No seed.
					return ans;
				}

			} else {

				// Log that we're going to delegate to using the saved result
				context.addHistory(new MetaMatchEvent(context, this, seed, MetaMatchEventType.ASSUME_RESULT));

				// Set the current position of the context equal to the seed's end index
				context.setPosition(seed.getEndIdx());

				// Return the result of applying the rule at this position
				return seed;

			}

		}

	}

	/**
	 * Any pattern should be able to decide whether it matches a given derivation.
	 * 
	 * @param derivation the substring left to match (and associated information)
	 * @return a Result indicating whether the match was successful, and if so, how
	 *         much was consumed by this match.
	 */
	protected abstract Result match(InputContext context);

	/**
	 * Generates (the base of) a unique hash code for this Pattern. By default, all
	 * Patterns are considered the same.
	 */
	@Override
	public int hashCode() {
		final int result = 1;
		return result;
	}

	/**
	 * Declares that by default, all Patterns are equal.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		return true;
	}

	/**
	 * Provides the type and definition for this Pattern.
	 */
	@Override
	public String toString() {
		return getType() + " <- " + getDefinition();
	}

// Figure 2 from Warth's "Packrat Parsers Can Support Left Recursion"
//	/**
//	 * Lazily matches this pattern onto the provided Derivation. If the Derivation
//	 * already expresses a result for this Pattern, returns that. Otherwise,
//	 * calculates the Result of the match, saves it to the Derivation for future
//	 * use, and returns it.
//	 *
//	 * @param derivation
//	 * @return
//	 */
//	public final Result<?> lazyMatch(Derivation derivation) {
//
//		// let m = MEMO(R,P)
//		// Let m hold the "known" result of applying this Rule at this Position
//		// Let m hold the "known" result of applying this Pattern at this Derivation
//		Result<?> m = derivation.resultFor(this);
//
//		// if m = NIL
//		// If the "known" result does not yet exist
//		if (m == null) {
//
//			// m = new MemoEntry(Fail,P)
//			// Set m to a failure entry that points us back to the same starting position
//			// Set m to a failure Result that doesn't provide a matching Derivation
//			m = Result.FAIL;
//
//			// MEMO(R,P) = m
//			// Set the memoized answer for applying this rule at this position to be m, the failure memo
//			// Set the Result for applying this Pattern on this Derivation to be m, the failure Result
//			derivation.setResultFor(this, m);
//
//			// let ans = EVAL(R.body)
//			// Evaluate the Rule at this position, saving the answer in the field "ans"
//			// Attempt to match the Pattern on this Derivation, saving the Result in the field "ans"
//			Result<?> ans = match(derivation);
//
//			// m.ans = ans
//			// Set the memoized answer equal to the answer from evaluating the rule at this position
//			// Set the memoized Rule's success equal to the success from evaluating the Pattern at this Derivation
//			//m.setSuccess(ans.isSuccess());
//
//			// m.pos = Pos
//			// Set the memoized position equal to the current Position after evaluating the rule
//			// Set the memoized Derivation equal to the Derivation within the Result acquired from evaluating the Pattern
//			//m.setDerivation(ans.getDerivation());
//
//			// Instead of the above two lines, we will instead just set derivation result for (this) equal to ans instead of m
//			// This was done in part because I have a bug with code setting the single object returned from Result.FAIL. Bug isn't fixed, but this workaround makes it go away for a little bit longer.
//			derivation.setResultFor(this, ans);
//
//			// return ans
//			// Return the answer of the evaluation of the rule
//			// Return the Result of attmepting to match the Pattern
//			return ans;
//
//
//
//
//		} else {
//			// Pos = m.pos
//			// Set the current position of parsing equal to the result m's position
//			// // handled implicitly by returning the Result object
//
//			// return m.ans
//			// Return the answer of applying the rule at this position
//			// Return the Result of applying the rule at this position
//			return m;
//		}
//
////		// Check for previously calculated Result
////		if (derivation.hasSaved(this)) {
////			return derivation.resultFor(this);
////		}
////
////		// No previously-calculated Result, we gotta do it ourselves.
////
////		// Calculate result
////		Result<?> result = match(derivation);
////		// Save result
////		derivation.setResultFor(this, result);
////		// Return result
////		return result;
//	}

}
