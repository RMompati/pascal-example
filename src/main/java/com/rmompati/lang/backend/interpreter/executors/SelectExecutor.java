package com.rmompati.lang.backend.interpreter.executors;

import com.rmompati.lang.backend.interpreter.Executor;

/**
 * <h1>SelectExecutor</h1>
 *
 * <p>Executes select statements.</p>
 */
public class SelectExecutor extends StatementExecutor {

  /**
   * Constructor.
   *
   * @param parent the parent executor.
   */
  public SelectExecutor(Executor parent) {
    super(parent);
  }
}
