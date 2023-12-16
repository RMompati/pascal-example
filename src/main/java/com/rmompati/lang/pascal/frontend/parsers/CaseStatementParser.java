package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.EofToken;
import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.frontend.TokenType;
import com.rmompati.lang.pascal.intermediate.ICodeFactory;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.EnumSet;
import java.util.HashSet;

import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeKeyImpl.VALUE;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;

/**
 * <h1>CaseStatementParser</h1>
 *
 * <p>A parser for the CASE statement</p>
 */
public class CaseStatementParser extends StatementParser {

  // Synchronization set for starting a "CASE" option constant.
  private static final EnumSet<PascalTokenType> CONSTANT_START_SET =
      EnumSet.of(IDENTIFIER, INTEGER, PLUS, MINUS, STRING);

  // Synchronization set for "OF"
  private static final EnumSet<PascalTokenType> OF_SET =
      CONSTANT_START_SET.clone();
  static {
    OF_SET.add(OF);
    OF_SET.addAll(StatementParser.STMT_FOLLOW_SET);
  }

  // Synchronization set for COMMA.
  private static final EnumSet<PascalTokenType> COMMA_SET =
      CONSTANT_START_SET.clone();
  static {
    COMMA_SET.add(COMMA);
    COMMA_SET.add(COLON);
    COMMA_SET.addAll(StatementParser.STMT_START_SET);
    COMMA_SET.addAll(StatementParser.STMT_FOLLOW_SET);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public CaseStatementParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parse a "CASE" statement.
   *
   * @param token the initial token.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurs.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    token = nextToken(); // Consume the "CASE"

    // Create the "SELECT" node.
    ICodeNode selectNode = ICodeFactory.createICodeNode(SELECT);

    // Parse the "CASE" expression.
    ExpressionParser expressionParser = new ExpressionParser(this);
    selectNode.addChild(expressionParser.parse(token));

    // Synchronize at the "OF"
    token = synchronize(OF_SET);
    if (token.getType() == OF) {
      token = nextToken(); // Consume the "OF"
    } else {
      errorHandler.flag(token, MISSING_OF, this);
    }

    // Set of "CASE" branch constants.
    HashSet<Object> constantSet = new HashSet<>();

    // Loop to parse each "CASE" branch until "END" token.
    while (!(token instanceof EofToken) && (token.getType() != END)) {
      // The "SELECT" node adopts the "CASE" branch subtree.
      selectNode.addChild(parseBranch(token, constantSet));
      
      token = currentToken();
      TokenType tokenType = token.getType();

      // Look for the semicolon between case branches.
      if (tokenType == SEMICOLON) {
        token = nextToken(); // Consume the ";".
      } else if (CONSTANT_START_SET.contains(tokenType)) {
        errorHandler.flag(token, MISSING_SEMICOLON, this);
      }
    }

    if (token.getType() == END) {
      token = nextToken(); // Consume "END"
    } else {
      errorHandler.flag(token, MISSING_END, this);
    }

    return selectNode;
  }

  /**
   * Parses a "CASE" branch.
   * @param token the current token.
   * @param constantSet the constant set of "CASE" branch constants.
   * @return the root "SELECT_BRANCH" node of the subtree.
   * @throws Exception if an error occurs.*/
  private ICodeNode parseBranch(Token token, HashSet<Object> constantSet) throws Exception {
    // Create a "SELECT_BRANCH" node and a SELECT_CONSTANTS node. The "SELECT_BRANCH" adopts the "SELECT_CONSTANTS"
    // node as its first child.
    ICodeNode branchNode = ICodeFactory.createICodeNode(SELECT_BRANCH);
    ICodeNode constantsNode = ICodeFactory.createICodeNode(SELECT_CONSTANTS);
    branchNode.addChild(constantsNode);

    // Parse the list of "CASE" branch constants.
    // The "SELECT_CONSTANTS" node adopts each constant.
    parseConstantList(token, constantsNode, constantSet);

    // Look for the ":" token.
    token = currentToken();
    if (token.getType() == COLON) {
      token = nextToken(); // Consume ":".
    } else {
      errorHandler.flag(token, MISSING_COLON, this);
    }

    // Parse the "CASE" branch statement. The "SELECT_BRANCH" node adopts the statement subtree as its second child.
    StatementParser statementParser = new StatementParser(this);
    branchNode.addChild(statementParser.parse(token));

    return branchNode;
  }

