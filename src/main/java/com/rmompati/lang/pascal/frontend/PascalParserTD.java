package com.rmompati.lang.pascal.frontend;

import com.rmompati.lang.frontend.Parser;
import com.rmompati.lang.frontend.Scanner;
import com.rmompati.lang.frontend.token.EofToken;
import com.rmompati.lang.frontend.token.Token;
import com.rmompati.lang.message.Message;
import com.rmompati.lang.message.MessageType;

/**
 * <h1>PascalParserTD</h1>
 *
 * <p>The top-down Pascal parser.</p>
 */
public class PascalParserTD extends Parser {
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

    while (!((token = nextToken()) instanceof EofToken)) {}

    // Send the parser summary message.
    float elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;
    sendMessage(new Message(MessageType.PARSER_SUMMARY, new Number[]{token.getLineNum(), getErrorCount(), elapsedTime}));
  }

  /**
   * Return the number of syntax errors found by the parser.
   * To be implemented by a language-specific parser subclass.
   *
   * @return the error count.
   */
  @Override
  public int getErrorCount() {
    return 0;
  }
}
