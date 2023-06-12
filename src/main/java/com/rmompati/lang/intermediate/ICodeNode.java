package com.rmompati.lang.intermediate;

import java.util.ArrayList;

/**
 * <h1>ICodeNode</h1>
 *
 * <p>The interface for a node of the intermediate code.</p>
 */
public interface ICodeNode {

  /**
   * Gets the node type.
   * @return the node type.
   */
  ICodeNodeType getType();

  /**
   * Gets the parent of this node.
   * @return the parent of this node.
   */
  ICodeNode getParent();

  /**
   * Adds a child node to this node.
   * @param node the child node.
   * @return the child node.
   */
  ICodeNode addChild(ICodeNode node);

  /**
   * Gets all the child nodes of this node.
   * @return an array list of child nodes.
   */
  ArrayList<ICodeNode> getChildren();

  /**
   * Sets a node attribute.
   * @param key the attribute key.
   * @param value the attribute value.
   */
  void setAttribute(ICodeKey key, Object value);

  /**
   * Gets the value of a node attribute.
   * @param key the attribute key.
   * @return the attribute value.
   */
  Object getAttribute(ICodeKey key);

  /**
   * Makes a copy of this node.
   * @return the copy.
   */
  ICodeNode copy();
}
