package structure;

import java.util.ArrayList;
import java.util.List;

public class Result {

	/** The success of this result. */
	private boolean success;

	/** The syntactic value of this Result. */
	private String data;

	/** The "type" of the pattern that matched this Result. */
	private String type;

	/** The index at which this match starts, inclusive, 1-indexed. */
	private int startIdx;

	/** The index at which this match ends, exclusive, 1-indexed. */
	private int endIdx;

	/** The left-recursion status of this Result. */
	private LeftRecursionStatus lRStatus;

	/** The sub-matches within this Result. */
	private final List<Result> children;
	{
		this.children = new ArrayList<>();
	}

	/**
	 * Generates a new fail Result for quick use in pattern definitions.
	 * 
	 * @return a Result representing a failed match.
	 */
	public static final Result FAIL() {
		return new Result(false, "", -1);
	}

	/**
	 * Generates a Result with a single initial character, intended for when you
	 * want to construct a Result with one initially matched character and then add
	 * on new characters iteratively.
	 * 
	 * @param success   whether the Result indicates a successful match
	 * @param firstData the first character matched in this Result
	 * @param startIdx  the index at which the match for this Result begins
	 */
	public Result(final boolean success, final char firstData, final int startIdx) {
		this(success, "" + firstData, startIdx);
	}

	/**
	 * @param success
	 * @param data
	 * @param derivation
	 */
	public Result(final boolean success, final String data, final int startIdx) {
		this(success, data, startIdx, LeftRecursionStatus.POSSIBLE);
	}

	public Result(final boolean success, final String data, final int startIdx,
			final LeftRecursionStatus leftRecursionStatus) {
		this.success = success;
		this.data = data;
		this.lRStatus = leftRecursionStatus;
		this.startIdx = startIdx;
		if (success) {
			this.endIdx = startIdx + data.length();
		} else {
			this.endIdx = -1;
		}
	}

	public void addChar(final char nextData) {
		this.data += nextData;
		this.endIdx++;
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
	public void setSuccess(final boolean success) {
		this.success = success;
	}

	/**
	 * @return the value
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the value to set
	 */
	public void setData(final String data) {
		this.data = data;
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
	public void setLRStatus(final LeftRecursionStatus lRStatus) {
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
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * @return the startIdx
	 */
	public int getStartIdx() {
		return startIdx;
	}

	/**
	 * @param startIdx the startIdx to set
	 */
	public void setStartIdx(final int startIdx) {
		this.startIdx = startIdx;
	}

	/**
	 * @return the endIdx
	 */
	public int getEndIdx() {
		return endIdx;
	}

	/**
	 * @param endIdx the endIdx to set
	 */
	public void setEndIdx(final int endIdx) {
		this.endIdx = endIdx;
	}

	/**
	 * Adds a sub-match to this Result
	 * 
	 * @param child
	 */
	public Result addChild(final Result child) {
		children.add(child);
		return this;
	}

	/**
	 * Generates a display-able string yielding all data of this Result except for
	 * objects (derivation and children)
	 */
	@Override
	public String toString() {
		return "Result [success=" + success + ", data=" + data + ", type=" + type + ", startIdx=" + startIdx
				+ ", endIdx=" + endIdx + ", lRStatus=" + lRStatus + "]";
	}

	/**
	 * Generates the Tree of matches that this Result represents.
	 * 
	 * @return a tree of the match, sub-matches, and additional information
	 */
	public String printResultTree() {
		return printResultSubTree(0).toString();
	}

	private StringBuilder printResultSubTree(final int indentLevel) {
		final StringBuilder tree = new StringBuilder();
		tree.append(tabs(indentLevel)).append("{\n");

		tree.append(tabs(indentLevel + 1)).append("\"type\": \"").append(type).append("\",\n");

		tree.append(tabs(indentLevel + 1)).append("\"data\": \"").append(data).append("\",\n");

		tree.append(tabs(indentLevel + 1)).append("\"left_recursion\": \"").append(this.getLRStatus().toString())
				.append("\",\n");

		tree.append(tabs(indentLevel + 1)).append("\"s\": ").append(startIdx + 1).append(",\n");

		tree.append(tabs(indentLevel + 1)).append("\"e\": ").append(endIdx + 1).append(children.isEmpty() ? "" : ",")
				.append("\n");

//		tree.append(tabs(indentLevel + 1)).append("end: ").append(derivation?).append("\n");

		if (!children.isEmpty()) {
			tree.append(tabs(indentLevel + 1)).append("\"subs\": [\n");
			for (final Result r : children) {
				tree.append(r.printResultSubTree(indentLevel + 2));

				if (r != children.get(children.size() - 1)) {
					tree.append(",");
				}

				tree.append("\n");
			}
			tree.append(tabs(indentLevel + 1)).append("]\n");
		}

		tree.append(tabs(indentLevel) + "}");
		return tree;
	}

	private String tabs(final int num) {
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
		IMPOSSIBLE;

		/** Display-friendly names for each value. */
		private static final String[] NAMES = { "Possible", "Detected", "Impossible" };

		/**
		 * Gets the display name of this Enum by indexing into the display names array
		 * by the Enum's ordinal.
		 * 
		 * @return a display-friendly name for this Enum.
		 */
		@Override
		public String toString() {
			return NAMES[this.ordinal()];
		}
	}

}
