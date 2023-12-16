package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.pascal.intermediate.ICodeFactory;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.EnumSet;

import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.DO;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.MISSING_DO;

/**
 * <h1>WhileStatementParser</h1>
 *
 * <p>A parser for the WHILE statement</p>
 */
public class WhileStatementParser extends StatementParser {

  // Synchronizing set for DO.
  private static final EnumSet<PascalTokenType> DO_SET =
      StatementParser.STMT_START_SET.clone();
  static {
    DO_SET.add(DO);
    DO_SET.addAll(StatementParser.STMT_FOLLOW_SET);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public WhileStatementParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses a WHILE statement.
   *
   * @param token the initial token.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurs.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    token = nextToken(); // Consume the "WHILE".

    // Create "LOOP", "TEST", and "NOT" nodes.
    ICodeNode loopNode = ICodeFactory.createICodeNode(LOOP);
    ICodeNode breakNode = ICodeFactory.createICodeNode(TEST);
    ICodeNode notNode = ICodeFactory.createICodeNode(NOT);

    // The "LOOP" node adopts the "TEST" node as its first child.
    // The "TEST" node adopts the "NOT" node as its only child.
    loopNode.addChild(breakNode);
    breakNode.addChild(notNode);

    // Parse the expression.
    // The "NOT" node adopts the expression subtree as its only child.
    ExpressionParser expressionParser = new ExpressionParser(this);
    notNode.addChild(expressionParser.parse(token));

    // Synchronize at the "DO".
    token = synchronize(DO_SET);
    if (token.getType() == DO) {
      token = nextToken(); // Consume the "DO".
    } else {
      errorHandler.flag(token, MISSING_DO, this);
    }

    // Parse the statement.
    StatementParser statementParser = new StatementParser(this);
    loopNode.addChild(statementParser.parse(token));

    return loopNode;
  }
}
