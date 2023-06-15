package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.frontend.TokenType;
import com.rmompati.lang.intermediate.ICodeFactory;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.intermediate.ICodeNodeType;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.intermediate.icodeimpl.ICodeNodeTypeImpl;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.EnumSet;
import java.util.HashMap;

import static com.rmompati.lang.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static com.rmompati.lang.intermediate.icodeimpl.ICodeKeyImpl.VALUE;
import static com.rmompati.lang.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;

/**
 * <h1>ExpressionParser</h1>
 *
 * <p>Parses expressions.</p>
 */
public class ExpressionParser extends StatementParser {

  // Set of relational operators.
  private static final EnumSet<PascalTokenType> REL_OPS =
      EnumSet.of(EQUALS, NOT_EQUALS, LESS_THAN, LESS_EQUALS, GREATER_THAN, GREATER_EQUALS);

  // Map relational operator tokens to node types.
  private static final HashMap<PascalTokenType, ICodeNodeType> REL_OPS_MAP = new HashMap<>();
  static {
    REL_OPS_MAP.put(EQUALS, EQ);
    REL_OPS_MAP.put(NOT_EQUALS, NE);
    REL_OPS_MAP.put(LESS_THAN, LT);
    REL_OPS_MAP.put(LESS_EQUALS, LE);
    REL_OPS_MAP.put(GREATER_THAN, GT);
    REL_OPS_MAP.put(GREATER_EQUALS, GE);
  }

  // Set of additive operators.
  private static  final EnumSet<PascalTokenType> ADD_OPS =
      EnumSet.of(PLUS, MINUS, PascalTokenType.OR);

  // Map additive operator tokens to node types.
  private static final HashMap<PascalTokenType, ICodeNodeTypeImpl> ADD_OPS_MAP = new HashMap<>();
  static {
    ADD_OPS_MAP.put(PLUS, ADD);
    ADD_OPS_MAP.put(MINUS, SUBTRACT);
    ADD_OPS_MAP.put(PascalTokenType.OR, ICodeNodeTypeImpl.OR);
  }

  // Set of multiplicative operators.
  private static  final EnumSet<PascalTokenType> MUL_OPS =
      EnumSet.of(STAR, SLASH, DIV, PascalTokenType.MOD, PascalTokenType.AND);

  // Map multiplicative operator tokens to node types.
  private static final HashMap<PascalTokenType, ICodeNodeType> MUL_OPS_MAP = new HashMap<>();
  static {
    MUL_OPS_MAP.put(STAR, MULTIPLY);
    MUL_OPS_MAP.put(SLASH, FLOAT_DIVIDE);
    MUL_OPS_MAP.put(DIV, INTEGER_DIVIDE);
    MUL_OPS_MAP.put(PascalTokenType.MOD, ICodeNodeTypeImpl.MOD);
    MUL_OPS_MAP.put(PascalTokenType.AND, ICodeNodeTypeImpl.AND);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public ExpressionParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parse a statement.
   *
   * @param token the initial token.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurs.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    return parseExpression(token);
  }

  /**
   * Parse an expression.
   * @param token  the initial token.
   * @return the root node of the generated parse subtree.
   * @throws Exception if an error occurs.
   */
  private ICodeNode parseExpression(Token token) throws Exception {
    // Parse simple expression and make the root of its tree the root node.
    ICodeNode rootNode = parseSimpleExpression(token);

    token = currentToken();
    TokenType tokenType = token.getType();

    // Look for a relational
    if (REL_OPS.contains(tokenType)) {
      // Create a new operator node and adopt the current tree as its first child.
      ICodeNodeType nodeType = REL_OPS_MAP.get(tokenType);
      ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
      opNode.addChild(rootNode);

      token = nextToken(); // Consume the operator.

      // Parse the second simple expression. The operator adopts the simple expression as its second child.
      opNode.addChild(parseSimpleExpression(token));

      // The operator node becomes the new root node.
      rootNode = opNode;
    }

    return rootNode;
  }

