package com.rmompati.lang.pascal.frontend.tokens;

import com.rmompati.lang.frontend.Source;
import com.rmompati.lang.pascal.frontend.PascalToken;

public class PascalNumberToken extends PascalToken {
  /**
   * Constructor.
   *
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  public PascalNumberToken(Source source) throws Exception {
    super(source);
  }

  /**
   * Extracts exactly one-character tokens from the source.
   * 
   * @throws Exception if an error occurs.
   */
  @Override
  protected void extract() throws Exception {
    super.extract();
  }
}
