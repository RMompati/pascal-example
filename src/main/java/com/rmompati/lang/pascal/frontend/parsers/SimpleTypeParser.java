package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.intermediate.Definition;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.intermediate.symtableimpl.DefinitionImpl;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.EnumSet;

import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;

public class SimpleTypeParser extends TypeSpecificationParser {

  // Synchronization set for starting a simple type specification
  static final EnumSet<PascalTokenType> SIMPLE_START_SET = ConstantsDefinitionParser.CONSTANT_START_SET.clone();
  static {
    SIMPLE_START_SET.add(LEFT_PAREN);
    SIMPLE_START_SET.add(COMMA);
    SIMPLE_START_SET.add(SEMICOLON);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public SimpleTypeParser(PascalParserTD parent) {
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
    // Synchronize at the start of a simple type specification.
    token = synchronize(SIMPLE_START_SET);

    switch ((PascalTokenType) token.getType()) {
      case IDENTIFIER: {
        String name = token.getText().toLowerCase();
        SymTableEntry id = symTabStack.lookup(name);

        if (id != null) {
          Definition definition = id.getDefinition();
          if (definition == DefinitionImpl.TYPE) {
            id.appendLineNumber(token.getLineNum());
            token = nextToken(); // Consume the identifier.

            return id.getTypeSpec();
          } else if ((definition != DefinitionImpl.CONSTANT) && (definition != DefinitionImpl.ENUMERATION_CONSTANT)) {
            errorHandler.flag(token, NOT_TYPE_IDENTIFIER, this);
            token = nextToken(); // Consume the identifier.
            return null;
          } else {
            SubrangeTypeParser subrangeTypeParser = new SubrangeTypeParser(this);
            return subrangeTypeParser.parse(token);
          }
        } else {
          errorHandler.flag(token, IDENTIFIER_UNDEFINED, this);
          token = nextToken(); // Consume the identifier.
          return null;
        }
      }
      case LEFT_PAREN: {
        EnumerationTypeParser enumerationTypeParser = new EnumerationTypeParser(this);
        return enumerationTypeParser.parse(token);
      }
      case COMMA:
      case SEMICOLON: {
        errorHandler.flag(token, INVALID_TYPE, this);
        return null;
      }
      default: {
        SubrangeTypeParser subrangeTypeParser = new SubrangeTypeParser(this);
        return subrangeTypeParser.parse(token);
      }
    }
  }
}
