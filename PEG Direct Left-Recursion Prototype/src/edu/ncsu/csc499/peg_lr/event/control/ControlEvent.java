/**
 * 
 */
package edu.ncsu.csc499.peg_lr.event.control;

import edu.ncsu.csc499.peg_lr.event.ParseEvent;
import edu.ncsu.csc499.peg_lr.structure.InputContext;

/**
 * @author Melody Griesen
 *
 */
public abstract class ControlEvent extends ParseEvent {

	public ControlEvent(final InputContext context, final int startIdx, final int length, final String detail) {
		super(context, startIdx, length, detail);
	}

}
