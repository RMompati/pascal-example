package com.rmompati.lang.backend.interpreter;

import com.rmompati.lang.backend.Backend;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.message.Message;

import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeKeyImpl.LINE;
import static com.rmompati.lang.message.MessageType.RUNTIME_ERROR;

/**
 * <h1>RuntimeErrorHandler</h1>
 *
 * <p>Runtime error handler for the backend interpreter.</p>
 */
public class RuntimeErrorHandler {
  private static final int MAX_ERRORS = 5;

  /** Count for runtime errors. */
  private static int errorCount = 0;

  /**
   * Flag a runtime error.
   *
   * @param node the root node of the offending statement or expressing.
   * @param errorCode the runtime error code.
   * @param backend the backend processor.
   */
  public void flag(ICodeNode node, RuntimeErrorCode errorCode, Backend backend) {
    // String lineNumber = null;

    // Look for the ancestor statement node with line number attribute.
    while ((node != null) && (node.getAttribute(LINE) == null)) {
      node = node.getParent();
    }

    // Notify the interpreter's listeners.
    backend.sendMessage(new Message(RUNTIME_ERROR, new Object[]{errorCode.toString(), node.getAttribute(LINE)}));

    if (++errorCount > MAX_ERRORS) {
      System.out.println("*** ABORTED AFTER TOO MANY RUNTIME ERRORS.");
      System.exit(-1);
    }
  }

  public int getErrorCount() {
    return errorCount;
  }
}
