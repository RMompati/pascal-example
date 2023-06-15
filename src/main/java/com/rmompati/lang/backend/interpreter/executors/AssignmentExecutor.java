package com.rmompati.lang.backend.interpreter.executors;

import com.rmompati.lang.backend.interpreter.Executor;

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
}
