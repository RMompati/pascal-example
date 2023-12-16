package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.frontend.TokenType;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.intermediate.symtableimpl.DefinitionImpl;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.EnumSet;

import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;

public class TypeDefinitionParser extends DeclarationsParser {

  /** Synchronization set for a type identifier. */
  private static final EnumSet<PascalTokenType> IDENTIFIER_SET = DeclarationsParser.VAR_START_SET.clone();
  static {
    IDENTIFIER_SET.add(IDENTIFIER);
  }

  /* Synchronization set for the = token. */
  private static final EnumSet<PascalTokenType> EQUALS_SET = ConstantsDefinitionParser.CONSTANT_START_SET.clone();
  static {
    EQUALS_SET.add(EQUALS);
    EQUALS_SET.add(SEMICOLON);
  }

  /** Synchronization set for what follows a definition or declaration. */
  private static final EnumSet<PascalTokenType> FOLLOW_SET = EnumSet.of(SEMICOLON);

  /** Synchronization set for the start of the next definition or declaration. */
  private static final EnumSet<PascalTokenType> NEXT_START_SET = DeclarationsParser.VAR_START_SET.clone();
  static {
    NEXT_START_SET.add(SEMICOLON);
    NEXT_START_SET.add(IDENTIFIER);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public TypeDefinitionParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses type definitions.
   * @param token the initial token.
   * @throws Exception if an error occurs.
   */
  public void parse(Token token) throws Exception {
    token = synchronize(IDENTIFIER_SET);

    // Loop to parse a sequence of type definitions separated by semicolons.
    while (token.getType() == IDENTIFIER) {
      String name = token.getText().toLowerCase();
      SymTableEntry typeId = symTabStack.lookupLocal(name);

      // Enter the new identifier into the symbol table but don't set how it was defined yet.
      if (typeId == null) {
        typeId = symTabStack.enterLocal(name);
        typeId.appendLineNumber(token.getLineNum());
      } else {
        errorHandler.flag(token, IDENTIFIER_REDEFINED, this);
        typeId = null;
      }

      token = nextToken(); // Consume the identifier token.

      // Synchronize on the = token
      token = synchronize(EQUALS_SET);
      if (token.getType() == EQUALS) {
        token = nextToken(); // Consume the =
      } else {
        errorHandler.flag(token, MISSING_EQUALS, this);
      }

      // Parse the type specification
      TypeSpecificationParser typeSpecificationParser = new TypeSpecificationParser(this);
      TypeSpec type = typeSpecificationParser.parse(token);

      // Set identifier to be a type and set its type spec.
      if (typeId != null) {
        typeId.setDefinition(DefinitionImpl.TYPE);
      }

      // Cross-link the type identifier and type specification
      if ((type != null) && (typeId != null)) {
        if (type.getIdentifier() == null) {
          type.setIdentifier(typeId);
        }
        typeId.setTypeSpec(type);
      } else {
        token = synchronize(FOLLOW_SET);
      }

      token = currentToken();
      TokenType tokenType = token.getType();

      // Look for one or more semicolons after a definition.
      if (tokenType == SEMICOLON) {
        while (token.getType() == SEMICOLON) {
          token = nextToken(); // Consume a ;
        }
      } else if (NEXT_START_SET.contains(tokenType)) {
        // If at the start of the next definition or declaration, then a semicolon is missing.
        errorHandler.flag(token, MISSING_SEMICOLON, this);
      }

      token = synchronize(IDENTIFIER_SET);
    }
  }
}
