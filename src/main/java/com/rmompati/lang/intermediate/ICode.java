package com.rmompati.lang.intermediate;

/**
 * <h1>ICode</h1>
 *
 * <p>The framework interface that represents the intermediate code.</p>
 */
public interface ICode {

  /**
   * Set and return the root node.
   * @param node the node to set as root.
   * @return the root node.
   */
  ICodeNode setRoot(ICodeNode node);

  /**
   * Gets the root node.
   * @return the root node.
   */
  ICodeNode getRoot();
}
