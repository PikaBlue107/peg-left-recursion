package structure;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {

	/** The success of this result. */
	private boolean success;

	/** The syntactic value of this Result. */
	private T value;
	
	/** The "type" of the pattern that matched this Result. */
	private String type;

	/** The left-recursion status of this Result. */
	private LeftRecursionStatus lRStatus;

	/** The sub-matches within this Result. */
	private List<Result<?>> children;

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
		this.children = new ArrayList<>();
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Adds a sub-match to this Result
	 * 
	 * @param child
	 */
	public Result<T> addChild(Result<?> child) {
		children.add(child);
		return this;
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
	 * Generates the Tree of matches that this Result represents.
	 * @return a tree of the match, sub-matches, and additional information
	 */
	public String printResultTree() {
		return printResultSubTree(0).toString();
	}
	
	private StringBuilder printResultSubTree(int indentLevel) {
		StringBuilder tree = new StringBuilder();
		tree.append(tabs(indentLevel)).append("{\n");
		
		tree.append(tabs(indentLevel + 1)).append("\"type\": \"").append(type).append("\"\n");
		
//		tree.append(tabs(indentLevel + 1)).append("end: ").append(derivation?).append("\n");
		
		if(!children.isEmpty()) {
			tree.append(tabs(indentLevel + 1)).append("\"subs\": [\n");
			for(Result<?> r : children) {
				tree.append(r.printResultSubTree(indentLevel + 2));
				
				if(r != children.get(children.size() - 1))
					tree.append(",");
				
				tree.append("\n");
			}
			tree.append(tabs(indentLevel + 1)).append("]\n");
		}
		
		tree.append(tabs(indentLevel) + "}");
		return tree;
	}
	
	private String tabs(int num) {
		return "  ".repeat(num);
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
