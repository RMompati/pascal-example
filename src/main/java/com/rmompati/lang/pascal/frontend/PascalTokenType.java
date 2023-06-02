package com.rmompati.lang.pascal.frontend;

import com.rmompati.lang.frontend.TokenType;

public enum PascalTokenType implements TokenType {
  ERROR;

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
