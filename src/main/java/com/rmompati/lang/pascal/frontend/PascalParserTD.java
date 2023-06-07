package com.rmompati.lang.pascal.frontend;

import com.rmompati.lang.frontend.*;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.message.Message;
import com.rmompati.lang.message.MessageType;
import com.rmompati.lang.pascal.frontend.error.PascalErrorCode;
import com.rmompati.lang.pascal.frontend.error.PascalErrorHandler;

import java.io.IOException;

import static com.rmompati.lang.pascal.frontend.PascalTokenType.ERROR;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.IDENTIFIER;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.IO_ERROR;

/**
 * <h1>PascalParserTD</h1>
 *
 * <p>The top-down Pascal parser.</p>
 */
public class PascalParserTD extends Parser {

  protected static PascalErrorHandler errorHandler = new PascalErrorHandler();

  /**
   * Constructor
   *
   * @param scanner the scanner to be used with this parser.
   */
  public PascalParserTD(Scanner scanner) {
    super(scanner);
  }

  /**
   * Parse a source program and generate the intermediate code and the symbol table.
   * To be implemented by a language-specific parser subclass.
   *
   * @throws Exception if an error occurs.
   */
  @Override
  public void parse() throws Exception {
    Token token;
    long startTime = System.currentTimeMillis();

    try {
      while (!((token = nextToken()) instanceof EofToken)) {
        TokenType tokenType = token.getType();
        if (tokenType == IDENTIFIER) {
          String name = token.getText().toLowerCase();

          // If it's not already in the symbol table, create and enter a new entry for the identifier.
          SymTableEntry entry = symTabStack.lookup(name);
          if (entry == null) {
            entry = symTabStack.enterLocal(name);
          }

          // Append the current line number.
          entry.appendLineNumber(token.getLineNum());
        } else if (tokenType == ERROR){
          errorHandler.flag(token, (PascalErrorCode) token.getValue(), this);
        }
      }

      // Send the parser summary message.
      float elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;
      sendMessage(new Message(
          MessageType.PARSER_SUMMARY, new Number[]{token.getLineNum(), getErrorCount(), elapsedTime}
      ));
    } catch (IOException exc) {
      errorHandler.abortTranslation(IO_ERROR, this);
    }
  }

  /**
   * Return the number of syntax errors found by the parser.
   * To be implemented by a language-specific parser subclass.
   *
   * @return the error count.
   */
  @Override
  public int getErrorCount() {
    return errorHandler.getErrorCount();
  }
}
