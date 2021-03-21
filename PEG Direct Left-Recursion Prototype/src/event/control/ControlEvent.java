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

	public ControlEvent(final InputContext context, final int startIdx, final int endIdx, final String detail) {
		super(context, startIdx, endIdx, detail);
	}

	public ControlEvent(final String affected, final int startIdx, final int endIdx, final String detail) {
		super(affected, startIdx, endIdx, detail);
	}

}
