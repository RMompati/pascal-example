package com.rmompati.lang.frontend;

public class EofToken extends Token {

  private final TokenType tokenType;

  /**
   * Constructor.
   *
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  public EofToken(Source source, TokenType tokenType) throws Exception {
    super(source);
    this.tokenType = tokenType;
  }

  /**
   * Do nothing. Do not consume any source characters.
   * Default method to extract exactly one-character tokens from the source.
   * Subclasses can override this method to construct language-specific tokens. After extracting the token, the
   * current source line position will be one beyond the last token character.
   *
   * @throws Exception if an error occurs.
   */
  @Override
  protected void extract() throws Exception {}

  public TokenType getTokenType() {
    return tokenType;
  }
}
