package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.frontend.TokenType;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.pascal.intermediate.TypeFactory;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.ArrayList;
import java.util.EnumSet;

import static com.rmompati.lang.pascal.intermediate.symtableimpl.DefinitionImpl.ENUMERATION_CONSTANT;
import static com.rmompati.lang.pascal.intermediate.symtableimpl.SymTableKeyImpl.CONSTANT_VALUE;
import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeFormImpl.ENUMERATION;
import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeKeyImpl.ENUMERATION_CONSTANTS;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;

public class EnumerationTypeParser extends TypeSpecificationParser {

  /** Synchronization set to start an enumeration constant. */
  private static final EnumSet<PascalTokenType> ENUM_CONSTANT_START_SET =
      EnumSet.of(IDENTIFIER, SEMICOLON);

  /** Synchronization set to follow an enumeration definition. */
  private static final EnumSet<PascalTokenType> ENUM_DEFINITION_FOLLOW_SET =
      EnumSet.of(RIGHT_PAREN, SEMICOLON);
  static {
    ENUM_DEFINITION_FOLLOW_SET.addAll(DeclarationsParser.VAR_START_SET);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public EnumerationTypeParser(PascalParserTD parent) {
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
    TypeSpec enumerationType = TypeFactory.createType(ENUMERATION);
    int value = -1;
    ArrayList<SymTableEntry> constants = new ArrayList<>();
    token = nextToken();

    do {
      token = synchronize(ENUM_CONSTANT_START_SET);
      parseEnumerationIdentifier(token, ++value, enumerationType, constants);

      token = currentToken();
      TokenType tokenType = token.getType();

      if (tokenType == COMMA) {
        token = nextToken(); // Consume the comma.

        if (ENUM_DEFINITION_FOLLOW_SET.contains(token.getType())) {
          errorHandler.flag(token, MISSING_IDENTIFIER, this);
        }
      } else if (ENUM_CONSTANT_START_SET.contains(tokenType)) {
        errorHandler.flag(token, MISSING_COMMA, this);
      }
    } while (!ENUM_DEFINITION_FOLLOW_SET.contains(token.getType()));

    // Look for the closing ")".
    if (token.getType() == RIGHT_PAREN) {
      token = nextToken(); // Consume the ")";
    } else {
      errorHandler.flag(token, MISSING_RIGHT_PAREN, this);
    }

    enumerationType.setAttribute(ENUMERATION_CONSTANTS, constants);

    return enumerationType;
  }

  /**
   * Parses an enumeration identifier.
   * @param token the current token.
   * @param value the identifier's integer value (sequence number)
   * @param enumerationType the enumeration type specification.
   * @param constants the array of table entries for the enumeration constants.
   * @throws Exception if an error occurs.
   */
  private void parseEnumerationIdentifier(Token token, int value, TypeSpec enumerationType,
                                          ArrayList<SymTableEntry> constants) throws Exception {
    TokenType tokenType = token.getType();

    if (tokenType == IDENTIFIER) {
      String name = token.getText().toLowerCase();
      SymTableEntry constantId = symTabStack.lookupLocal(name);

      if (constantId != null) {
        errorHandler.flag(token, IDENTIFIER_REDEFINED, this);
      } else {
        constantId = symTabStack.enterLocal(token.getText());
        constantId.setDefinition(ENUMERATION_CONSTANT);
        constantId.setTypeSpec(enumerationType);
        constantId.setAttribute(CONSTANT_VALUE, value);
        constantId.appendLineNumber(token.getLineNum());
        constants.add(constantId);
      }

      token = nextToken(); // Consume the identifier.
    } else {
      errorHandler.flag(token, MISSING_IDENTIFIER, this);
    }
  }
}
