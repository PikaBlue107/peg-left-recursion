package patterns.definition;

import patterns.general.Pattern;
import structure.Derivation;
import structure.Result;

public class SimpleExpression extends Pattern {

	private static final SimpleNumber num = new SimpleNumber();
	
	@Override
	protected Result<?> match(Derivation derivation) {
		Result<?> result;
		result = tryFullExpression(derivation);
		if (result.isSuccess())
			return result;
		
		return num.lazyMatch(derivation);
	}
	
	private Result<?> tryFullExpression(Derivation derivation) {
		Result<?> result;
		result = this.lazyMatch(derivation);
		if( !result.isSuccess())
			return result;
		result = this.matchPlus(result.getDerivation());
		if (!result.isSuccess())
			return result;
		result = num.lazyMatch(result.getDerivation());
		return result;
	}
	
	private Result<?> matchPlus(Derivation derivation) {
		if(derivation.getChResult().isSuccess() && derivation.getChResult().getValue() == '+') {
			System.out.println("Matched [" + derivation.getChResult().getValue() + "]");
			return new Result<Object>(true, "+", derivation.getChResult().getDerivation());
		} else {
			return Result.FAIL();
		}
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
