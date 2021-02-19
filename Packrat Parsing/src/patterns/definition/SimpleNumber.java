package patterns.definition;

import patterns.general.Pattern;
import structure.Derivation;
import structure.Result;

public class SimpleNumber extends Pattern {

	@Override
	protected Result match(final Derivation derivation) {
		if (!derivation.getChResult().isSuccess()) {
			return Result.FAIL();
		}

		if (!Character.isDigit(derivation.getChResult().getData().charAt(0))) {
			return Result.FAIL();
		}

		System.out.println("Matched [" + derivation.getChResult().getData() + "]");

		final Result priorResult = new Result(true, null, derivation.getChResult().getDerivation());
		priorResult.setType("Number");
		priorResult.setData(derivation.getChResult().getData());
		priorResult.setStartIdx(derivation.getIndex());
		priorResult.setEndIdx(derivation.getIndex() + 1);

		// While next step is a valid character
		while (priorResult.getDerivation().getChResult().isSuccess()) {
			if (!Character.isDigit(priorResult.getDerivation().getChResult().getData().charAt(0))) {
				return priorResult;
			}
			System.out.println("Matched [" + priorResult.getDerivation().getChResult().getData() + "]");
			priorResult.setDerivation(priorResult.getDerivation().getChResult().getDerivation());
			priorResult.setData(priorResult.getData() + priorResult.getDerivation().getChResult().getData());
			priorResult.setEndIdx(priorResult.getDerivation().getIndex());
		}

		return priorResult;
	}

	/**
	 * Unique by instance
	 */
	@Override
	public int hashCode() {
		return this.getID();
	}

	/**
	 * Must be same instance
	 */
	@Override
	public boolean equals(final Object obj) {
		return this == obj;
	}

}
