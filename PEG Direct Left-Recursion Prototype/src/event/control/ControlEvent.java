/**
 * 
 */
package event.control;

import event.ParseEvent;
import structure.InputContext;

/**
 * @author Melody Griesen
 *
 */
public abstract class ControlEvent extends ParseEvent {

	public ControlEvent(final InputContext context, final int startIdx, final int length, final String detail) {
		super(context, startIdx, length, detail);
	}

}
