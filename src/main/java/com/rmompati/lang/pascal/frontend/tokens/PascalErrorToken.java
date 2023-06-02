package com.rmompati.lang.pascal.frontend.tokens;

import com.rmompati.lang.frontend.Source;
import com.rmompati.lang.pascal.frontend.PascalToken;
import com.rmompati.lang.pascal.frontend.error.PascalErrorCode;

import static com.rmompati.lang.pascal.frontend.PascalTokenType.ERROR;

/**
 * <h1>PascalErrorToken</h1>
 *
 * <p>Pascal error token</p>
 */
public class PascalErrorToken extends PascalToken {

  /**
   * Constructor.
   * @param source the source from where to fetch the token's characters.
   * @param errorCode the error code.
   * @param tokenText the text of the erroneous token.
   * @throws Exception if an error occurs.
   */
  public PascalErrorToken(Source source, PascalErrorCode errorCode, String tokenText) throws Exception {
    super(source);
    this.text = tokenText;
    this.type = ERROR;
    this.value = errorCode;
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
