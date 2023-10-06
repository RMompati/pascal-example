package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.frontend.TokenType;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.intermediate.TypeFactory;
import com.rmompati.lang.intermediate.TypeForm;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.ArrayList;
import java.util.EnumSet;

import static com.rmompati.lang.intermediate.typeimpl.TypeFormImpl.ARRAY;
import static com.rmompati.lang.intermediate.typeimpl.TypeFormImpl.*;
import static com.rmompati.lang.intermediate.typeimpl.TypeKeyImpl.*;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;

public class ArrayTypeParser extends TypeSpecificationParser {

  /** Synchronization set for the [ token. */
  private static final EnumSet<PascalTokenType> LEFT_BRACKET_SET =
      SimpleTypeParser.SIMPLE_START_SET.clone();
  static {
    LEFT_BRACKET_SET.add(LEFT_BRACKET);
    LEFT_BRACKET_SET.add(RIGHT_BRACKET);
  }

  /** Synchronization set for the ] token. */
  private static final EnumSet<PascalTokenType> RIGHT_BRACKET_SET =
      EnumSet.of(RIGHT_BRACKET, OF, SEMICOLON);


  /** Synchronization set for OF. */
  private static final EnumSet<PascalTokenType> OF_SET =
      TypeSpecificationParser.TYPE_START_SET.clone();
  static {
    OF_SET.add(OF);
    OF_SET.add(SEMICOLON);
  }

  /** Synchronization set to start of an index type. */
  private static final EnumSet<PascalTokenType> INDEX_START_SET =
      SimpleTypeParser.SIMPLE_START_SET.clone();
  static {
    INDEX_START_SET.add(COMMA);
  }

  /** Synchronization sey to end an index type. */
  private static final EnumSet<PascalTokenType> INDEX_END_SET =
      EnumSet.of(RIGHT_BRACKET, OF, SEMICOLON);

  /** Synchronization set to follow an index type. */
  private static final EnumSet<PascalTokenType> INDEX_FOLLOW_SET =
      INDEX_START_SET.clone();
  static {
    INDEX_FOLLOW_SET.addAll(INDEX_END_SET);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public ArrayTypeParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses a Pascal type specification.
   *
   * @param token the current token.
   * @return the type specification.
   * @throws Exception if an error occurs.
   */
  @Override
  public TypeSpec parse(Token token) throws Exception {
    TypeSpec arrayType = TypeFactory.createType(ARRAY);
    token = nextToken(); // Consume the ARRAY.

    // Synchronize the '[' token.
    token = synchronize(LEFT_BRACKET_SET);
    if (token.getType() != LEFT_BRACKET) errorHandler.flag(token, MISSING_LEFT_BRACKET, this);

    // Parse the list index.
    TypeSpec elementType = parseIndexTypeList(token, arrayType);

    // Synchronize the ']' token.
    token = synchronize(RIGHT_BRACKET_SET);
    if (token.getType() == RIGHT_BRACKET) {
      token = nextToken(); // Consume the ']'.
    } else {
      errorHandler.flag(token, MISSING_RIGHT_BRACKET, this);
    }

    // Synchronize at OF.
    token = synchronize(OF_SET);
    if (token.getType() == OF) {
      token = nextToken(); // Consume the ']'.
    } else {
      errorHandler.flag(token, MISSING_OF, this);
    }

    // Parse the element type.
    elementType.setAttribute(ARRAY_ELEMENT_TYPE, parseElementType(token));

    return arrayType;
  }

  /**
   * Parses the list of index type specification.
   * @param token the current token.
   * @param arrayType the current array type specification.
   * @throws Exception if an error occurs.
   */
  private TypeSpec parseIndexTypeList(Token token, TypeSpec arrayType) throws Exception {
    TypeSpec elementType = arrayType;
    boolean anotherIndex = false;

    token = nextToken(); // Consume the "[" token.

    // Parse the list of index type specification.
    do {
      anotherIndex = false;

      // Parse the index type.
      token = synchronize(INDEX_START_SET);
      parseIndexType(token, elementType);

      // Synchronize at the "," token.
      token = synchronize(INDEX_FOLLOW_SET);
      TokenType tokenType = token.getType();
      if ((tokenType != COMMA) && (tokenType != RIGHT_BRACKET)) {
        if (INDEX_START_SET.contains(tokenType)) {
          errorHandler.flag(token, MISSING_COMMA, this);
          anotherIndex = true;
        }
      } else if (tokenType == COMMA) {
        TypeSpec newElementType = TypeFactory.createType(ARRAY);
        elementType.setAttribute(ARRAY_ELEMENT_TYPE, newElementType);
        elementType = newElementType;

        token = nextToken(); // Consume the "," token.
        anotherIndex = true;
      }
    } while (anotherIndex);

    return elementType;
  }

  /**
   * Parse an index type specification.
   * @param token the current token.
   * @param arrayType the current array type specification.
   * @throws Exception if an error occurs.
   */
  private void parseIndexType(Token token, TypeSpec arrayType) throws Exception {
    SimpleTypeParser simpleTypeParser = new SimpleTypeParser(this);
    TypeSpec indexType = simpleTypeParser.parse(token);
    arrayType.setAttribute(ARRAY_INDEX_TYPE, indexType);

    if (indexType == null) return;

    TypeForm form = indexType.getForm();
    int count = 0;

    // Check the index type and set the element count.
    if (form == SUBRANGE) {
      Integer minValue = (Integer) indexType.getAttribute(SUBRANGE_MIN_VALUE);
      Integer maxValue = (Integer) indexType.getAttribute(SUBRANGE_MAX_VALUE);
      if ((minValue != null) && (maxValue != null)) {
        count = maxValue - minValue + 1;
      }
    } else if (form == ENUMERATION) {
      count = ((ArrayList<SymTableEntry>) indexType.getAttribute(ENUMERATION_CONSTANTS)).size();
    } else {
      errorHandler.flag(token, INVALID_INDEX_TYPE, this);
    }

    arrayType.setAttribute(ARRAY_ELEMENT_COUNT, count);
  }

  /**
   * Parses the element type specification.
   * @param token the current token.
   * @return the element type specification.
   * @throws Exception if an error occurs.
   */
  private TypeSpec parseElementType(Token token)  throws Exception {
    TypeSpecificationParser typeSpecificationParser = new TypeSpecificationParser(this);
    return typeSpecificationParser.parse(token);
  }
}
