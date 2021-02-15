package structure;

public class Result<T> {

	/** The success of this result. */
	private boolean success;

	/** The syntactic value of this Result. */
	private T value;

	/** The left-recursion status of this Result. */
	private LeftRecursionStatus lRStatus;

	/**
	 * The Derivation that this result gives (the remaining unmatched characters
	 * after this result is acquired).
	 */
	private Derivation derivation;

	/**
	 * Generates a new fail Result for quick use in pattern definitions.
	 * 
	 * @return a Result representing a failed match.
	 */
	public static final Result<Object> FAIL() {
		return new Result<Object>(false, null, null);
	}

	/**
	 * @param success
	 * @param value
	 * @param derivation
	 */
	public Result(boolean success, T value, Derivation derivation) {
		this(success, value, derivation, LeftRecursionStatus.POSSIBLE);
	}
	
	public Result(boolean success, T value, Derivation derivation, LeftRecursionStatus leftRecursionStatus) {
		this.success = success;
		this.value = value;
		this.derivation = derivation;
		this.lRStatus = leftRecursionStatus;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @return the derivation
	 */
	public Derivation getDerivation() {
		return derivation;
	}

	/**
	 * @param derivation the derivation to set
	 */
	public void setDerivation(Derivation derivation) {
		this.derivation = derivation;
	}

	/**
	 * @return the lRStatus
	 */
	public LeftRecursionStatus getLRStatus() {
		return lRStatus;
	}

	/**
	 * @param lRStatus the lRStatus to set
	 */
	public void setLRStatus(LeftRecursionStatus lRStatus) {
		this.lRStatus = lRStatus;
	}

	/**
	 * Generates a String containing all of the Result's fields
	 */
	@Override
	public String toString() {
		return "Result [success=" + success + ", value=" + value + ", lRStatus=" + lRStatus + ", derivation="
				+ derivation + "]";
	}

	/**
	 * Possible options for the result of a Pattern at any given position. A Pattern
	 * can have left recursion be possible, detected, or impossible.
	 * 
	 * @author Melody Griesen
	 *
	 */
	public enum LeftRecursionStatus {
		/**
		 * The Pattern at this Derivation *might* be left-recursive. We'll know after it
		 * finishes matching once.
		 */
		POSSIBLE,
		/**
		 * The Pattern at this Derivation is definitely left-recursive. It called itself
		 * before we had a chance to finish its first match.
		 */
		DETECTED,
		/**
		 * The Pattern at this Derivation is not left-recursive. We finished one full
		 * match without it calling itself.
		 */
		IMPOSSIBLE
	}

}
