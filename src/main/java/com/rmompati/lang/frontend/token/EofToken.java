package com.rmompati.lang.frontend.token;

import com.rmompati.lang.frontend.Source;

public class EofToken extends Token {

  /**
   * Constructor.
   *
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  public EofToken(Source source) throws Exception {
    super(source);
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
}
