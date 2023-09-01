package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.EnumSet;

import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;

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
   */
  public void parse(Token token) {}
}
