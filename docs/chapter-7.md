# Parsing Control Statements

## Goals and Approach
> - Parsers in the frontend for Pascal control statements `WHILE`, `REPEAT`, `FOR`, `IF`, and `CASE`.
> - Flexible error recovery to ensure parsers still work, even when there are bad syntax errors in the source.


### Syntax Diagrams | Rules
> ```
> statement = compoundStatment
>  | assignmentStatement
>  | WHILE statement
>  | REPEAT statement
>  | FOR statement
>  | IF statement
>  | CASE statement
> ```
> ```
> WHILE statement = "WHILE" expression "DO" statement
> ```
> ```
> REPEAT statement = "REPEAT" statementList "UNTIL" expression
> ```
> ```
> FOR statement = "FOR" identifier ":=" expression to_keyword expression "DO" statement
> ```
> ```
> to_keyword = "TO" | "DOWNTO"
> ```
> ```
> IF statement = "IF" expression "THEN" ( ε | "ELSE" statement)
> ```
> ```
> CASE statement = "CASE" expression "OF" (ε | caseBody ) "END"
> ```
> ```
> caseBody = constantList ":" statment | caseBody ";" constantList ":" statment 
> ```
>
> ```
> constantList = constant | constandList "," constant
> ``` 
> ```
> constant = neg_op identifier | neg_op number | string
> ```
