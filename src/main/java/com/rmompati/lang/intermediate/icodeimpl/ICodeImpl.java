package com.rmompati.lang.intermediate.icodeimpl;

import com.rmompati.lang.intermediate.ICode;
import com.rmompati.lang.intermediate.ICodeNode;

/**
 * <h1>ICodeImpl</h1>
 *
 * <p>An implementation of the intermediate code as a parse tree.</p>
 */
public class ICodeImpl implements ICode {

  private ICodeNode root;

  /**
   * Set and return the root node.
   *
   * @param node the node to set as root.
   * @return the root node.
   */
  @Override
  public ICodeNode setRoot(ICodeNode node) {
    root = node;
    return node;
  }

  /**
   * Gets the root node.
   *
   * @return the root node.
   */
  @Override
  public ICodeNode getRoot() {
    return root;
  }
}