  /**
   * Parse a simple expression.
   * @param token the initial token.
   * @return the root of the generated subtree.
   * @throws Exception if an error occurs.
   */
  private ICodeNode parseSimpleExpression(Token token) throws Exception {
    TokenType signType = null; // Type of leading sign, if any.

    // Look for a leading + or -.
    TokenType tokenType = token.getType();
    if (tokenType == PLUS || tokenType == MINUS) {
      signType = tokenType;
      token = nextToken(); // Consume the + or -.
    }
    
    // Parse a term and make the root of its tree the root node.
    ICodeNode rootNode = parseTerm(token);

    // Was there a leading sign?
    if (signType == MINUS) {
      // Create a NEGATE node and adopt the current tree as its child. The NEGATE node becomes the new root node.
      ICodeNode negateNode = ICodeFactory.createICodeNode(NEGATE);
      negateNode.addChild(rootNode);
      rootNode = negateNode;
    }

    token = currentToken();
    tokenType = token.getType();

    // Loop over additive operators.

    while (ADD_OPS.contains(tokenType)) {
      // Create a new operator node and adopt the current tree as its first child.
      ICodeNodeType nodeType = ADD_OPS_MAP.get(tokenType);
      ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
      opNode.addChild(rootNode);

      token = nextToken(); // Consume the operator.

      // Parse another term. The operator node adopts the term's tree as its second child.
      opNode.addChild(parseTerm(token));

      // The operator node becomes the new root node.
      rootNode = opNode;

      token = currentToken();
      tokenType = token.getType();
    }

    return rootNode;
  }

  /**
   * Parse term.
   * @param token the initial token.
   * @return the root of the generated parse subtree.
   * @throws Exception if an error occurs.
   */
  private ICodeNode parseTerm(Token token) throws Exception {

    // Parse a factor and make its root node the root subtree.
    ICodeNode rootNode = parseFactor(token);

    token = currentToken();
    TokenType tokenType = token.getType();

    // Loop over multiplicative operators.

    while (MUL_OPS.contains(tokenType)) {
      // Create a new operator and adopt the current tree as its first child.
      ICodeNodeType nodeType = MUL_OPS_MAP.get(tokenType);
      ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
      opNode.addChild(rootNode);

      token = nextToken(); // Consume the operator.

      // Parse another factor. The operator node adopts the term's tree as its second child.
      opNode.addChild(parseFactor(token));

      // The operator becomes the new root mode.
      rootNode = opNode;

      token = currentToken();
      tokenType = token.getType();
    }

    return  rootNode;
  }

  /**
   * Parse a factor.
   * @param token the initial token.
   * @return the root of the generated parse subtree.
   * @throws Exception if an error occurs.
   */
  private ICodeNode parseFactor(Token token) throws Exception {
    TokenType tokenType = token.getType();
    ICodeNode rootNode = null;

    switch ((PascalTokenType) tokenType) {
      case IDENTIFIER: {
        // Look up the identifier in the symbol table stack.
        // Flag the identifier as undefined if it's not found.
        String name = token.getText().toLowerCase();
        SymTableEntry id = symTabStack.lookup(name);
        if (id == null) {
          errorHandler.flag(token, IDENTIFIER_UNDEFINED, this);
          id = symTabStack.enterLocal(name);
        }

        rootNode = ICodeFactory.createICodeNode(VARIABLE);
        rootNode.setAttribute(ID, id);
        id.appendLineNumber(token.getLineNum());

        token = nextToken(); // consume the identifier.
        break;
      }
      case INTEGER: {
        // Create an INTEGER_CONSTANT node as the root node.
        rootNode = ICodeFactory.createICodeNode(INTEGER_CONSTANT);
        rootNode.setAttribute(VALUE, token.getValue());

        token = nextToken(); // Consume the number.
        break;
      }
      case REAL: {
        // Create an REAL_CONSTANT node as the root node.
        rootNode = ICodeFactory.createICodeNode(REAL_CONSTANT);
        rootNode.setAttribute(VALUE, token.getValue());

        token = nextToken(); // Consume the number.
        break;
      }
      case STRING: {
        String value = (String) token.getValue();

        // Create a STRING_CONSTANT node as the root node.
        rootNode = ICodeFactory.createICodeNode(STRING_CONSTANT);
        rootNode.setAttribute(VALUE, value);

        token = nextToken(); // Consume the string.
        break;
      }
      case NOT: {
        token = nextToken(); // Consume the NOT

        // Create a NOT node as the root node.
        rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.NOT);

        // Parse the factor. The NOT node adopts the factor node as its child.
        rootNode.addChild(parseFactor(token));
        break;
      }
      case LEFT_PAREN: {
        token = nextToken(); // Consume the "("

        // Parse an expression and make its node the root node.
        rootNode = parseExpression(token);

        // Look for the matching ")" token
        token = currentToken();
        if (token.getType() == RIGHT_PAREN) {
          token = nextToken(); // Consume the ")".
        } else {
          errorHandler.flag(token, MISSING_RIGHT_PAREN, this);
        }
        break;
      }
      default: {
        errorHandler.flag(token, UNEXPECTED_TOKEN, this);
        break;
      }
    }

    return rootNode;
  }
}
