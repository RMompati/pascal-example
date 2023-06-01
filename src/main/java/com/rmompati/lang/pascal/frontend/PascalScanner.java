package com.rmompati.lang.pascal.frontend;

import com.rmompati.lang.frontend.Scanner;
import com.rmompati.lang.frontend.Source;
import com.rmompati.lang.frontend.EofToken;
import com.rmompati.lang.frontend.Token;

import static com.rmompati.lang.frontend.Source.EOF;

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
    Token token;
    char currentChar = currentChar();

    if (currentChar == EOF) {
      token = new EofToken(source);
    } else {
      token = new Token(source);
    }
    
    return token;
  }
}
