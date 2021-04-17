# NOTES from discussion with Melody

#### (Friday, April 2, 2021, 1:30pm-3:00pm)
---

## Full case

### Pattern definitions:
    Expression = (Expression "+" Number) / (Expression "X" Number) / Number / Var
    Number = [0-9]+
    Var = [a-z]

Expression will compile to a vector of bytecode that ends in a Return.

### Number match definition:
    Number:
    	// Must have at least one match
      	CHARSET [0-9]
    RepNumber:
      	// Possibly more than one
      	CHOICE: On failure, go to DoneNumber
      	// TODO: Continue
      	
    DoneNumber:
    	RET

### Expression match definition:
    Expression:
      // When some other pattern calls 'Expression', vm jumps to this address.

      // We know that any left-recursive rule for Expression will fail at first, so
      // we should generate code for each non-left-recursive alternative.
      // There are two: Number and Var

      grow[0].start = <CurrentPosition>

      CHOICE: On failure, goto TryVar
      CALL Number
      // If that call were to fail, we would jump to TryVar because that is on top
      // of the backtrack stack.  Otherwise, success!
      COMMIT and jump to SeedSuccess

    TryVar:
      // No CHOICE because it's the last possible alternative
      CALL Var

    SeedSuccess:
      // Yay! We have matched Expression to one of the non-LR rules!
      grow[0].end = <CurrentPosition>
      i = 0

    TryPlus:

      // Attempt the first LR rule: Expression "+" Number

      CHOICE: On failure, goto TryTimes
      CHAR '+'
      CALL Number

      // Now at end of a LR rule and it has succeeded
      // (1) Check to make sure that we have extended the match, not shortened it.
      //     If CurrentPosition < grow.end then this LR call has failed.
      //     There may be other rules to try.  Business as usual?
      //     NOTE: Where should we do this check?  Suppose user writes LR and non-LR
      //     alternatives, interleaved...
      // (2) Update growing data structure, which is a reverse capture list

      COMMIT and jump to GrowSuccess     // Pop the choice off stack because the seed was successfully grown

    TryTimes:
      CHOICE: On failure, goto Done
      Match character "X"
      Call Number

      COMMIT and jump to GrowSuccess     // Pop the choice off stack because the seed was successfully grown

    GrowSuccess:
      i++
      grow[i].start = grow[0].start     // pure LR call
      grow[i].end = CurrentPosition
      GOTO TryPlus                      // jump back to attempt the first LR rule

    Done:
      // If there were no captures, we could just RETURN here.
      // But, captures that accrued during the match to Expression must be saved
      // onto the global capture stack before we return.
      PUSH results onto capture stack (backwards from i down to 0)
      Return


---

#### EDGE CASES:

LR calls that do not expand the match, e.g. using predicates

    Expr = Expr "+" Num
    Expr = Expr >"foo"      // Illegal? Or transform into: Expr = >"foo"

Normal recursion, in which there is a LR rule but also recursive but not LR,
e.g.

  Expr = Expr "+" Num
  Expr = "(" Expr ")"

---

## Simple Case

    Expression = (Expression "+" Number) / Number
    Number = [0-9]

Expression will compile to a vector of bytecode that ends in a Return.

### Number match definition:
    Number:
      CHARSET '0'-'9'
      RET

### Expression match definition:
    Expression:
      	// When some other pattern calls 'Expression', vm jumps to this address.

		// TODO: Instantiate grow array
		OPENGROW <CurrentPosition>
		
		// TODO: Revisit
      	grow[0].start = <CurrentPosition>

      	// We know that any left-recursive rule for Expression will fail at first, so
      	// we should generate code for each non-left-recursive alternative.
      	// There is just one: Number
      	
      	
		

		// Delegate to non-LR alternatives
      	CALL Number

    SeedSuccess:
      	// Yay! We have matched Expression to one of the non-LR rules!
      	
      	// TODO: Revisit
      	grow[0].end = <CurrentPosition>
 		// TODO: Revisit
      	i = 0

	// BEGIN LEFT-RECURSIVE ATTEMPTS
	
	// Attempt first LR alternative
    TryPlus:

      	// Attempt the first LR rule: Expression "+" Number
		
		// Attempt this alternative, if fail, finish match
      	CHOICE: On failure, goto Done
      	// Skip LR CALL, delegate to rest of definition 
      	CHAR '+'
      	CALL Number

      	// Now at end of a LR rule and it has succeeded
      	
      	// Because extending a seed does not re-evaluate the full pattern,
      	// we dont' have to worry about catching a "shorter" match.
      	
      	// TODO: Update growing data structure, which is a reverse capture list

      	COMMIT and jump to GrowSuccess     // Pop the choice off stack because the seed was successfully grown

    GrowSuccess:
      	i++
      	grow[i].start = grow[0].start     // pure LR call
      grow[i].end = CurrentPosition
      GOTO TryPlus                      // jump back to attempt the first LR rule

    Done:
      // If there were no captures, we could just RETURN here.
      // But, captures that accrued during the match to Expression must be saved
      // onto the global capture stack before we return.
      PUSH results onto capture stack (backwards from i down to 0)
      Return
