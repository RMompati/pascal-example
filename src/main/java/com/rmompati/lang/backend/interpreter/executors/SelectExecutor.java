package com.rmompati.lang.backend.interpreter.executors;

import com.rmompati.lang.backend.interpreter.Executor;
import com.rmompati.lang.intermediate.ICodeNode;

import java.util.ArrayList;

import static com.rmompati.lang.intermediate.icodeimpl.ICodeKeyImpl.VALUE;

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

  /**
   * Executes a select statement.
   *
   * @param node the root node of the statement.
   * @return null
   */
  @Override
  public Object execute(ICodeNode node) {
    // Get the "SELECT" node's children.
    ArrayList<ICodeNode> selectChildren = node.getChildren();
    ICodeNode exprNode = selectChildren.get(0);

    // Execute the "SELECT" expression.
    ExpressionExecutor expressionExecutor = new ExpressionExecutor(this);
    Object selectValue = expressionExecutor.execute(exprNode);

    // Attempt to select a "SELECT_BRANCH".
    ICodeNode selectedBranchNode = searchBranches(selectValue, selectChildren);

    if (selectedBranchNode != null) {
      ICodeNode stmtNode = selectedBranchNode.getChildren().get(1);
      StatementExecutor statementExecutor = new StatementExecutor(this);
      statementExecutor.execute(stmtNode);
    }

    ++executionCount;
    return null;
  }

  /**
   * Search the "SELECT_BRANCH-es" to find a match.
   * @param selectValue the value to match.
   * @param selectChildren the children of the select node.
   * @return the select branch node of matching select value.
   */
  private ICodeNode searchBranches(Object selectValue, ArrayList<ICodeNode> selectChildren) {
    // Loop over the "SELECT_BRANCH-es" to find a map.
    for (int i = 1; i < selectChildren.size(); ++i) {
      ICodeNode branchNode = selectChildren.get(i);
      if (searchConstants(selectValue, branchNode)) {
        return branchNode;
      }
    }
    return null;
  }

  /**
   * Searches the constants of a SELECT_BRANCH for matching value.
   * @param selectValue the value to match.
   * @param branchNode the SELECT_BRANCH node.
   * @return true if a match is found, false otherwise.
   */
  private boolean searchConstants(Object selectValue, ICodeNode branchNode) {
    boolean integerMode = selectValue instanceof Integer;

    ICodeNode constantsNode = branchNode.getChildren().get(0);
    ArrayList<ICodeNode> constantsList = constantsNode.getChildren();

    // Search the list of constants.
    if (integerMode) {
      for (ICodeNode constantNode : constantsList) {
        int constant = (Integer) constantNode.getAttribute(VALUE);
        if (((Integer) selectValue) == constant) return true;
      }
    } else {
      for (ICodeNode constantNode : constantsList) {
        String constant = (String) constantNode.getAttribute(VALUE);
        if (((String) selectValue).equals(constant)) return true;
      }
    }
    return false;
  }
}
