package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.frontend.TokenType;
import com.rmompati.lang.intermediate.*;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;
import com.rmompati.lang.pascal.intermediate.ICodeFactory;
import com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl;
import com.rmompati.lang.pascal.intermediate.symtableimpl.Predefined;
import com.rmompati.lang.pascal.intermediate.typeimpl.TypeChecker;
import com.rmompati.lang.pascal.intermediate.typeimpl.TypeFormImpl;

import java.util.EnumSet;

import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl.SUBSCRIPTS;
import static com.rmompati.lang.pascal.intermediate.symtableimpl.DefinitionImpl.*;
import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeKeyImpl.*;

public class VariableParser  extends StatementParser {

  // Synchronization set for the ']' token.
  private static final EnumSet<PascalTokenType> RIGHT_BRACKET_SET =
      EnumSet.of(RIGHT_BRACKET, EQUALS, SEMICOLON);

  private final EnumSet<PascalTokenType> SUBSCRIPT_FIELD_START_SET =
      EnumSet.of(LEFT_BRACKET, DOT);

  /**
   * Constructor for subclasses.
   *
   * @param parent the parent parser.
   */
  public VariableParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses a variable.
   * @param token the initial token.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurs.
   */
  public ICodeNode parse(Token token) throws Exception {

    String name = token.getText();
    SymTableEntry variableId = symTabStack.lookup(name);

    // If not found, flag the error and enter the identifier as undefined identifier with an undefined type.
    if (variableId == null) {
      errorHandler.flag(token, IDENTIFIER_UNDEFINED, this);
      variableId = symTabStack.enterLocal(name);
      variableId.setDefinition(UNDEFINED);
      variableId.setTypeSpec(Predefined.undefinedType);
    }

    return parse(token, variableId);
  }

  /**
   * Parses a variable.
   * @param token the initial token.
   * @param variableId the symbol table entry of the variable identifier.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurs.
   */
  public ICodeNode parse(Token token, SymTableEntry variableId) throws Exception {
    // Check how the variable was defined.
    Definition dfnCode = variableId.getDefinition();
    if ((dfnCode != VARIABLE) && (dfnCode != VALUE_PARAM) && (dfnCode != VAR_PARAM)) {
      errorHandler.flag(token, INVALID_IDENTIFIER_USAGE, this);
    }

    variableId.appendLineNumber(token.getLineNum());

    ICodeNode variableNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.VARIABLE);
    variableNode.setAttribute(ID, variableId);

    token = nextToken(); // Consume the identifier.

    // Parse array subscriptions or record fields.
    TypeSpec variableType = variableId.getTypeSpec();
    while (SUBSCRIPT_FIELD_START_SET.contains(token.getType())) {
      ICodeNode subFldNode = token.getType() == LEFT_BRACKET
          ? parseSubscripts(variableType)
          : parseField(variableType);

      token = nextToken();

      // Update the variable's type. The variable node adopts the SUBSCRIPTS or FIELD node.
      variableType = subFldNode.getTypeSpec();
      variableNode.addChild(subFldNode);
    }

    variableNode.setTypeSpec(variableType);

    return variableNode;
  }

  /**
   * Parses a record field.
   * @param variableType the type of the record variable.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurs.
   */
  private ICodeNode parseField(TypeSpec variableType) throws Exception {
    // Create a field node.
    ICodeNode fieldNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.FIELD);

    Token token = nextToken();
    TokenType tokenType = token.getType();
    TypeForm variableForm = variableType.getForm();

    if ((tokenType == IDENTIFIER) && (variableForm == TypeFormImpl.RECORD)) {
      SymTable symTable = (SymTable) variableType.getAttribute(RECORD_SYMTAB);
      String fieldName = token.getText();
      SymTableEntry fieldId = symTable.lookup(fieldName);

      if (fieldId != null) {
        variableType = fieldId.getTypeSpec();
        fieldId.appendLineNumber(token.getLineNum());

        fieldNode.setAttribute(ID, fieldId);
      } else {
        errorHandler.flag(token, INVALID_FIELD, this);
      }
    } else {
      errorHandler.flag(token, INVALID_FIELD, this);
    }

    token = nextToken(); // Consume the field identifier

    fieldNode.setTypeSpec(variableType);
    return fieldNode;
  }

  /**
  * Parses a set of comma-separated subscript expressions.
  * @param variableType the type of the array variable.
  * @return the root node of the generated parse tree.
  * @throws Exception if an error occurs.
  */
  private ICodeNode parseSubscripts(TypeSpec variableType) throws Exception {
    Token token;
    ExpressionParser expressionParser = new ExpressionParser(this);

    // Crete a SUBSCRIPTS node.
    ICodeNode subscriptsNode = ICodeFactory.createICodeNode(SUBSCRIPTS);

    do {
      token = nextToken();

      if (variableType.getForm() == TypeFormImpl.ARRAY) {
        // Parse the subscript expression
        ICodeNode exprNode = expressionParser.parse(token);
        TypeSpec exprType = exprNode != null ? exprNode.getTypeSpec() : Predefined.undefinedType;

        // The subscript expression type must be assignment compatible with the array index type
        TypeSpec indexType = (TypeSpec) variableType.getAttribute(ARRAY_INDEX_TYPE);
        if (!TypeChecker.areAssignmentCompatible(indexType, exprType)) {
          errorHandler.flag(token, INCOMPATIBLE_TYPES, this);
        }

        subscriptsNode.addChild(exprNode);

        variableType = (TypeSpec) variableType.getAttribute(ARRAY_ELEMENT_TYPE);
      } else {
        errorHandler.flag(token, TOO_MANY_SUBSCRIPTS, this);
        expressionParser.parse(token);
      }

      token = currentToken();
    } while (token.getType() == COMMA);

    // Synchronize the "]" token
    token = synchronize(RIGHT_BRACKET_SET);
    if (token.getType() == RIGHT_BRACKET) {
      token = nextToken(); // Consume the "]" token.
    } else {
      errorHandler.flag(token, MISSING_RIGHT_BRACKET, this);
    }

    subscriptsNode.setTypeSpec(variableType);
    return subscriptsNode;
  }
}
