package edu.ncsu.csc499.peg_lr.structure;

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

	/**
	 * Tracks whether this Result matches an alias, which should be skipped in the
	 * printout tree.
	 */
	private boolean alias;

	/** The left-recursion status of this Result. */
	private LeftRecursionStatus lRStatus;

	/** The sub-matches within this Result. */
	private final List<Result> children;
	{
		this.children = new ArrayList<>();
	}

	/**
	 * Generates a new fail Result with the given index for quick use in pattern
	 * definitions.
	 * 
	 * @param idx the index that this fail result should mark
	 * @return a Result representing a failed match.
	 */
	public static final Result FAIL(final int idx) {
		final Result fail = new Result("", idx);
		fail.setAlias(false);
		fail.setSuccess(false);
		fail.setLRStatus(LeftRecursionStatus.IMPOSSIBLE);
		return fail;
	}

	/**
	 * Constructs a successful Result with no data at the given index.
	 *
	 * @param startIdx the index at which the match for this Result begins
	 */
	public Result(final int startIdx) {
		this("", startIdx);
	}

	/**
	 * Constructs a Result with a single initial character, intended for when you
	 * want to construct a Result with one initially matched character and then add
	 * on new characters iteratively.
	 *
	 * @param firstData the first character matched in this Result
	 * @param startIdx  the index at which the match for this Result begins
	 */
	public Result(final char firstData, final int startIdx) {
		this("" + firstData, startIdx);
	}

	/**
	 * Constructs a successful result with the given data and start index.
	 * 
	 * @param data     the initial data matched
	 * @param startIdx the index that the data was matched at in the input string
	 */
	public Result(final String data, final int startIdx) {
		this(true, data, startIdx, LeftRecursionStatus.POSSIBLE);
	}

	/**
	 * Constructs a result with all given fields.
	 * 
	 * @param success             whether the result was successful
	 * @param data                the data that this Result matched. Cannot be null.
	 * @param startIdx            the index that the data was matched at in the
	 *                            input string
	 * @param leftRecursionStatus the known recursion status of this Result
	 * @throws NullPointerException if data is null
	 */
	public Result(final boolean success, final String data, final int startIdx,
			final LeftRecursionStatus leftRecursionStatus) {
		setSuccess(success);
		setData(data);
		setLRStatus(leftRecursionStatus);
		setStartIdx(startIdx);
		setEndIdx(startIdx + data.length());
	}

	/**
	 * Adds on a character to this Result, concatenating it onto the data and
	 * incrementing the end index.
	 * 
	 * @param nextData
	 */
	public void addChar(final char nextData) {
		this.data += nextData;
		this.endIdx++;
	}

	/**
	 * Adds a sub-match to this Result. Transfers the child's information to this
	 * parent Result (appends data, sets endIndex, etc.)
	 * 
	 * @param child
	 */
	public Result addChild(final Result child) {

		// Check that this child's start idx matches this parent Result's end idx
		if (this.getEndIdx() != child.getStartIdx()) {
			throw new IllegalArgumentException(
					"Child does not start at the parent's current end position - not a valid child.");
		}

		// Add the child to the children list
		children.add(child);

		// Append the child's data to our own
		setData(this.getData() + child.getData());

		// Set end index equal to child's end index
		setEndIdx(child.getEndIdx());

		// Return this object so that you can add multiple children in sequence
		return this;
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
	 * @return the alias
	 */
	public boolean isAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(final boolean alias) {
		this.alias = alias;
	}

	/**
	 * Determines if this Result is a hidden result. A Result is hidden if at least
	 * one of the following is true:
	 * 
	 * - the Result has a null Type
	 * 
	 * - the Result is an alias
	 * 
	 * 
	 * @return whether this Result is hidden (removed from the printResultTree()
	 *         output)
	 */
	public boolean isHidden() {
		return this.isAlias() || (this.getType() == null);
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
	 * Returns a list of non-hidden children. Only meant for use in
	 * printResultTree().
	 *
	 * @return the list of non-hidden children
	 */
	private List<Result> nonHiddenChildren() {

		// Start counting from zero
		final List<Result> nonHiddenChildren = new ArrayList<>();

		// Loop over all children in this Result
		for (final Result child : this.children) {
			// Child is hidden. Recurse down to count its non-hidden children
			if (child.isHidden()) {
				nonHiddenChildren.addAll(child.nonHiddenChildren());
			}
			// Child is not hidden. Increment counter for non-hidden children
			else {
				nonHiddenChildren.add(child);
			}
		}

		// Return our total count
		return nonHiddenChildren;
	}

	/**
	 * Generates the Tree of matches that this Result represents.
	 * 
	 * @return a tree of the match, sub-matches, and additional information
	 */
	public String printResultTree() {
		// Default behavior is to skip hidden elements
		return printResultTree(false);
	}

	/**
	 * Generates the Tree of matches that this Result represents. If includeHidden
	 * is true, prints all hidden matches (null-type and alias) as well.
	 * 
	 * @param includeHidden whether to include hidden Results
	 * @return a tree of hte match, sub-matches, and additional information
	 */
	public String printResultTree(final boolean includeHidden) {
		return printResultSubTree(0, includeHidden).toString();
	}

	/**
	 * Recursive helper to printResultTree(). Prints the tree at this level, and
	 * recurses for each child.
	 *
	 * @param indentLevel the level of indentation that this subtree should print at
	 * @return a StringBuilder yielding the sub-tree for this Result
	 */
	private StringBuilder printResultSubTree(final int indentLevel, final boolean includeHidden) {
		// Find list of non-hidden children
		final List<Result> nonHiddenChildren = this.nonHiddenChildren();

		// Create the StringBuilder that this method will build from
		final StringBuilder tree = new StringBuilder();

		// Special case to handle the hidden Results
		if (!includeHidden && this.isHidden()) {

			// Delegate to its children
			for (final Result child : nonHiddenChildren) {
				tree.append(child.printResultSubTree(indentLevel, includeHidden));

				// If it's a non-final child in a list, mark it down here
				if (child != nonHiddenChildren.get(nonHiddenChildren.size() - 1)) {
					tree.append(",");
				}

				tree.append("\n");
			}
		}

		tree.append(tabs(indentLevel)).append("{\n");

		tree.append(tabs(indentLevel + 1)).append("\"type\": \"").append(type).append("\",\n");

		tree.append(tabs(indentLevel + 1)).append("\"data\": \"").append(data).append("\",\n");

		tree.append(tabs(indentLevel + 1)).append("\"left_recursion\": \"").append(this.getLRStatus().toString())
				.append("\",\n");

		tree.append(tabs(indentLevel + 1)).append("\"s\": ").append(startIdx + 1).append(",\n");

		tree.append(tabs(indentLevel + 1)).append("\"e\": ").append(endIdx + 1).append(children.isEmpty() ? "" : ",")
				.append("\n");

//		tree.append(tabs(indentLevel + 1)).append("end: ").append(derivation?).append("\n");

		final List<Result> childrenToPrint = includeHidden ? children : nonHiddenChildren;

		if (!childrenToPrint.isEmpty()) {
			tree.append(tabs(indentLevel + 1)).append("\"subs\": [\n");
			for (final Result child : childrenToPrint) {
				tree.append(child.printResultSubTree(indentLevel + 2, includeHidden));

				if (child != childrenToPrint.get(childrenToPrint.size() - 1)) {
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
		POSSIBLE("Possible"),
		/**
		 * The Pattern at this Derivation is definitely left-recursive. It called itself
		 * before we had a chance to finish its first match.
		 */
		DETECTED("Detected"),
		/**
		 * The Pattern at this Derivation is not left-recursive. We finished one full
		 * match without it calling itself.
		 */
		IMPOSSIBLE("Impossible");

		/** Display-friendly name for this enum value */
		private String displayName;

		/**
		 * Constructs an enum value with a display-friendly name
		 *
		 * @param displayName display-friendly name for this value
		 */
		LeftRecursionStatus(final String displayName) {
			this.displayName = displayName;
		}

		/**
		 * @return the enum's display name
		 */
		public String getDisplayName() {
			return displayName;
		}

		/**
		 * Gets the display name of this Enum by indexing into the display names array
		 * by the Enum's ordinal.
		 * 
		 * @return a display-friendly name for this Enum.
		 */
		@Override
		public String toString() {
			return getDisplayName();
		}
	}

}
