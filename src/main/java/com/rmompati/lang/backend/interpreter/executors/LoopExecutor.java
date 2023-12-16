package com.rmompati.lang.backend.interpreter.executors;

import com.rmompati.lang.backend.interpreter.Executor;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl;

import java.util.List;

import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl.TEST;

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

  /**
   * Execute a loop statement.
   *
   * @param node the root node of the statement.
   * @return null
   */
  @Override
  public Object execute(ICodeNode node) {
    boolean exitLoop = false;
    ICodeNode exprNode = null;
    List<ICodeNode> loopChildren = node.getChildren();

    ExpressionExecutor expressionExecutor = new ExpressionExecutor(this);
    StatementExecutor statementExecutor = new StatementExecutor(this);

    // Loop until the "TEST" value is true.
    while (!exitLoop) {
      ++executionCount;

      // Execute the children of the loop.
      for (ICodeNode child : loopChildren) {
        ICodeNodeTypeImpl childType = (ICodeNodeTypeImpl) child.getType();

        // TEST node?
        if (childType == TEST) {
          if (exprNode == null) {
            exprNode = child.getChildren().get(0);
          }
          exitLoop = (Boolean) expressionExecutor.execute(exprNode);
        } else {
          statementExecutor.execute(child);
        }

        // Exit if the TEST expression value is true.
        if (exitLoop) break;
      }
    }
    return null;
  }
}
