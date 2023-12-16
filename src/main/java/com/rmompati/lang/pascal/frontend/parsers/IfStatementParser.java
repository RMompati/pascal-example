package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.pascal.intermediate.ICodeFactory;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.EnumSet;

import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl.IF;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.ELSE;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.THEN;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.MISSING_THEN;

/**
 * <h1>IfStatementParser</h1>
 *
 * <p>A parser for the IF statement</p>
 */
public class IfStatementParser extends StatementParser {

  private static final EnumSet<PascalTokenType> THEN_SET =
      StatementParser.STMT_START_SET.clone();
  static {
    THEN_SET.add(THEN);
    THEN_SET.addAll(StatementParser.STMT_FOLLOW_SET);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public IfStatementParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses an IF statement.
   *
   * @param token the initial token.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurs.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    token = nextToken(); // Consume the "IF".

    // Create the "IF" node.
    ICodeNode ifNode = ICodeFactory.createICodeNode(IF);

    // Parse the expression.
    // The "IF" node adopts the expression subtree as its first child.
    ExpressionParser expressionParser = new ExpressionParser(this);
    ifNode.addChild(expressionParser.parse(token));

    // Synchronize at the "THEN".
    token = synchronize(THEN_SET);
    if (token.getType() == THEN) {
      token = nextToken(); // Consume the "THEN"
    } else {
      errorHandler.flag(token, MISSING_THEN, this);
    }

    // Parse the "THEN" statement.
    StatementParser statementParser = new StatementParser(this);
    ifNode.addChild(statementParser.parse(token));
    token = currentToken();

    // Look for "ELSE"
    if (token.getType() == ELSE) {
      token = nextToken(); // Consume the "THEN"

      // Parse the "ELSE" statement.
      ifNode.addChild(statementParser.parse(token));
    }

    return ifNode;
  }
}
