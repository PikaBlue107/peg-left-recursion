# Representation of Capture Stack for LR matching

#### Monday, April 19, 2021, 1-2 PM

---

## Example

### Pattern Definitions:

    Expression = (Expression Plus Number) / Number
    Number = Digit+
    Digit = [0-9]
    Plus = "+"
    
### Example case:

	1+23
	
Resulting capture stack:

	| Type of Capture | Pattern  |
	|-----------------|----------|
	| Close Recursion | Expr     |--------\
	| Close           | Expr     |--------\
	| Close           | Number   |-----\  |
	| Close           | Digit    |--\  |  |
	| Open            | Digit    |--/  |  |
	| Close           | Digit    |--\  |  |
	| Open            | Digit    |--/  |  |
	| Open            | Number   |-----/  |
	| Close           | Plus     |--\     |
	| Open            | Plus     |--/     |
	| Close           | Expr     |--------|
	| Close           | Number   |-----\  |
	| Close           | Digit    |--\  |  |
	| Open            | Digit    |--/  |  |
	| Open            | Number   |-----/  |
	| Open Recursion  | Expr     |--------/
	|_________________|__________|

	
## Approaches for handling

### Approach A

1. Generate this stack into a separate structure?
2. After processing (on "Done:") traverse structure and push pure Open/Close
    Captures onto the standard Rosie Capture Stack

Pros:
 - Post-processing of rosie cap stack is the same
 
Cons:
 - Extra VM operations which may be wasted if match fails
 - Requires extra structure space
 
### Approach B

1. Generate this stack as-is onto the Rosie Capture Stack, with expanded Capture values
    for Open / Close Recursion
2. Amend Rosie Capture Processing (caploop) to handle these recursive Caps

Pros:
 - No extra allocation for data structure
 - Simpler VM, no extra work in case match fails
 
Cons:
 - Additional complexity in post-processing


## Possible optimizations

 - Store the grow iteration (0 for seed) at each Close Expr
 
 - Store total count of grow iterations in Open / Close Recursion
 
 - Store references to Open / Close Recursion 
 
 
 
 
 
 
 
 
 
 
 