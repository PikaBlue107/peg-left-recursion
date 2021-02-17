package patterns.definition;

import patterns.general.Pattern;
import structure.Derivation;
import structure.Result;

public class SimpleNumber extends Pattern {

	@Override
	protected Result<?> match(Derivation derivation) {
		if (!derivation.getChResult().isSuccess())
			return Result.FAIL();
		
		if (!Character.isDigit(derivation.getChResult().getValue()))
			return Result.FAIL();
		
		System.out.println("Matched [" + derivation.getChResult().getValue() + "]");
		
		Result<?> priorResult = new Result<Object>(true, null, derivation.getChResult().getDerivation());
		priorResult.setType("Number");
		
		// While next step is a valid character
		while(priorResult.getDerivation().getChResult().isSuccess()) {
			if (!Character.isDigit(priorResult.getDerivation().getChResult().getValue()))
				return priorResult;
			System.out.println("Matched [" + priorResult.getDerivation().getChResult().getValue() + "]");
			priorResult.setDerivation(priorResult.getDerivation().getChResult().getDerivation());
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
	public boolean equals(Object obj) {
		return this == obj;
	}

	
	
}
