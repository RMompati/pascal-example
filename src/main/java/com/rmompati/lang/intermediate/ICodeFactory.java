package com.rmompati.lang.intermediate;

import com.rmompati.lang.intermediate.icodeimpl.ICodeImpl;
import com.rmompati.lang.intermediate.icodeimpl.ICodeNodeImpl;

/**
 * <h1>ICodeFactory</h1>
 *
 * <p>A factory for creating objects that implement the intermediate code.</p>
 */
public class ICodeFactory {

  /**
   * Creates and returns an intermediate code implementation.
   * @return the intermediate code implementation.
   */
  public static ICode createICode() {
    return new ICodeImpl();
  }

  /**
   * Creates and returns a node implementation.
   * @param type the type of the node.
   * @return the node implementation.
   */
  public static ICodeNode createICodeNode(ICodeNodeType type) {
    return new ICodeNodeImpl(type);
  }
}
