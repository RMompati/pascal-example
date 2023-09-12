package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.frontend.TokenType;
import com.rmompati.lang.intermediate.Definition;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.intermediate.TypeFactory;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.intermediate.symtableimpl.Predefined;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.EnumSet;

import static com.rmompati.lang.intermediate.symtableimpl.DefinitionImpl.CONSTANT;
import static com.rmompati.lang.intermediate.symtableimpl.DefinitionImpl.ENUMERATION_CONSTANT;
import static com.rmompati.lang.intermediate.symtableimpl.SymTableKeyImpl.CONSTANT_VALUE;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;

public class ConstantsDefinitionParser extends DeclarationsParser {

  /** Synchronization set for a constant identifier. */
  private static final EnumSet<PascalTokenType> IDENTIFIER_SET = DeclarationsParser.TYPE_START_SET.clone();
  static {
    IDENTIFIER_SET.add(IDENTIFIER);
  }

  /** Synchronization set for the starting a constant. */
  static final EnumSet<PascalTokenType> CONSTANT_START_SET =
      EnumSet.of(IDENTIFIER, INTEGER, REAL, PLUS, MINUS, STRING, SEMICOLON);

  /** Synchronization set for the = token. */
  private static final EnumSet<PascalTokenType> EQUALS_SET = CONSTANT_START_SET.clone();
  static {
    EQUALS_SET.add(EQUALS);
    EQUALS_SET.add(SEMICOLON);
  }
  /** Synchronization set for the start of the next definition or declaration. */
  private static final EnumSet<PascalTokenType> NEXT_START_SET = DeclarationsParser.TYPE_START_SET.clone();
  static {
    NEXT_START_SET.add(SEMICOLON);
    NEXT_START_SET.add(IDENTIFIER);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public ConstantsDefinitionParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses declarations.
   *
   * @param token the initial token.
   * @throws Exception if an error occurs.
   */
  @Override
  public void parse(Token token) throws Exception {
    token = synchronize(IDENTIFIER_SET);

    // Loop to parse a sequence of constant definitions separated by semicolons.
    while (token.getType() == IDENTIFIER) {
      String name = token.getText();
      SymTableEntry constantId = symTabStack.lookupLocal(name);

      // Enter the new identifier into the symbol table, without setting how it was defined.
      if (constantId == null) {
        constantId = symTabStack.enterLocal(name);
        constantId.appendLineNumber(token.getLineNum());
      } else {
        errorHandler.flag(token, IDENTIFIER_REDEFINED, this);
        constantId = null;
      }

      token = nextToken();
      token = synchronize(EQUALS_SET);
      if (token.getType() == EQUALS) {
        token = nextToken(); // Consume the =
      } else {
        errorHandler.flag(token, MISSING_EQUALS, this);
      }

      // Parse the constant value.
      Token constantToken = token;
      Object value = parseConstant(token);

      // Set identifier to be a constant and set its value.
      if (constantId != null) {
        constantId.setDefinition(CONSTANT);
        constantId.setAttribute(CONSTANT_VALUE, value);

        // Set the constant's type
        TypeSpec constantType = constantToken.getType() == IDENTIFIER
            ? getConstantType(constantToken)
            : getConstantType(value);
        constantId.setTypeSpec(constantType);
      }

      token = currentToken();
      TokenType tokenType = token.getType();

      // Look for one or more semicolons after a definition.
      if (tokenType == SEMICOLON) {
        while (token.getType() == SEMICOLON) token = nextToken(); // Consume the ';'
      } else if (NEXT_START_SET.contains(tokenType)) {
        errorHandler.flag(token, MISSING_SEMICOLON, this);
      }
      token = synchronize(IDENTIFIER_SET);
    }
  }

  /**
   * Return the type of the constant given its identifier.
   * @param identifier the constant's identifier.
   * @return the type specification.
   */
  public TypeSpec getConstantType(Token identifier) {
    SymTableEntry id = symTabStack.lookup(identifier.getText());

    if (id == null) return null;

    Definition definition = id.getDefinition();
    if ((definition == CONSTANT) || (definition == ENUMERATION_CONSTANT)) {
      return id.getTypeSpec();
    }

    return null;
  }

  /**
   * Return the type of the constant given its value.
   * @param value the constant value.
   * @return the type specification.
   */
  public TypeSpec getConstantType(Object value) {

    if (value instanceof Integer) {

      return Predefined.integerType;
    } else if (value instanceof Float) {

      return Predefined.realType;
    } else if (value instanceof String) {
      String stringValue = (String) value;

      if (stringValue.length() == 1) {

        return Predefined.charType;
      } else {

        return TypeFactory.createStringType(stringValue);
      }
    }

    return null;
  }

  /**
   * Parses a constant value.
   * @param token the current token.
   * @return the constant value
   * @throws Exception if an error occurs.
   */
  protected Object parseConstant(Token token) throws Exception {
    TokenType sign = null;

    token = synchronize(CONSTANT_START_SET);
    TokenType tokenType = token.getType();

    // Plus or minus sign
    if ((tokenType == PLUS) || (tokenType == MINUS)) {
      sign = tokenType;
      token = nextToken(); // Consume the sign.
    }

    // Parse the constant
    switch ((PascalTokenType)token.getType()) {
      case IDENTIFIER: {
        return parseIdentifierConstant(token, sign);
      }
      case INTEGER: {
        Integer value = (Integer) token.getValue();
        nextToken(); // consume the number.
        return sign == MINUS ? -value : value;
      }
      case REAL: {
        Float value = (Float) token.getValue();
        nextToken(); // consume the number.
        return sign == MINUS ? -value : value;
      }
      case STRING: {
        if (sign != null) errorHandler.flag(token, INVALID_CONSTANT, this);

        nextToken(); // Consume the string.
        return String.valueOf(token.getValue());
      }
      default: {
        errorHandler.flag(token, INVALID_CONSTANT, this);
        return null;
      }
    }
  }

  /**
   * Parses an identifier constant
   * @param token the current token.
   * @param sign the sign, if any.
   * @return Object the constant value.
   * @throws Exception if an error occurs.
   */
  private Object parseIdentifierConstant(Token token, TokenType sign) throws Exception {
    String name = token.getText();
    SymTableEntry id = symTabStack.lookup(name);

    nextToken(); // Consume the identifier.

    if (id == null) {
      errorHandler.flag(token, IDENTIFIER_REDEFINED, this);
      return null;
    }

    Definition definition = id.getDefinition();
    if (definition == CONSTANT) {
      Object value = id.getAttribute(CONSTANT_VALUE);
      id.appendLineNumber(token.getLineNum());

      if (value instanceof Integer) return sign == MINUS ? -((Integer) value) : value;
      else if (value instanceof Float) return sign == MINUS ? -((Float) value) : value;
      else if (value instanceof String) {
        if (sign != null) errorHandler.flag(token, INVALID_CONSTANT, this);

        return value;
      }
    } else if (definition == ENUMERATION_CONSTANT) {
      Object value = id.getAttribute(CONSTANT_VALUE);
      id.appendLineNumber(token.getLineNum());

      if (sign != null) errorHandler.flag(token, INVALID_CONSTANT, this);

      return value;
    } else if (definition == null) {
      errorHandler.flag(token, NOT_CONSTANT_IDENTIFIER, this);
      return null;
    }

    errorHandler.flag(token, INVALID_CONSTANT, this);
    return null;
  }
}
