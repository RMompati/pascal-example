package com.rmompati.lang.pascal.frontend.tokens;

import com.rmompati.lang.frontend.Source;
import com.rmompati.lang.pascal.frontend.PascalToken;

import static com.rmompati.lang.frontend.Source.EOF;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.ERROR;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.STRING;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.UNEXPECTED_EOF;

public class PascalStringToken extends PascalToken {
  /**
   * Constructor.
   *
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  public PascalStringToken(Source source) throws Exception {
    super(source);
  }

  /**
   * Extracts Pascal String token from the source.
   * 
   * @throws Exception if an error occurs.
   */
  @Override
  protected void extract() throws Exception {
    StringBuilder textBuffer = new StringBuilder();
    StringBuilder valueBuffer = new StringBuilder();

    char currentChar = nextChar(); // Consume the initial quote.
    textBuffer.append('\'');

    // Get String characters
    do {
      // Replace any whitespace character with a blank.
      if (Character.isWhitespace(currentChar)) {
        currentChar = ' ';
      }

      if ((currentChar != '\'') && (currentChar != EOF)) {
        textBuffer.append(currentChar);
        valueBuffer.append(currentChar);
        currentChar = nextChar();
      }

      if (currentChar == '\'') {
        while ((currentChar == '\'') && (peekChar()) == '\'') {
          textBuffer.append("''");
          valueBuffer.append(currentChar);
          nextChar();
          currentChar = nextChar();
        }
      }
    } while ((currentChar != '\'') && (currentChar != EOF));

    if (currentChar == '\'') {
      nextChar();
      textBuffer.append('\'');

      type = STRING;
      value = valueBuffer.toString();
    } else {
      type = ERROR;
      value = UNEXPECTED_EOF;
    }

    text = textBuffer.toString();
  }
}
