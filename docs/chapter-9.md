# Parsing Declarations

## Goals and Approach
> - Parsers in the front end for Pascal constant definitions, type definitions,
> variable declarations, and type specifications.
> - Additions to the symbol table to contain type information.

### Pascal Declarations
> ```
> block = declarations compoundStatement
> ```
> ```
> declarations          =   nonEmptydeclarations
>                       |   ε
> ```
> ```
> nonEmptydeclarations  =   "CONST" constants
>                       |   "TYPE" types
>                       |   "VAR" variables
>                       |   nonEmptydeclarations
> ```
> ```
> constants             =   constantDefinition ";"
>                       |   constantDefinition ";" constants
> ```
> ```
> types                 =   typeDefinition ";"
>                       |   typeDefinition ";" types
> ```
> ```
> variables             =   variableDeclaration ";"
>                       |   variableDeclaration ";" variables
> ```
> ```
> constantDefinition    =   identifier "=" constant
> ```
> ```
> typeDefinition        =   identifier "=" typeSpecification
> ```
> ```
> typeSpecification     =   simpleType
>                       |   arrayType
>                       |   recordType
> ```
> ```
> simpleType            =   identifier
>                       |   "(" identifiers ")"
>                       |   constant ".." constant
> ```
> ```
> arrayType             =   "ARRAY" "[" identifiers "]" "OF" typeSpecification
> ```
> ```
> recordType            =   "RECORD" fields "END"
> ```
> ```
> fields                =   fieldDeclaration | fieldDeclaration "," fields
> ```
> ```
> fieldDeclaration      =   identifiers ":" typeSpecification
> ```
> ```
> variableDeclaration   =   identifiers ":" typeSpecification
> ```
> ```
> identifiers           =   identifier | identifier "," identifiers
> ```
