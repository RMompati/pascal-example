package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.intermediate.Definition;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;
import com.rmompati.lang.pascal.intermediate.ICodeFactory;
import com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl;
import com.rmompati.lang.pascal.intermediate.symtableimpl.Predefined;

import java.util.EnumSet;

import static com.rmompati.lang.pascal.frontend.PascalTokenType.DOT;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.LEFT_BRACKET;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.IDENTIFIER_UNDEFINED;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.INVALID_IDENTIFIER_USAGE;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static com.rmompati.lang.pascal.intermediate.symtableimpl.DefinitionImpl.*;

public class VariableParser  extends StatementParser {

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
          ? parseSubscriptions(variableType)
          : parseField(variableType);

      token = nextToken();

      // Update the variable's type. The variable node adopts the SUBSCRIPTS or FIELD node.
      variableType = subFldNode.getTypeSpec();
      variableNode.addChild(subFldNode);
    }

    variableNode.setTypeSpec(variableType);

    return variableNode;
  }

  private ICodeNode parseField(TypeSpec variableType) {
    return null;
  }

  private ICodeNode parseSubscriptions(TypeSpec variableType) {
    return null;
  }
}
