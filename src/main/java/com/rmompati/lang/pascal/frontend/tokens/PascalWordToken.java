package com.rmompati.lang.pascal.frontend.tokens;

import com.rmompati.lang.frontend.Source;
import com.rmompati.lang.pascal.frontend.PascalToken;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import static com.rmompati.lang.pascal.frontend.PascalTokenType.IDENTIFIER;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.RESERVED_WORDS;

public class PascalWordToken extends PascalToken {
  /**
   * Constructor.
   *
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  public PascalWordToken(Source source) throws Exception {
    super(source);
  }

  /**
   * Extracts a pascal word token from the source.
   *
   * @throws Exception if an error occurs.
   */
  @Override
  protected void extract() throws Exception {
    StringBuilder pascalWord = new StringBuilder();
    char currentChar = currentChar();

    // Get the word characters (letter or digit). The scanner has already determined
    // that the first character is a letter.
    while (Character.isLetterOrDigit(currentChar)) {
      pascalWord.append(currentChar);
      currentChar = nextChar();
    }

    text = pascalWord.toString();

    // Is it a reserved word or an identifier.
    type = (RESERVED_WORDS.contains(text.toLowerCase())) ?
        PascalTokenType.valueOf(text.toUpperCase()) // reserved word.
        : IDENTIFIER;                               // identifier.
  }
}
