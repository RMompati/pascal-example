package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.intermediate.ICodeFactory;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.EnumSet;

import static com.rmompati.lang.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static com.rmompati.lang.intermediate.icodeimpl.ICodeNodeTypeImpl.ASSIGN;
import static com.rmompati.lang.intermediate.icodeimpl.ICodeNodeTypeImpl.VARIABLE;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.COLON_EQUALS;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.MISSING_COLON_EQUALS;

public class AssignmentStatementParser extends StatementParser {

  // Synchronization set for the ":=" token.
  private static final EnumSet<PascalTokenType> COLON_EQUALS_SET =
      ExpressionParser.EXPR_START_SET.clone();

  static {
    COLON_EQUALS_SET.add(COLON_EQUALS);
    COLON_EQUALS_SET.addAll(StatementParser.STMT_FOLLOW_SET);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public AssignmentStatementParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parse a statement.
   * To be overridden by the specialized statement parser subclasses.
   *
   * @param token the initial token.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurs.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    // Create ASSIGN node.
    ICodeNode assignNode = ICodeFactory.createICodeNode(ASSIGN);

    // Look up the identifier in the symbol table stack.
    // Enter the identifier into the table if it's not found
    String targetName = token.getText().toLowerCase();
    SymTableEntry targetId = symTabStack.lookup(targetName);
    if (targetId == null) {
      targetId = symTabStack.enterLocal(targetName);
    }
    targetId.appendLineNumber(token.getLineNum());

    token = nextToken(); // Consume the identifier token.

    // Create the variable node and set its name attribute.
    ICodeNode variableNode = ICodeFactory.createICodeNode(VARIABLE);
    variableNode.setAttribute(ID, targetId);

    // ASSIGN node adopts the variable node as its first child.
    assignNode.addChild(variableNode);

    // Synchronize on the  := token
    token = synchronize(COLON_EQUALS_SET);
    if (token.getType() == COLON_EQUALS) {
      token = nextToken(); // Consume :=
    } else {
      errorHandler.flag(token, MISSING_COLON_EQUALS, this);
    }

    // Parse the expression. The ASSIGN node adopts the expression's node as it's second child.
    ExpressionParser expressionParser = new ExpressionParser(this);
    assignNode.addChild(expressionParser.parse(token));
    return assignNode;
  }
}
