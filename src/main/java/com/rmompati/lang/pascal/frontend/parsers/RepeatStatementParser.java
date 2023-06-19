package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.intermediate.ICodeFactory;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.pascal.frontend.PascalParserTD;

import static com.rmompati.lang.intermediate.icodeimpl.ICodeNodeTypeImpl.LOOP;
import static com.rmompati.lang.intermediate.icodeimpl.ICodeNodeTypeImpl.TEST;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.UNTIL;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.MISSING_UNTIL;

/**
 * <h1>RepeatStatementParser</h1>
 *
 * <p>A parser for the REPEAT statement</p>
 */
public class RepeatStatementParser extends StatementParser {
  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public RepeatStatementParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses a REPEAT statement.
   *
   * @param token the initial token.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurs.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    token = nextToken(); // Consume the REPEAT token.

    // Create the LOOP and TEST nodes.
    ICodeNode loopNode = ICodeFactory.createICodeNode(LOOP);
    ICodeNode testNode = ICodeFactory.createICodeNode(TEST);

    // Parse the statement list terminated by the UNTIL token.
    StatementParser statementParser = new StatementParser(this);
    statementParser.parseList(token, loopNode, UNTIL, MISSING_UNTIL);
    token = currentToken();

    // Parse the expression. The TEST node adopts the expression subtree as its only child.
    ExpressionParser expressionParser = new ExpressionParser(this);
    testNode.addChild(expressionParser.parse(token));
    loopNode.addChild(testNode);

    return loopNode;
  }
}
