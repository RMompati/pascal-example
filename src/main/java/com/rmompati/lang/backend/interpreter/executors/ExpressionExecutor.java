package com.rmompati.lang.backend.interpreter.executors;

import com.rmompati.lang.backend.interpreter.Executor;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.rmompati.lang.backend.interpreter.RuntimeErrorCode.DIVISION_BY_ZERO;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeKeyImpl.VALUE;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static com.rmompati.lang.pascal.intermediate.symtableimpl.SymTableKeyImpl.DATA_VALUE;

/**
 * <h1>ExpressionExecutor</h1>
 *
 * <p>Executes expression statements.</p>
 */
public class ExpressionExecutor extends StatementExecutor {

  // Set of arithmetic operator node types.
  private static final EnumSet<ICodeNodeTypeImpl> ARITH_OPS =
      EnumSet.of(ADD, SUBTRACT, MULTIPLY, FLOAT_DIVIDE, INTEGER_DIVIDE);

  /**
   * Constructor.
   *
   * @param parent the parent executor.
   */
  public ExpressionExecutor(Executor parent) {
    super(parent);
  }

  /**
   * Execute an expression.
   *
   * @param node the root intermediate code node of the compound statement.
   * @return the computed value of the expression.
   */
  @Override
  public Object execute(ICodeNode node) {

    ICodeNodeTypeImpl nodeType = (ICodeNodeTypeImpl) node.getType();

    switch (nodeType) {
      case VARIABLE: {
        // Get the variable's symbol table entry, and it's value.
        SymTableEntry entry = (SymTableEntry) node.getAttribute(ID);
        return entry.getAttribute(DATA_VALUE);
      }
      case INTEGER_CONSTANT: {
        // Return the integer value
        return (Integer) node.getAttribute(VALUE);
      }
      case REAL_CONSTANT: {
        // Return the float value.
        return (Float) node.getAttribute(VALUE);
      }
      case STRING_CONSTANT: {
        return (String) node.getAttribute(VALUE);
      }
      case NEGATE: {
        // Get the NEGATE node's expression node child.
        ArrayList<ICodeNode> children = node.getChildren();
        ICodeNode expressionNode = children.get(0);

        // Execute the expression and return the negative of its value.
        Object value = execute(expressionNode);
        if (value instanceof Integer) {
          return -((Integer) value);
        } else {
          return -((Float) value);
        }
      }
      case NOT: {
        // Get the NOT node's expression node child.
        ArrayList<ICodeNode> children = node.getChildren();
        ICodeNode expressionNode = children.get(0);

        // Execute the expression and return the "not" of it's value.
        boolean value = (Boolean) execute(expressionNode);
        return !value;
      }
      default: return executeBinaryOperator(node, nodeType);
    }
  }

  /**
   * Execute a binary operator.
   * @param node the root node of the expression.
   * @param nodeType the node type.
   * @return the computed value of the expression.
   */
  private Object executeBinaryOperator(ICodeNode node, ICodeNodeTypeImpl nodeType) {
    // Get the two operand children of the operator node.
    List<ICodeNode> children = node.getChildren();
    ICodeNode operandNode1 = children.get(0);
    ICodeNode operandNode2 = children.get(1);

    // Operands
    Object operand1 = execute(operandNode1);
    Object operand2 = execute(operandNode2);

    boolean integerMode = (operand1 instanceof Integer) && (operand2 instanceof Integer);

    if (ARITH_OPS.contains(nodeType)) {
      if (integerMode) {
        int value1 = (Integer) operand1;
        int value2 = (Integer) operand2;

        // Integer operations.
        switch (nodeType) {
          case ADD: return value1 + value2;
          case SUBTRACT: return value1 - value2;
          case MULTIPLY: return value1 * value2;
          case FLOAT_DIVIDE:
          case INTEGER_DIVIDE: {
            // Check division by zero
            if (value2 != 0) {
              return (((float) value1) / ((float) value2));
            } else {
              errorHandler.flag(node, DIVISION_BY_ZERO, this);
              return 0;
            }
          }
          case MOD: {
            // Check division by zero
            if (value2 != 0) {
              return value1 % value2;
            } else {
              errorHandler.flag(node, DIVISION_BY_ZERO, this);
              return 0;
            }
          }
        }
      } else {
        float value1 = operand1 instanceof Integer ? (Integer) operand1 : (Float) operand1;
        float value2 = operand2 instanceof Integer ? (Integer) operand2 : (Float) operand2;

        // Float operations
        switch (nodeType) {
          case ADD: return value1 + value2;
          case SUBTRACT: return value1 - value2;
          case MULTIPLY: return value1 * value2;
          case FLOAT_DIVIDE: {
            // Check division by zero
            if (value2 != 0.0f) {
              return value1 / value2;
            } else {
              errorHandler.flag(node, DIVISION_BY_ZERO, this);
              return 0.0f;
            }
          }
      }
    }
    return null;
  } else if ((nodeType == AND) || (nodeType == OR)) {
      boolean value1 = (Boolean) operand1;
      boolean value2 = (Boolean) operand2;
      return (nodeType == AND) ? value1 && value2 : value1 || value2;
    } else if (integerMode) {
      int value1 = (Integer) operand1;
      int value2 = (Integer) operand2;

      switch (nodeType) {
        case EQ: return value1 == value2;
        case NE: return value1 != value2;
        case LT: return value1 < value2;
        case LE: return value1 <= value2;
        case GT: return value1 > value2;
        case GE: return value1 >= value2;
      }
    } else {
      float value1 = operand1 instanceof Integer ? (Integer) operand1 : (Float) operand1;
      float value2 = operand2 instanceof Integer ? (Integer) operand2 : (Float) operand2;
      switch (nodeType) {
        case EQ: return value1 == value2;
        case NE: return value1 != value2;
        case LT: return value1 < value2;
        case LE: return value1 <= value2;
        case GT: return value1 > value2;
        case GE: return value1 >= value2;
      }
    }
    return 0; // Should never reach here.
  }
  private Object executeOperator() {
    return new Object();
  }
}
