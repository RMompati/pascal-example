package com.rmompati.lang.pascal.frontend;

import com.rmompati.lang.frontend.Source;
import com.rmompati.lang.frontend.Token;

/**
 * <h1>PascalToken</h1>
 *
 * <p>Base class for Pascal token classes.</p>
 */
public class PascalToken extends Token {
  /**
   * Constructor.
   *
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  public PascalToken(Source source) throws Exception {
    super(source);
  }
}
