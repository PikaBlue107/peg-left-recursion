/**
 * 
 */
package event.pattern;

import patterns.general.Pattern;
import structure.InputContext;
import structure.Result;

/**
 * @author Melody Griesen
 *
 */
public class MetaMatchEvent extends PatternEvent {

	/**
	 * Possible types for a MetaMatchEvent.
	 * 
	 * @author Melody Griesen
	 *
	 */
	public enum MetaMatchEventType {
		/** Meta matching for pattern is about to be attempted. */
		BEGIN_PREP("Begin Preparation"),
		/** Meta matching identifies no saved result, and will calculate one. */
		RUN_MATCH("Run Match"),
		/** Meta matching identifies a saved result. */
		ASSUME_RESULT("Assume Result"),
		/** Meta matching identifies a match call as left-recursive. */
		IDENTIFY_LR("ID Left Recursion"),
		/** Meta matching begins growing a left-recursive seed. */
		BEGIN_GROW("Begin Growing");

		/** Stores a display-friendly name for this enum. */
		private final String displayName;

		/**
		 * Constructs each Enum value with a display-friendly name
		 *
		 * @param name the name to display for this Enum value
		 */
		MetaMatchEventType(final String name) {
			displayName = name;
		}

		/**
		 * Retrieves the display name for this enum
		 *
		 * @return this enum's display name
		 */
		public String getDisplayName() {
			return displayName;
		}
	}

	/**
	 * Type of meta match event this object represents. One possible step in the
	 * meta-matching process.
	 */
	private final MetaMatchEventType type;

	/**
	 * Constructs a general MetaMatchEvent with the provided pattern, index, and
	 * type.
	 *
	 * @param context the context used for matching
	 * @param pattern the pattern being matched
	 * @param index   the index at which matching occurs
	 * @param type    the type of event this meta match event is
	 */
	public MetaMatchEvent(final InputContext context, final Pattern pattern, final int index,
			final MetaMatchEventType type) {
		super(context, index, 0, "for " + pattern.toString());
		this.type = type;
	}

	/**
	 * Constructs a MetaMatchEvent for assuming a result given by the result
	 * provided.
	 *
	 * @param context the context that's being matched
	 * @param pattern the pattern that's used for matching
	 * @param result  the result that was acquired
	 */
	public MetaMatchEvent(final InputContext context, final Pattern pattern, final Result result,
			final MetaMatchEventType type) {
		super(context, result.getStartIdx(), result.getData().length(),
				(type == MetaMatchEventType.ASSUME_RESULT
						? "that pattern " + pattern.getType()
								+ (result.isSuccess() ? " matches \"" + result.getData() + "\"" : " does not match")
						: "for " + pattern.toString()));
		this.type = type;
	}

	/**
	 * Returns the display name of this MetaMatchEvent's type
	 * 
	 * @return the String display name for the type
	 */
	@Override
	public String getType() {
		return type.getDisplayName();
	}

}
