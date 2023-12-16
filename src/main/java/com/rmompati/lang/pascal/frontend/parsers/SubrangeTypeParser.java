package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.frontend.TokenType;
import com.rmompati.lang.pascal.intermediate.TypeFactory;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.intermediate.symtableimpl.Predefined;
import com.rmompati.lang.pascal.frontend.PascalParserTD;

import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeFormImpl.ENUMERATION;
import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeFormImpl.SUBRANGE;
import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeKeyImpl.*;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.DOT_DOT;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.IDENTIFIER;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;

public class SubrangeTypeParser extends TypeSpecificationParser {

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public SubrangeTypeParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses a Pascal subrange type specification.
   *
   * @param token the current token.
   * @return the type specification.
   * @throws Exception if an error occurs.
   */
  @Override
  public TypeSpec parse(Token token) throws Exception {
    TypeSpec subrangeType = TypeFactory.createType(SUBRANGE);
    Object minValue = null;
    Object maxValue = null;

    // Parse the minimum constant.
    Token constantToken = token;
    ConstantsDefinitionParser constantParser = new ConstantsDefinitionParser(this);
    minValue = constantParser.parseConstant(token);

    // Set the minimum constant's type.
    TypeSpec minType = constantToken.getType() == IDENTIFIER
        ? constantParser.getConstantType(constantToken)
        : constantParser.getConstantType(minValue);
    minValue = checkValueType(constantToken, minValue, minType);

    token = currentToken();
    boolean sawDotDot = false;

    // Look for '..' token
    if (token.getType() == DOT_DOT) {
      token = nextToken(); // Consume the .. token
      sawDotDot = true;
    }

    TokenType tokenType = token.getType();

    // At the start of maximum constant
    if (ConstantsDefinitionParser.CONSTANT_START_SET.contains(tokenType)) {
      if (!sawDotDot) {
        errorHandler.flag(token, MISSING_DOT_DOT, this);
      }

      // Parse the maximum constant
      token = synchronize(ConstantsDefinitionParser.CONSTANT_START_SET);
      constantToken = token;
      maxValue = constantParser.parseConstant(token);

      // Set the type of maximum constant's type
      TypeSpec maxType = constantToken.getType() == IDENTIFIER
          ? constantParser.getConstantType(constantToken)
          : constantParser.getConstantType(maxValue);

      maxValue = checkValueType(constantToken, maxValue, maxType);

      // Ensure same types
      if ((minType == null) || (maxType == null)) {
        errorHandler.flag(constantToken, INCOMPATIBLE_TYPES, this);
      } else if (minType != maxType) {
        errorHandler.flag(constantToken, INVALID_SUBRANGE_TYPE, this);
      } else if ((minValue != null) && (maxValue != null) && (((Integer) minValue) > (Integer) maxValue)) {
        errorHandler.flag(constantToken, MIN_GT_MAX, this);
      }
    } else {
      errorHandler.flag(constantToken, INVALID_SUBRANGE_TYPE, this);
    }

    subrangeType.setAttribute(SUBRANGE_BASE_TYPE, minType);
    subrangeType.setAttribute(SUBRANGE_MIN_VALUE, minValue);
    subrangeType.setAttribute(SUBRANGE_MAX_VALUE, maxValue);

    return subrangeType;
  }

  /**
   * Checks a value of a type specification.
   * @param token the current token.
   * @param value the value.
   * @param type the type specification.
   */
  public Object checkValueType(Token token, Object value, TypeSpec type) {
    if (type == null) return value;

    if (type == Predefined.integerType) {
      return value;
    } else if (type == Predefined.charType) {
      char ch = ((String) value).charAt(0);
      return Character.getNumericValue(ch);
    } else if (type.getForm() == ENUMERATION) {
      return value;
    } else {
      errorHandler.flag(token, INVALID_SUBRANGE_TYPE, this);
      return value;
    }
  }
}
