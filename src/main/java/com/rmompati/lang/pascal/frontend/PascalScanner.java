package com.rmompati.lang.pascal.frontend;

import com.rmompati.lang.frontend.EofToken;
import com.rmompati.lang.frontend.Scanner;
import com.rmompati.lang.frontend.Source;
import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.pascal.frontend.error.PascalErrorCode;
import com.rmompati.lang.pascal.frontend.tokens.*;

import static com.rmompati.lang.frontend.Source.EOF;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.END_OF_FILE;

/**
 * <h1>PascalScanner</h1>
 *
 * <p>The Pascal Scanner.</p>
 */
public class PascalScanner extends Scanner {
  /**
   * Constructor
   *
   * @param source the source to be used with the scanner.
   */
  public PascalScanner(Source source) {
    super(source);
  }

  /**
   * Do the actual work of extracting and returning the next token from the source. Implemented by scanner subclasses.
   *
   * @return the next token.
   * @throws Exception if an error occurs.
   */
  @Override
  protected Token extractToken() throws Exception {
    skipWhitespaces();

    Token token;
    char currentChar = currentChar();

    if (currentChar == EOF) {
      token = new EofToken(source, END_OF_FILE);
    } else if(Character.isLetter(currentChar)) {
      token = new PascalWordToken(source);
    } else if(Character.isDigit(currentChar)) {
      token = new PascalNumberToken(source);
    } else if(currentChar == '\"') {
      token = new PascalStringToken(source);
    } else if(PascalTokenType.SPECIAL_SYMBOLS.containsKey(Character.toString(currentChar))) {
      token = new PascalSpecialSymbolToken(source);
    } else {
      token = new PascalErrorToken(source, PascalErrorCode.INVALID_CHARACTER, Character.toString(currentChar));
      nextChar();
    }
    
    return token;
  }

  /**
   * Skip whitespace characters by consuming them. A comment is whitespace.
   * @throws Exception if an error occurs.
   */
  private void skipWhitespaces() throws Exception {
    char currentChar = currentChar();

    while (Character.isWhitespace(currentChar) || (currentChar == '{')) {
      // Start of a comment?
      if (currentChar == '{') {
        do {
          currentChar = nextChar();
        } while ((currentChar != '}') && (currentChar != EOF));

        // Found closing '}'?
        if (currentChar == '}') {
          currentChar = nextChar();
        }
      } else { // Not a comment
        currentChar = nextChar();
      }
    }
  }
}
