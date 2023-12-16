package com.rmompati.lang.backend.interpreter.executors;

import com.rmompati.lang.backend.interpreter.Executor;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.message.Message;

import java.util.ArrayList;

import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeKeyImpl.LINE;
import static com.rmompati.lang.pascal.intermediate.symtableimpl.SymTableKeyImpl.DATA_VALUE;
import static com.rmompati.lang.message.MessageType.ASSIGN;

/**
 * <h1>AssignmentExecutor</h1>
 *
 * <p>Executes assignment statements.</p>
 */
public class AssignmentExecutor extends StatementExecutor {
  /**
   * Constructor.
   *
   * @param parent the parent executor.
   */
  public AssignmentExecutor(Executor parent) {
    super(parent);
  }

  /**
   * Execute an assignment statement.
   *
   * @param node the root node of the statement.
   * @return null
   */
  @Override
  public Object execute(ICodeNode node) {
    // The ASSIGN node's children are the target variable and expression.
    ArrayList<ICodeNode> children = node.getChildren();
    ICodeNode variableNode = children.get(0);
    ICodeNode expressionNode = children.get(1);

    // Execute the expression and get its value.
    ExpressionExecutor expressionExecutor = new ExpressionExecutor(this);
    Object value = expressionExecutor.execute(expressionNode);

    // Set the value as an attribute of the variable's symbol table.
    SymTableEntry variableId = (SymTableEntry) variableNode.getAttribute(ID);
    variableId.setAttribute(DATA_VALUE, value);

    sendMessage(node, variableId.getName(), value);

    ++executionCount;
    return null;
  }

  /**
   * Sends a message about the assignment operation.
   * @param node ASSIGN node.
   * @param variableName the name of the target variable.
   * @param value the value of the expression.
   */
  private void sendMessage(ICodeNode node, String variableName, Object value) {
    Object lineNumber = node.getAttribute(LINE);

    // Send an ASSIGN message.
    if (lineNumber != null) {
      sendMessage(new Message(ASSIGN, new Object[]{lineNumber, variableName, value}));
    }
  }
}
