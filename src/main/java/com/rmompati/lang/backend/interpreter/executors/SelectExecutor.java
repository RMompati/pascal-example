package com.rmompati.lang.backend.interpreter.executors;

import com.rmompati.lang.backend.interpreter.Executor;
import com.rmompati.lang.intermediate.ICodeNode;

import java.util.ArrayList;
import java.util.HashMap;

import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeKeyImpl.VALUE;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl.STRING_CONSTANT;

/**
 * <h1>SelectExecutor</h1>
 *
 * <p>Executes select statements.</p>
 */
public class SelectExecutor extends StatementExecutor {

  private static HashMap<ICodeNode, HashMap<Object, ICodeNode>> jumpCache =
      new HashMap<>();

  /**
   * Constructor.
   *
   * @param parent the parent executor.
   */
  public SelectExecutor(Executor parent) {
    super(parent);
  }

  /**
   * Executes a select statement.
   *
   * @param node the root node of the statement.
   * @return null
   */
  @Override
  public Object execute(ICodeNode node) {
    HashMap<Object, ICodeNode> jumpTable = jumpCache.get(node);
    if (jumpTable == null) {
      jumpTable = createJumpTable(node);
      jumpCache.put(node, jumpTable);
    }

    // Get the "SELECT" node's children.
    ArrayList<ICodeNode> selectChildren = node.getChildren();
    ICodeNode exprNode = selectChildren.get(0);

    // Evaluate the "SELECT" expression.
    ExpressionExecutor expressionExecutor = new ExpressionExecutor(this);
    Object selectValue = expressionExecutor.execute(exprNode);

    // If there is a selection, execute the SELECT_BRANCH's statement.
    ICodeNode statementNode = jumpTable.get(selectValue);
    if (statementNode != null) {
      StatementExecutor statementExecutor = new StatementExecutor(this);
      statementExecutor.execute(statementNode);
    }

    ++executionCount;
    return null;
  }

  /**
   * Create a jump table for a "SELECT" node.
   * @param node the "SELECT" node.
   * @return the jump table.
   */
  private HashMap<Object, ICodeNode> createJumpTable(ICodeNode node) {
    HashMap<Object, ICodeNode> jumpTable = new HashMap<>();
    // Loop over children that are select branch nodes.
    ArrayList<ICodeNode> selectChildren = node.getChildren();
    for (int i = 1; i < selectChildren.size(); ++i) {
      ICodeNode branchNode = selectChildren.get(i);
      ICodeNode constantsNode = branchNode.getChildren().get(0);
      ICodeNode statementNode = branchNode.getChildren().get(1);

      // Loop over the constants children of the branch's "CONSTANTS_NODE".
      ArrayList<ICodeNode> constantsList = constantsNode.getChildren();
      for (ICodeNode constantNode : constantsList) {
        Object value = constantNode.getAttribute(VALUE);
        if (constantNode.getType() == STRING_CONSTANT) {
          value = ((String) value).charAt(0);
        }
        jumpTable.put(value, statementNode);
      }
    }
    return jumpTable;
  }
}
