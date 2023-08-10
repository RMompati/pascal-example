package com.rmompati.lang.pascal.frontend;

import com.rmompati.lang.frontend.TokenType;

import java.util.HashSet;
import java.util.Hashtable;

public enum PascalTokenType implements TokenType {

  // Reserved Words
  AND, ARRAY, BEGIN, CASE, CONST,DIV, DO, DOWNTO, ELSE, END,
  FILE, FOR, FUNCTION, GOTO, IF, IN, LABEL, MOD, NIL, NOT,
  OF, OR, PACKED, PROCEDURE, PROGRAM, RECORD, REPEAT, SET,
  THEN, TO, TYPE, UNTIL, VAR, WHILE, WITH,
  // Special symbols
  PLUS("+"), MINUS("-"), STAR("*"), SLASH("/"), COLON_EQUALS(":="),
  DOT("."), COMMA(","), SEMICOLON(";"), COLON(":"), QUOTE("\""),
  EQUALS("="), NOT_EQUALS("<>"), LESS_THAN("<"), LESS_EQUALS("<="),
  GREATER_EQUALS(">="), GREATER_THAN(">"), LEFT_PAREN("("), RIGHT_PAREN(")"),
  LEFT_BRACKET("["), RIGHT_BRACKET("]"), LEFT_BRACE("{"), RIGHT_BRACE("}"),
  UP_ARROW("^"), DOT_DOT(".."),

  IDENTIFIER, INTEGER, REAL, STRING,
  ERROR, END_OF_FILE;

  private final static int FIRST_RESERVED_WORD_INDEX = AND.ordinal();
  private final static int LAST_RESERVED_WORD_INDEX = WITH.ordinal();

  private final static int FIRST_SPECIAL_INDEX = PLUS.ordinal();
  private final static int LAST_SPECIAL_INDEX = DOT_DOT.ordinal();

  /** Token text */
  private final String text;

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

  /** Set of lower-cased Pascal reserved word text strings. */
  public static final HashSet<String> RESERVED_WORDS = new HashSet<>();
  static {
    PascalTokenType[] values = PascalTokenType.values();
    for (int i = FIRST_RESERVED_WORD_INDEX; i <= LAST_RESERVED_WORD_INDEX; ++i) {
      RESERVED_WORDS.add(values[i].getText().toLowerCase());
    }
  }

  /** Hash table of Pascal special symbols. Each special symbol;s text is the key to its Pascal token type. */
  public static final Hashtable<String, PascalTokenType> SPECIAL_SYMBOLS = new Hashtable<>();
  static {
    PascalTokenType[] values = PascalTokenType.values();
    for (int i = FIRST_SPECIAL_INDEX; i <= LAST_SPECIAL_INDEX; ++i) {
      SPECIAL_SYMBOLS.put(values[i].getText(), values[i]);
    }
  }
}
