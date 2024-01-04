package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;
import com.rmompati.lang.pascal.intermediate.ICodeFactory;
import com.rmompati.lang.pascal.intermediate.symtableimpl.Predefined;
import com.rmompati.lang.pascal.intermediate.typeimpl.TypeChecker;

import java.util.EnumSet;

import static com.rmompati.lang.pascal.frontend.PascalTokenType.COLON_EQUALS;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.INCOMPATIBLE_TYPES;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.MISSING_COLON_EQUALS;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl.ASSIGN;

public class AssignmentStatementParser extends StatementParser {

  // Synchronization set for the ":=" token.
  private static final EnumSet<PascalTokenType> COLON_EQUALS_SET =
      ExpressionParser.EXPR_START_SET.clone();

  static {
    COLON_EQUALS_SET.add(COLON_EQUALS);
    COLON_EQUALS_SET.addAll(StatementParser.STMT_FOLLOW_SET);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public AssignmentStatementParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parse a statement.
   * To be overridden by the specialized statement parser subclasses.
   *
   * @param token the initial token.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurs.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    // Create ASSIGN node.
    ICodeNode assignNode = ICodeFactory.createICodeNode(ASSIGN);

    // Parse the target variable
    VariableParser variableParser = new VariableParser(this);
    ICodeNode targetNode = variableParser.parse(token);
    TypeSpec targetType = targetNode != null ? targetNode.getTypeSpec() : Predefined.undefinedType;

    // The ASSIGN node adopts the variable nodes as its first child.
    assignNode.addChild(targetNode);

    // Synchronize the := token
    token = synchronize(COLON_EQUALS_SET);
    if (token.getType() == COLON_EQUALS) {
      token = nextToken();
    } else {
      errorHandler.flag(token, MISSING_COLON_EQUALS, this);
    }

    // Parse the expression. The ASSIGN node adopts the expression's node as it second child.
    ExpressionParser expressionParser = new ExpressionParser(this);
    ICodeNode exprNode = expressionParser.parse(token);
    assignNode.addChild(exprNode);

    // Type check: Assignment compatible?
    TypeSpec exprType = exprNode != null ? exprNode.getTypeSpec() : Predefined.undefinedType;
    if (!TypeChecker.areAssignmentCompatible(targetType, exprType)) {
      errorHandler.flag(token, INCOMPATIBLE_TYPES, this);
    }

    assignNode.setTypeSpec(targetType);
    return assignNode;
  }
}
