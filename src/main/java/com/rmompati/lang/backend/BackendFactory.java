package com.rmompati.lang.backend;

import com.rmompati.lang.backend.compiler.CodeGenerator;
import com.rmompati.lang.backend.interpreter.Executor;

/**
 * <h1>BackendFactory</h1>
 *
 * <p>A factory class that creates compiler and interpreter components.</p>
 */
public class BackendFactory {

  public static Backend createBackend(String operation) throws Exception {
    if (operation.equalsIgnoreCase("compile")) {
      return new CodeGenerator();
    } else if (operation.equalsIgnoreCase("execute")) {
      return new Executor();
    } else {
      throw new Exception("Backend Factory: Invalid operation \"" + operation + "\"");
    }
  }
}
