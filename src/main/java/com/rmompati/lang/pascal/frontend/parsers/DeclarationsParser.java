package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.EnumSet;

import static com.rmompati.lang.pascal.intermediate.symtableimpl.DefinitionImpl.VARIABLE;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;

public class DeclarationsParser extends PascalParserTD {

  static final EnumSet<PascalTokenType> DECLARATIONS_START_SET = EnumSet.of(CONST, TYPE, VAR, PROCEDURE, FUNCTION, BEGIN);
  static final EnumSet<PascalTokenType> TYPE_START_SET = DECLARATIONS_START_SET.clone();
  static {
    TYPE_START_SET.remove(CONST);
  }

  static final EnumSet<PascalTokenType> VAR_START_SET = TYPE_START_SET.clone();
  static {
    VAR_START_SET.remove(TYPE);
  }

  static final EnumSet<PascalTokenType> ROUTINE_START_SET = VAR_START_SET.clone();
  static {
    ROUTINE_START_SET.remove(VAR);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public DeclarationsParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses declarations.
   * To be overridden by the specialized declarations parser subclasses.
   * @param token the initial token.
   * @throws Exception if an error occurs.
   */
  public void parse(Token token) throws Exception {
    token = synchronize(DECLARATIONS_START_SET);

    if (token.getType() == CONST) {
      token = nextToken(); // Consume 'CONST'
      ConstantsDefinitionParser constantsDefinitionParser = new ConstantsDefinitionParser(this);
      constantsDefinitionParser.parse(token);
    }

    token = synchronize(TYPE_START_SET);
    if (token.getType() == TYPE) {
      token = nextToken();  // Consume 'TYPE'
      TypeDefinitionParser typeDefinitionParser = new TypeDefinitionParser(this);
      typeDefinitionParser.parse(token);
    }

    token = synchronize(VAR_START_SET);
    if (token.getType() == VAR) {
      token = nextToken();  // Consume 'VAR'
      VariableDeclarationsParser variableDeclarationsParser = new VariableDeclarationsParser(this);
      variableDeclarationsParser.setDefinition(VARIABLE);
      variableDeclarationsParser.parse(token);
    }

    token = synchronize(ROUTINE_START_SET);
  }
}
