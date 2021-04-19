# Representation of Capture Stack for LR matching

#### Monday, April 19, 2021, 1-2 PM

---

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
	