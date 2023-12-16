package com.rmompati.lang.backend.interpreter.executors;

import com.rmompati.lang.backend.interpreter.Executor;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl;
import com.rmompati.lang.message.Message;

import static com.rmompati.lang.backend.interpreter.RuntimeErrorCode.UNIMPLEMENTED_FEATURE;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeKeyImpl.LINE;
import static com.rmompati.lang.message.MessageType.SOURCE_LINE;

/**
 * <h1>StatementExecutor</h1>
 *
 * <p>The statement executor.</p>*/
public class StatementExecutor extends Executor {

  /**
   * Constructor for subclasses.
   *
   * @param parent the parent executor.
   */
  public StatementExecutor(Executor parent) {
    super(parent);
  }

  /**
   * Execute a statement.
   * To be overridden by the specialized statement executor subclasses.
   * @param node the root node of the statement.
   * @return  null
   */
  public Object execute(ICodeNode node) {

    ICodeNodeTypeImpl nodeType = (ICodeNodeTypeImpl) node.getType();

    // Send a message about the current source line
    sendSourceLineMessage(node);

    switch (nodeType) {
      case COMPOUND: {
        CompoundExecutor compoundExecutor = new CompoundExecutor(this);
        return compoundExecutor.execute(node);
      }
      case ASSIGN: {
        AssignmentExecutor assignmentExecutor = new AssignmentExecutor(this);
        return assignmentExecutor.execute(node);
      }
      case LOOP: {
        LoopExecutor loopExecutor = new LoopExecutor(this);
        return loopExecutor.execute(node);
      }
      case IF: {
        IfExecutor ifExecutor = new IfExecutor(this);
        return ifExecutor.execute(node);
      }
      case SELECT: {
        SelectExecutor selectExecutor = new SelectExecutor(this);
        return selectExecutor.execute(node);
      }
      case NO_OP: return null;
      default: {
        errorHandler.flag(node, UNIMPLEMENTED_FEATURE, this);
        return null;
      }
    }
  }

  private void sendSourceLineMessage(ICodeNode node) {
    Object lineNumber = node.getAttribute(LINE);

    // Send the SOURCE_LINE message.
    if (lineNumber != null) {
      sendMessage(new Message(SOURCE_LINE, lineNumber));
    }
  }
}
