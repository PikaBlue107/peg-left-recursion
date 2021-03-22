package pattern;

import event.control.GrowingEvent;
import event.control.GrowingEvent.GrowingEventType;
import event.pattern.MetaMatchEvent;
import event.pattern.MetaMatchEvent.MetaMatchEventType;
import event.pattern.PatternMatchEvent;
import structure.InputContext;
import structure.Result;
import structure.Result.LeftRecursionStatus;

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

		// If this pattern has no Type or is an alias, delegate immediately to match()
		if ((this.getType() == null) || this.isAlias()) {

			// Skip left-recursion and memoization
			return this.matchAndName(context);
		}

		// Begin the full meta-match
		// Log it in the context
		context.addHistory(new MetaMatchEvent(context, this, context.getPosition(), MetaMatchEventType.BEGIN_PREP));

		// let m = MEMO(R,P)
		// Let m hold the "known" result of applying this Rule at this Position
		// Let m hold the "known" result of applying this Pattern at this Derivation
		Result m = context.resultFor(this);

		// if m = NIL
		// If the "known" result does not yet exist
		if (m == null) {

			// Log that we're going to have to manually run a match
			context.addHistory(new MetaMatchEvent(context, this, context.getPosition(), MetaMatchEventType.RUN_MATCH));

			// Save the initial position before matching in case we need to grow the match
			final int initialPosition = context.getPosition();

			// let lr = new LR(FALSE)
			// Create a new LeftRecursion tracker that says this rule is suspected but not
			// confirmed to be left recursive
			// Create the subsequent Result with a LeftRecursionStatus of POSSIBLE

			// m = new MemoEntry(lr,P)
			// Set m to a failure entry that points us back to the same starting position
			// and indicates that this rule might be left recursive
			// Set m to a failure Result with a left recursion status of POSSIBLE to
			// indicate we suspect but don't know that the Pattern might be left recursive
			m = Result.FAIL(initialPosition);

			// MEMO(R,P) = m
			// Set the memoized answer for applying this rule at this position to be m, the
			// failure memo
			// Set the Result for applying this Pattern on this Derivation to be m, the
			// failure Result
			context.setResultFor(this, m);

			// let ans = EVAL(R.body)
			// Evaluate the Rule at this position, saving the answer in the field "ans"
			// Attempt to match the Pattern on this Derivation, saving the Result in the
			// field "ans"
			final Result ans = matchAndName(context);
			ans.setType(getType());
			ans.setAlias(isAlias());

			ans.setLRStatus(m.getLRStatus());
			context.setResultFor(this, ans, initialPosition);

			// m.ans = ans
			// Set the memoized answer equal to the answer from evaluating the rule at this
			// position
			// Set the memoized Rule's success equal to the success from evaluating the
			// Pattern at this Derivation
//			m.setSuccess(ans.isSuccess());

			// m.pos = Pos
			// Set the memoized position equal to the current Position after evaluating the
			// rule
			// Set the memoized Derivation equal to the Derivation within the Result
			// acquired from evaluating the Pattern
//			m.setDerivation(ans.getDerivation());

			// if lr.detected and ans != FAIL
			// If the answer from evaluating the rule says that it is left-recursive and it
			// had a definite match when we finished evaluating it 3 lines of code up
			// If the Result from evaluating the Pattern has a DETECTED LeftRecursionStatus
			// and it has a successful match (seed) of the recursive pattern
			if ((ans.getLRStatus() == LeftRecursionStatus.DETECTED) && ans.isSuccess()) {

				// Log that we're going to start growing this left-recursive call
				context.addHistory(new MetaMatchEvent(context, this, ans, MetaMatchEventType.BEGIN_GROW));

				// return GROW-LR(R,P,m,NIL)
				// return the result of growing the left-recursive rule until it cannot be
				// re-evaluated to consume any more input
				// return the result of growing the left-recursive Pattern until it cannot be
				// re-evaluated to consume any more input
				return growLeftRecursion(context, initialPosition);

			} else {
				// Set left recursion status to impossible, since we didn't get a simultaneous
				// call while this branch executed
				ans.setLRStatus(LeftRecursionStatus.IMPOSSIBLE);

				// return ans
				// Return the answer of the evaluation of the rule
				// Return the Result of attmepting to match the Pattern
				return ans;
			}

		} else {

			// Log that we're going to delegate to using the saved result
			context.addHistory(new MetaMatchEvent(context, this, m, MetaMatchEventType.ASSUME_RESULT));

			// Pos = m.pos
			// Set the current position of parsing equal to the result m's position
			// If the result is a success, set the current position of the context equal to
			// the result m's end index
			if (m.isSuccess()) { // TODO: Ideally we comment this out
				context.setPosition(m.getEndIdx());
			}

			// if m.ans is LR
			// if the current memoised answer of the rule application is LR, then this else
			// block was called
			// while another instance of this method further up the stack trace is still
			// trying to evaluate the rule
			// after an initial creation of a possibly left-recursive memoised answer
			// if the current memoised Result of the Pattern application has a
			// LeftRecursiveStatus of POSSIBLY,
			// then this iteration of lazyMatch() was called while another iteration further
			// up the stack trace is still
			// in the middle of the first evaluation of this rule at this position,
			// meaning that this rule is definitely left-recursive at this position
			if (m.getLRStatus() == LeftRecursionStatus.POSSIBLE) {
				// m.ans.detected = TRUE
				// Mark this rule at this position as left-recursive for sure
				// Mark this Pattern at this Derivation as left-recursive for sure
				m.setLRStatus(LeftRecursionStatus.DETECTED);

				// Log that we identified a left-recursive call

				// Log that we're flagging this result as left-recursive
				context.addHistory(new MetaMatchEvent(context, this, m, MetaMatchEventType.IDENTIFY_LR));

				// return FAIL
				// end this iteration with a failure so that we can find a seed later on in the
				// rule
				// end this iteration with a failure so that we can find a seed later on in the
				// Pattern
				assert !m.isSuccess();
			} else {
				// return m.ans
				// Return the answer of applying the rule at this position
				// Return the Result of applying the rule at this position
			}

			// Return the result of applying the rule at this position
			return m;

		}

//		// Check for previously calculated Result
//		if (derivation.hasSaved(this)) {
//			return derivation.resultFor(this);
//		}
//
//		// No previously-calculated Result, we gotta do it ourselves.
//
//		// Calculate result
//		Result<?> result = match(derivation);
//		// Save result
//		derivation.setResultFor(this, result);
//		// Return result
//		return result;
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