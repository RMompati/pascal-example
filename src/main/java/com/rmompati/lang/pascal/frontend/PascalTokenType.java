package com.rmompati.lang.pascal.frontend;

import com.rmompati.lang.frontend.TokenType;

public enum PascalTokenType implements TokenType {

  // Reserved Words
  AND, ARRAY, BEGIN, CASE, CONST,DIV, DO, DOWNTO, ELSE, END,
  FILE, FOR, FUNCTION, GOTO, IF, IN, LABEL, MOD, NIL, NOT,
  OF, OR, PACKED, PROCEDURE, PROGRAM, RECORD, REPEAT, SET,
  THEN, TO, UNTIL, VAR, WHILE, WITH,
  // Special symbols
  PLUS("+"), MINUS("-"), STAR("*"), SLASH("/"), COLON_EQUALS(":="),
  DOT("."), COMMA(","), SEMICOLON(";"), COLON(":"), QUOTE("\""),
  EQUALS("="), NOT_EQUALS("<>"), LESS_THAN("<"), LESS_EQUALS("<="),
  GREATER_EQUALS(">="), GREATER_THAN(">"), LEFT_PAREN("("), RIGHT_PAREN(")"),
  LEFT_BRACKET("["), RIGHT_BRACKET("]"), LEFT_BRACE("{"), RIGHT_BRACE("}"),
  UP_ARROW("^"), DOT_DOT(".."),

  IDENTIFIER, INTEGER, REAL, STRING,
  ERROR, END_OF_FILE;

  private String text;

  /**
   * Constructor
   */
  PascalTokenType() {
    this.text = this.toString();
  }

  PascalTokenType(String text) {
    this.text = text;
  }

  /**
   * Gets the token type text.
   * @return the token text.
   */
  public String getText() {
    return text;
  }
}