  /**
   * Parse a list of "CASE" branch constants.
   * @param token the current token.
   * @param constantsNode the parent "SELECT_CONSTANTS" node.
   * @param constantSet the set of CASE branch constants.
   * @throws Exception if an error occurs.
   */
  private void parseConstantList(Token token, ICodeNode constantsNode, HashSet<Object> constantSet) throws Exception {
    // Loop to parse each constant.
    while (CONSTANT_START_SET.contains(token.getType())) {
      // The constant list nde adopts the constant node.
      constantsNode.addChild(parseConstant(token, constantSet));

      // Synchronize at the comma between constants.
      token = synchronize(COMMA_SET);

      if (token.getType() == COMMA) {
        token = nextToken(); // Consume the ","
      } else if (CONSTANT_START_SET.contains(token.getType())) {
        errorHandler.flag(token, MISSING_COMMA, this);
      }
    }
  }

  /**
   * Parses "CASE" branch constant.
   * @param token  the current token.
   * @param constantSet the set of "CASE" branch constants.
   * @return the constant node.
   * @throws Exception if an error occurs.
   */
  private ICodeNode parseConstant(Token token, HashSet<Object> constantSet) throws Exception {
    TokenType sign = null;
    ICodeNode constantNode = null;

    // Synchronize at the start of a constant.
    token = synchronize(CONSTANT_START_SET);
    TokenType tokenType = token.getType();

    if ((tokenType == PLUS) || (tokenType == MINUS)) {
      sign = tokenType;
      token = nextToken(); // Consume the sign
    }
    
    // Parse the constant.
    switch ((PascalTokenType) token.getType()) {
      case IDENTIFIER: {
        constantNode = parseIdentifierConstant(token, sign);
        break;
      }
      case INTEGER: {
        constantNode = parseIntegerConstant(token.getText(), sign);
        break;
      }
      case STRING: {
        constantNode = parseCharacterConstant(token, (String) token.getValue(), sign);
        break;
      }
      default: {
        errorHandler.flag(token, INVALID_CONSTANT, this);
      }
    }

    // Check for reused constants.
    if (constantNode != null) {
      Object value = constantNode.getAttribute(VALUE);
      if (constantSet.contains(value)) {
        errorHandler.flag(token, CASE_CONSTANT_REUSED, this);
      } else {
        constantSet.add(value);
      }
    }

    nextToken(); // Consume the constant.
    return constantNode;
  }

  /**
   * Parses a character "CASE" constant.
   * @param token the current token.
   * @param value the current token value string.
   * @param sign the sign, if any.
   * @return the constant node.
   */
  private ICodeNode parseCharacterConstant(Token token, String value, TokenType sign) {
    ICodeNode constantNode = null;

    if (sign != null) {
      errorHandler.flag(token, INVALID_CONSTANT, this);
    } else {
      if (value.length() == 1) {
        constantNode = ICodeFactory.createICodeNode(STRING_CONSTANT);
        constantNode.setAttribute(VALUE, value);
      } else {
        errorHandler.flag(token, INVALID_CONSTANT, this);
      }
    }

    return constantNode;
  }

  /**
   * Parses an integer "CASE" constant.
   * @param value the current token value string.
   * @param sign the sign, if any.
   * @return the constant node.
   */
  private ICodeNode parseIntegerConstant(String value, TokenType sign) {
    ICodeNode constantNode = ICodeFactory.createICodeNode(INTEGER_CONSTANT);
    int intValue = Integer.parseInt(value);

    if (sign == MINUS) intValue = -intValue;

    constantNode.setAttribute(VALUE, intValue);
    return constantNode;
  }

  /**
   * Parses an identifier "CASE" constant.
   * @param token the current token.
   * @param sign the sign, if any.
   * @return the constant node.
   */
  private ICodeNode parseIdentifierConstant(Token token, TokenType sign) {
    errorHandler.flag(token, INVALID_CONSTANT, this);
    return null;
  }
}
