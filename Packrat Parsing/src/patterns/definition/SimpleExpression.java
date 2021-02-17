package patterns.definition;

import patterns.general.Pattern;
import structure.Derivation;
import structure.Result;

public class SimpleExpression extends Pattern {

	private static final SimpleNumber num = new SimpleNumber();
	
	@Override
	protected Result match(Derivation derivation) {
		Result result;
		// Ordered choice
		
		// Attempt full expression
		result = tryFullExpression(derivation);
		if (result.isSuccess())
			return result;
		
		// Match num
		result = num.lazyMatch(derivation);
		

		Result expression = new Result(true, null, null);
		expression.setStartIdx(derivation.getIndex());
		expression.setType("Expression");
//		expression.setValue(result.getValue());
		expression.setDerivation(result.getDerivation());
		expression.addChild(result);
		expression.setData(result.getData());
		expression.setEndIdx(result.getEndIdx());
		return expression;
	}
	
	private Result tryFullExpression(Derivation derivation) {
		Result expression = new Result(true, null, null);
		expression.setStartIdx(derivation.getIndex());
		expression.setType("Expression");
		expression.setData("");
		Result result;
		
		// Sequence
		
		// Match recursive Expression
		result = this.lazyMatch(derivation);
		if( !result.isSuccess())
			return result;
		expression.addChild(result);
		expression.setData(expression.getData() + result.getData());
		
		// Match plus
		result = this.matchPlus(result.getDerivation());
		if (!result.isSuccess())
			return result;
		expression.setData(expression.getData() + result.getData());
		
		// Match Num
		result = num.lazyMatch(result.getDerivation());
		if ( !result.isSuccess())
			return result;
		expression.addChild(result);
		expression.setData(expression.getData() + result.getData());
		expression.setDerivation(result.getDerivation());
		expression.setEndIdx(result.getEndIdx());
		return expression;
	}
	
	private Result matchPlus(Derivation derivation) {
		if(derivation.getChResult().isSuccess() && 
				derivation.getChResult().getData().charAt(0) == '+') {
			System.out.println("Matched [" + derivation.getChResult().getData() + "]");
			return new Result(true, "+", derivation.getChResult().getDerivation());
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
