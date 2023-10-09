package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.frontend.TokenType;
import com.rmompati.lang.intermediate.Definition;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.ArrayList;
import java.util.EnumSet;

import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;

public class VariableDeclarationsParser extends DeclarationsParser {

  /** Definition for the identifier. */
  private Definition definition;

  /** Synchronization set for a variable identifier. */
  private static final EnumSet<PascalTokenType> IDENTIFIER_SET =
      DeclarationsParser.VAR_START_SET.clone();
  static {
    IDENTIFIER_SET.add(IDENTIFIER);
    IDENTIFIER_SET.add(END);
    IDENTIFIER_SET.add(SEMICOLON);
  }

  /** Synchronization set for the start of the next definition or declaration. */
  private static final EnumSet<PascalTokenType> NEXT_START_SET =
      DeclarationsParser.ROUTINE_START_SET.clone();
  static {
    NEXT_START_SET.add(IDENTIFIER);
    NEXT_START_SET.add(SEMICOLON);
  }

  /** Synchronization set to start a sublist identifier. */
  private static final EnumSet<PascalTokenType> IDENTIFIER_START_SET =
      EnumSet.of(IDENTIFIER, COMMA);

  /** Synchronization set to follow a sublist identifier. */
  private static final EnumSet<PascalTokenType> IDENTIFIER_FOLLOW_SET =
      EnumSet.of(COLON, SEMICOLON);
  static {
    IDENTIFIER_FOLLOW_SET.addAll(DeclarationsParser.VAR_START_SET);
  }

  /** Synchronization set for the "," token. */
  private static final EnumSet<PascalTokenType> COMMA_SET =
      EnumSet.of(COMMA, COLON, IDENTIFIER, SEMICOLON);

  /** Synchronization set for the ":" token. */
  private static final  EnumSet<PascalTokenType> COLON_SET =
      EnumSet.of(COLON, SEMICOLON);

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public VariableDeclarationsParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses declarations.
   * To be overridden by the specialized declarations parser subclasses.
   *
   * @param token the initial token.
   * @throws Exception if an error occurs.
   */
  @Override
  public void parse(Token token) throws Exception {
    token = synchronize(IDENTIFIER_SET);

    // Loop to parse a sequence of variable declarations seperated by semicolons.
    while (token.getType() == IDENTIFIER) {
      // Parse the identifier sublist and its type specification.
      parseIdentifierSublist(token);

      token = currentToken();
      TokenType tokenType = token.getType();

      // Look for one or more semicolons after a definition.
      if (tokenType == SEMICOLON) {
        while (token.getType() == SEMICOLON) {
          token = nextToken(); // Consume the ";".
        }
      } else if (NEXT_START_SET.contains(tokenType)) {
        errorHandler.flag(token, MISSING_SEMICOLON, this);
      }

      token = synchronize(IDENTIFIER_SET);
    }
  }

  /**
   * Parses a sublist of identifiers and their type specification.
   * @param token the current token.
   * @return the sublist of identifiers in a declaration.
   * @throws Exception if an error occurs.
   */
  protected ArrayList<SymTableEntry> parseIdentifierSublist(Token token) throws Exception {
    ArrayList<SymTableEntry> sublist = new ArrayList<>();

    do {
      token = synchronize(IDENTIFIER_START_SET);
      SymTableEntry id = parseIdentifier(token);

      if (id != null) sublist.add(id);

      token = synchronize(COMMA_SET);
      TokenType tokenType = token.getType();

      // Look for the comma.
      if (tokenType == COMMA) {
        token = nextToken();

        if (IDENTIFIER_FOLLOW_SET.contains(token.getType())) {
          errorHandler.flag(token, MISSING_IDENTIFIER, this);
        }
      } else if (IDENTIFIER_START_SET.contains(tokenType)) {
        errorHandler.flag(token, MISSING_COMMA, this);
      }
    } while (!IDENTIFIER_FOLLOW_SET.contains(token.getType()));

    // Parse the type specification
    TypeSpec type = parseTypeSpec(token);

    for (SymTableEntry variableId : sublist) {
      variableId.setTypeSpec(type);
    }

    return sublist;
  }

  /**
   * Parses an identifier.
   * @param token the current token.
   * @return the symbol table entry of the identifier.
   * @throws Exception if an error occurs.
   */
  private SymTableEntry parseIdentifier(Token token)  throws Exception {
    SymTableEntry id = null;

    if (token.getType() == IDENTIFIER) {
      String name = token.getText().toLowerCase();
      id = symTabStack.lookupLocal(name);

      if (id == null) {
        id = symTabStack.enterLocal(name);
        id.setDefinition(definition);
        id.appendLineNumber(token.getLineNum());
      } else {
        errorHandler.flag(token, IDENTIFIER_REDEFINED, this);
      }

      token =  nextToken();
    } else {
      errorHandler.flag(token, MISSING_IDENTIFIER, this);
    }

    return id;
  }

  /**
   * Parses the type specification.
   * @param token the current token.
   * @return the type specification.
   * @throws Exception if an error occurs.
   */
  protected TypeSpec parseTypeSpec(Token token) throws Exception {
    token = synchronize(COLON_SET);

    if (token.getType() == COLON) {
      token = nextToken(); // Consume the ":".
    } else {
      errorHandler.flag(token, MISSING_COLON, this);
    }

    // Parse the type specification.
    TypeSpecificationParser typeSpecificationParser = new TypeSpecificationParser(this);

    return typeSpecificationParser.parse(token);
  }

  public Definition getDefinition() {
    return definition;
  }

  public void setDefinition(Definition definition) {
    this.definition = definition;
  }
}
