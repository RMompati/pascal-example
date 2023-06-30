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
> nonEmptydeclarations  =   "CONST" constantDeclaration
>                       |   "TYPE" typeDeclaration
>                       |   "VAR" variableDeclaration
>                       |   nonEmptydeclarations
> ```
> ```
> constantDeclaration   =   constantDefinition ";"
>                       |   constantDefinition ";" constantDeclaration
> ```
> ```
> typeDeclaration       =   typeDefinition ";"
>                       |   typeDefinition ";" typeDeclaration
> ```
> ```
> variableDeclaration   =   variableDefinition ";"
>                       |   variableDefinition ";" variableDeclaration
> ```
> ```
> constantDefinition    =   identifier "=" constant
> ```
> ```
> variableDefinition    =   identifier
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
>                       |   "(" identifierType ")"
>                       |   constant ".." constant
> ```
> ```
> arrayType             =   "ARRAY" "[" identifierType "]" "OF" typeSpecification
> ```
> ```
> identifierType        =   identifier
>                       |   idetifier "," identifierType
> ```
> ```
> recordType            =   "RECORD" fields "END"
> ```
> ```
> fields                =   fieldDeclaration | fieldDeclaration "," fields
> ```
> ```
> fieldDeclaration      =   identifiers
>                       |   typeSpecification
> ```
> ```
> identifiers           =   identifier |
> ```
> 