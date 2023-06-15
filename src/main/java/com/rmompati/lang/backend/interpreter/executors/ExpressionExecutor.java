package com.rmompati.lang.backend.interpreter.executors;

import com.rmompati.lang.backend.interpreter.Executor;

/**
 * <h1>ExpressionExecutor</h1>
 *
 * <p>Executes expression statements.</p>
 */
public class ExpressionExecutor extends StatementExecutor {
  /**
   * Constructor.
   *
   * @param parent the parent executor.
   */
  public ExpressionExecutor(Executor parent) {
    super(parent);
  }

  private Object executeOperator() {
    return new Object();
  }
}
