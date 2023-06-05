package com.rmompati.lang.pascal.frontend.error;

import com.rmompati.lang.frontend.Parser;
import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.message.Message;
import com.rmompati.lang.message.MessageType;

import static com.rmompati.lang.message.MessageType.SYNTAX_ERROR;

/**
 * <h1>PascalErrorHandler</h1>
 *
 * <p>Error handler Pascal syntax errors.</p>*/
public class PascalErrorHandler {
  private static final int MAX_ERRORS = 25;

  /** Count of syntax errors. */
  private static int errorCount = 0;

  /**
   * Flag an error in the source line.
   *
   * @param token the bad token.
   * @param parser the parser.
   */
  public void flag(Token token, PascalErrorCode errorCode, Parser parser) {
    // Notify the parser's listeners.
    parser.sendMessage(new Message(
        SYNTAX_ERROR, new Object[]{token.getLineNum(), token.getPosition(), token.getText(), errorCode.toString()}
    ));

    if (++errorCount > MAX_ERRORS) {
      abortTranslation(errorCode, parser);
    }
  }

  public void abortTranslation(PascalErrorCode errorCode, Parser parser) {
    // Notify the parser's listeners and then abort.
    String fatalText = "FATAL ERROR: " + errorCode.toString();
    parser.sendMessage(new Message(SYNTAX_ERROR, new Object[]{0, 0, "", fatalText}));
    System.exit(errorCode.getStatus());
  }

  public int getErrorCount() {
    return errorCount;
  }
}
