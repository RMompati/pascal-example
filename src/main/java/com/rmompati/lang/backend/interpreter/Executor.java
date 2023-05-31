package com.rmompati.lang.backend.interpreter;

import com.rmompati.lang.backend.Backend;
import com.rmompati.lang.intermediate.ICode;
import com.rmompati.lang.intermediate.SymTable;
import com.rmompati.lang.message.Message;
import com.rmompati.lang.message.MessageListener;
import com.rmompati.lang.message.MessageType;

/**
 * <h1>Executor</h1>
 *
 * <p>A backend component for processing and executing the source program.</p>
 */
public class Executor extends Backend {
  /**
   * Process the intermediate code and the symbol table generated by the parser to execute the source program.
   *
   * @param iCode    the intermediate code.
   * @param symTable the symbol table.
   * @throws Exception if an exception occurs.
   */
  @Override
  public void process(ICode iCode, SymTable symTable) throws Exception {
    long startTime = System.currentTimeMillis();
    float elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;
    int executionCount = 0;
    int runtimeErrors = 0;

    // Send the interpreter summary.
    sendMessage(new Message(MessageType.INTERPRETER_SUMMARY, new Number[]{executionCount, runtimeErrors, elapsedTime}));
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
}
