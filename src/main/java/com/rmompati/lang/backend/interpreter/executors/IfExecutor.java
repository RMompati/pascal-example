package com.rmompati.lang.backend.interpreter.executors;

import com.rmompati.lang.backend.interpreter.Executor;

/**
 * <h1>IfExecutor</h1>
 *
 * <p>Executes if statements.</p>
 */
public class IfExecutor extends StatementExecutor {

  /**
   * Constructor.
   *
   * @param parent the parent executor.
   */
  public IfExecutor(Executor parent) {
    super(parent);
  }
}
