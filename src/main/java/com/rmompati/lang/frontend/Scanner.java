package com.rmompati.lang.frontend;

/**
 * <h1>Scanner</h1>
 *
 * <p>A language-independent framework class. This abstract scanner class will be implemented by language-specific
 * subclasses.</p>
 * */
public abstract class Scanner {
  protected Source source;
  private Token currentToken;

  /**
   * Constructor
   * @param source the source to be used with the scanner.
   */
  public Scanner(Source source) {
    this.source = source;
  }

  /**
   * @return the current token.
   */
  public Token currentToken() {
    return currentToken;
  }

  /**
   * Return the next token from the source.
   * @return the current token.
   * @throws Exception if an error occurs.
   */
  public Token nextToken() throws Exception {
    currentToken = extractToken();
    return currentToken;
  }

  /**
   * Do the actual work of extracting and returning the next token from the source. Implemented by scanner subclasses.
   * @return the next token.
   * @throws Exception if an error occurs.
   */
  protected abstract Token extractToken() throws Exception;

  /**
   * Call the source's {@link Source#currentChar()} method.
   * @return the current character from the source.
   * @throws Exception if an error occurs.
   */
  public char currentChar() throws Exception {
    return source.currentChar();
  }

  /**
   * Call the source's {@link Source#nextChar()} method.
   * @return the next character from the source.
   * @throws Exception if an error occurs.
   */
  public char nextChar() throws Exception {
    return source.nextChar();
  }
}
