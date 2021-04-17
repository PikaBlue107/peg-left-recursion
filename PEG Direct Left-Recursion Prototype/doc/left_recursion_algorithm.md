# NOTES from discussion with Melody

#### (Friday, April 2, 2021, 1:30pm-3:00pm)

---

## Simple Case

This case lists the typical example of a left-recursive expression, and how its matching may be implemented in the VM.

### Pattern Definitions:

    Expression = (Expression "+" Number) / Number
    Number = [0-9]


### Number match definition:
    Number:
      CHARSET '0'-'9'
      RET

### Expression match definition:
Expression will compile to a vector of bytecode that ends in a Return.

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

---

## Full case

This case contains more alternatives that introduce complexities into the above algorithm.

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

## EDGE CASES:

### Non-expanding LR calls
LR calls that do not expand the match, e.g. using predicates, e.g.

    Expr = Expr "+" Num
    Expr = Expr >"foo"      // Illegal? Or transform into: Expr = >"foo"
    
or

    Expr = Expr "+" Num
    Expr = Expr "!"?
    
#### Remarks

(Melody)

Overall, these patterns should not be allowed to be compiled directly, 
for the same reason that an infinite repetition of a nullable pattern should not be allowed to compile - 
they will loop forever!

The first example doesn't serve any purpose, as an expression match is already found when the second choice is attempted.
Whether Expr is followed by "foo" makes no difference about whether it matches or what it consumes.

The second example *may* match another character, but this should be corrected to `Expr = Expr "!"` to properly expand the match.
An optional expansion causes infinite looping.
    
### Mixed Recursion and Left-Recursion
Normal recursion, in which there is a LR rule but also recursive but not LR, e.g.

    Expr = Expr "+" Num
    Expr = "(" Expr ")"
    Expr = Num
    
or

    S = S "c"
    S = "a" S
    S = "b"
    
or

    Expr = Expr "+" Expr
    Expr = Num
    
#### Remarks

(Melody)

I don't believe this is an issue for matching.
If the recursive call is made, it will be handled when attempting to find a seed for this pattern.
That recursive call may lead to yet another, or it may close the match,
which would then go on to attempt to expand the *smallest* and *latest* match left-recursively.

The second example *may* pose some issues for associativity. For a string:

    aabc

The correct PEG parse tree should be:

        S
       / \
      S  "c"
     / \
    "a" S
       / \
      "a" S
          |
         "b"

However, with the current algorithm, the *smallest* and *latest* match of `S` will be the first to be expanded by left-recursion:

      S
     / \
    "a" S
       / \
      "a" S
         / \
        S  "c"
        |
       "b"

The problem here is that it's the *most recent* call to S that is expended left-recursively, instead of the *first* call to S.

A possible solution may be to somehow create a queue of recursive calls of S to attempt to expand with left-recursion?
With this solution, the initial call to S that matches "a" and then recursively calls itself afterwards would be the first to be expanded,
preserving PEG's greedy nature.

The same issue is exhibited by the third example, which is the canonical example of this issue by Tratt.
I'm not confident that the proposed solution above would handle the case where the recursive call and the left-recursive call
are in the same ordered choice. This must be investigated further (likely by analyzing Tratt's proposed solution).

### Preceding nullable nonterminals before LR
LR calls that are preceded by nullable nonterminals that may or may not match, e.g.

    Expr = "-"? Expr "+" Num
 
or

    S = A S "c" / "s"
    A = "a"?
    
or

    S = A B S "c" / "s"
    A = "a"?
    B = "b"?
    
#### Remarks

(Melody)

I'm not sure how to handle this. Possibly a similar solution to the above edge case? Kinda stumped at the moment.

    
### Optional LR
LR calls that are optional within a match definition, e.g.

    Expr = { Expr "+" }? Num
    
or

    S = S? "s"
    
#### Remarks

(Melody)

This definition is equivalent to `S = S "s" / "s"`, which is a well-defined means of handling left-recursion.

Perhaps this can be generalized as a common way to approach optional LR?
Does this have consequences if we just convert it without any special care?
    
### LR uses that shrink the match
By use of left-recursive predicates, these definitions can require that a full predicate is found,
but only consume part of it, e.g.

    Expr = >Expr Num
    
or

    S = >S "s" / "sos"
    
#### Remarks

(Melody)

In this situation, there is no point to expanding the >Expr left-recursive match.
So, this definition should only be matched once, without attempting to expand it.
