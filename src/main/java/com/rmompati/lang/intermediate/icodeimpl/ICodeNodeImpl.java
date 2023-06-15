package com.rmompati.lang.intermediate.icodeimpl;

import com.rmompati.lang.intermediate.ICodeFactory;
import com.rmompati.lang.intermediate.ICodeKey;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.intermediate.ICodeNodeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <h1>ICodeNodeImpl</h1>
 *
 * <p>An implementation of a node of the intermediate code.</p>
 */
public class ICodeNodeImpl extends HashMap<ICodeKey, Object> implements ICodeNode {

  private final ICodeNodeType type;
  private ICodeNode parent;
  private final ArrayList<ICodeNode> children;

  public ICodeNodeImpl(ICodeNodeType type) {
    this.type = type;
    this.parent = null;
    this.children = new ArrayList<>();
  }

  /**
   * Gets the node type.
   *
   * @return the node type.
   */
  @Override
  public ICodeNodeType getType() {
    return type;
  }

  /**
   * Gets the parent of this node.
   *
   * @return the parent of this node.
   */
  @Override
  public ICodeNode getParent() {
    return parent;
  }

  /**
   * Adds a child node to this node.
   *
   * @param node the child node.
   * @return the child node.
   */
  @Override
  public ICodeNode addChild(ICodeNode node) {
    if (node != null) {
      children.add(node);
      ((ICodeNodeImpl) node).parent = this;
    }

    return node;
  }

  /**
   * Gets all the child nodes of this node.
   *
   * @return an array list of child nodes.
   */
  @Override
  public ArrayList<ICodeNode> getChildren() {
    return children;
  }

  /**
   * Sets a node attribute.
   *
   * @param key   the attribute key.
   * @param value the attribute value.
   */
  @Override
  public void setAttribute(ICodeKey key, Object value) {
    put(key, value);
  }

  /**
   * Gets the value of a node attribute.
   *
   * @param key the attribute key.
   * @return the attribute value.
   */
  @Override
  public Object getAttribute(ICodeKey key) {
    return get(key);
  }

  /**
   * Makes a copy of this node.
   *
   * @return the copy.
   */
  @Override
  public ICodeNode copy() {
    ICodeNodeImpl copy = (ICodeNodeImpl) ICodeFactory.createICodeNode(type);

    Set<Map.Entry<ICodeKey, Object>> attributes = entrySet();

    // Copy attributes;
    for (Entry<ICodeKey, Object> attribute : attributes) {
      copy.put(attribute.getKey(), attribute.getValue());
    }

    return copy;
  }

  @Override
  public String toString() {
    return type.toString();
  }
}
