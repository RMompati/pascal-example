package com.rmompati.lang.frontend;

import com.rmompati.lang.intermediate.ICode;
import com.rmompati.lang.intermediate.SymTable;
import com.rmompati.lang.message.Message;
import com.rmompati.lang.message.MessageHandler;
import com.rmompati.lang.message.MessageListener;
import com.rmompati.lang.message.MessageProducer;
import com.rmompati.lang.frontend.token.Token;

/**
 * <h1>Parser</h1>
 *
 * <p>A language-independent framework class. This abstract parser class will be implemented by language-specific
 * sub-classes.</p>
 * */
public abstract class Parser implements MessageProducer {

  /** The generated symbol table. */
  protected static SymTable symTable;
  protected static MessageHandler messageHandler;
  static {
    symTable = null;
    messageHandler = new MessageHandler();
  }

  /** The scanner used with this parser. */
  protected Scanner scanner;
  /** The intermediate code generated by this parser. */
  protected ICode iCode;

  /**
   * Constructor
   *
   * @param scanner the scanner to be used with this parser.
   */
  protected Parser(Scanner scanner) {
    this.scanner = scanner;
    this.iCode = null;
  }

  /**
   * Parse a source program and generate the intermediate code and the symbol table.
   * To be implemented by a language-specific parser subclass.
   *
   * @throws Exception if an error occurs.
   */
  public abstract void parse() throws Exception;

  /**
   * Return the number of syntax errors found by the parser.
   * To be implemented by a language-specific parser subclass.
   *
   * @return the error count.
   */
  public abstract int getErrorCount();

  /**
   * Call the scanner's {@link Scanner#currentToken()} method.
   * @return the current token.
   */
  public Token currentToken() {
    return scanner.currentToken();
  }

  /**
   * Call the scanner's {@link Scanner#nextToken()} method.
   * @return the current token.
   * @throws Exception if an error occurs.
   */
  public Token nextToken() throws Exception {
    return scanner.nextToken();
  }

  /**
   * Adds a lister to the listener list.
   *
   * @param listener the listener to add.
   */
  @Override
  public void addMessageListener(MessageListener listener) {
    messageHandler.addListener(listener);
  }

  /**
   * Remove a listener from the listener list.
   *
   * @param listener the listener to remove.
   */
  @Override
  public void removeMessageListener(MessageListener listener) {
    messageHandler.removeListener(listener);
  }

  /**
   * Notify listeners after setting the message.
   *
   * @param message the message to send.
   */
  @Override
  public void sendMessage(Message message) {
    messageHandler.sendMessage(message);
  }

  public ICode getiCode() {
    return iCode;
  }

  public SymTable getSymTable() {
    return symTable;
  }
}
