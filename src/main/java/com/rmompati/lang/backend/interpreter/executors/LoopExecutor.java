package com.rmompati.lang.backend.interpreter.executors;

import com.rmompati.lang.backend.interpreter.Executor;

/**
 * <h1>LoopExecutor</h1>
 *
 * <p>Executes loop statements.</p>
 */
public class LoopExecutor extends StatementExecutor {

  /**
   * Constructor.
   *
   * @param parent the parent executor.
   */
  public LoopExecutor(Executor parent) {
    super(parent);
  }
}
