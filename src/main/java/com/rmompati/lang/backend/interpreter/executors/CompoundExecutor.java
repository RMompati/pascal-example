package com.rmompati.lang.backend.interpreter.executors;

import com.rmompati.lang.backend.interpreter.Executor;
import com.rmompati.lang.intermediate.ICodeNode;

import java.util.ArrayList;

/**
 * <h1>CompoundExecutor</h1>
 *
 * <p>Executes compound statements.</p>
 */
public class CompoundExecutor extends StatementExecutor {
  /**
   * Constructor.
   *
   * @param parent the parent executor.
   */
  public CompoundExecutor(Executor parent) {
    super(parent);
  }

  /**
   * Execute a compound statement statement.
   *
   * @param node the root node of the statement.
   * @return null
   */
  @Override
  public Object execute(ICodeNode node) {
    // Loop over the children of the COMPOUND node and execute each child.
    StatementExecutor statementExecutor = new StatementExecutor(this);
    ArrayList<ICodeNode> children = node.getChildren();
    for (ICodeNode child : children) {
      statementExecutor.execute(child);
    }

    return null;
  }
}
