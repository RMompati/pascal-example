package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.EofToken;
import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.frontend.TokenType;
import com.rmompati.lang.intermediate.ICodeFactory;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;
import com.rmompati.lang.pascal.frontend.error.PascalErrorCode;

import java.util.EnumSet;

import static com.rmompati.lang.intermediate.icodeimpl.ICodeKeyImpl.LINE;
import static com.rmompati.lang.intermediate.icodeimpl.ICodeNodeTypeImpl.NO_OP;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.MISSING_SEMICOLON;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.UNEXPECTED_TOKEN;

/**
 * <h1>StatementParser</h1>
 *
 * <p>A base parser for a Pascal Statement.</p>
 */
public class StatementParser extends PascalParserTD {

  // Synchronization set for starting a statement.
  protected static final EnumSet<PascalTokenType> STMT_START_SET =
      EnumSet.of(BEGIN, CASE, FOR, IF, REPEAT, WHILE, IDENTIFIER, SEMICOLON);

  // Synchronization set for following a statement.
  protected static final EnumSet<PascalTokenType> STMT_FOLLOW_SET =
      EnumSet.of(SEMICOLON, END, ELSE, UNTIL, DOT);

  /**
   * Constructor.
   * @param parent the parent parser.
   */
  public StatementParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parse a statement.
   * To be overridden by the specialized statement parser subclasses.
   * @param token the initial token.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurs.*/
  public ICodeNode parse(Token token) throws Exception {
    ICodeNode statementNode = null;
    switch (((PascalTokenType) token.getType())) {
      case BEGIN: {
        CompoundStatementParser compoundParser = new CompoundStatementParser(this);
        statementNode = compoundParser.parse(token);
        break;
      }
      case IDENTIFIER: {
        AssignmentStatementParser assignmentParser = new AssignmentStatementParser(this);
        statementNode = assignmentParser.parse(token);
        break;
      }
      default: {
        statementNode = ICodeFactory.createICodeNode(NO_OP);
      }
    }

    // Set the current line number as an attribute.
    serLineNumber(statementNode, token);

    return statementNode;
  }

  /**
   * Sets the current line number as a statement node attribute.
   * @param node the ICode node.
   * @param token the token.
   */
  protected void serLineNumber(ICodeNode node, Token token) {
    if (node != null) {
      node.setAttribute(LINE, token.getLineNum());
    }
  }

  /**
   * Parse a statement list.
   * @param token the current token.
   * @param parentNode the parent node of the statement list.
   * @param terminator the token type of the node that terminates the list.
   * @param errorCode the error code if the terminator token is missing.
   * @throws Exception if an error occurs.
   */
  protected void parseList(Token token, ICodeNode parentNode, PascalTokenType terminator,
                           PascalErrorCode errorCode) throws Exception {
    // Loop to parse each statement until the "END" token, or end of source file.
    while (!(token instanceof EofToken) && (token.getType() != terminator)) {
      // parse a statement. The parent node adopts the statement node.
      ICodeNode statementNode = parse(token);
      parentNode.addChild(statementNode);

      token = currentToken();
      TokenType tokenType = token.getType();

      // Look for the semicolon between statements.
      if (tokenType == SEMICOLON) {
        token = nextToken();
      } else if (tokenType == IDENTIFIER) {
        errorHandler.flag(token, MISSING_SEMICOLON, this);
      } else if (tokenType != terminator) {
        errorHandler.flag(token, UNEXPECTED_TOKEN, this);
        token = nextToken(); // Consume the unexpected token.
      }
    }
    // Look for the terminator token.
    if (token.getType() == terminator) {
      token = nextToken();
    } else {
      errorHandler.flag(token, errorCode, this);
    }
  }
}
