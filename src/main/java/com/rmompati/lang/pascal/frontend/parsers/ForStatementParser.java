package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.frontend.TokenType;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.intermediate.ICodeFactory;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;
import com.rmompati.lang.pascal.intermediate.symtableimpl.Predefined;
import com.rmompati.lang.pascal.intermediate.typeimpl.TypeChecker;

import java.util.EnumSet;

import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeKeyImpl.VALUE;
import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;
import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeFormImpl.ENUMERATION;

/**
 * <h1>ForStatementParser</h1>
 *
 * <p>A parser for the FOR statement</p>
 */
public class ForStatementParser extends StatementParser {

  // Synchronization set for TO or DOWNTO
  static final EnumSet<PascalTokenType> TO_DOWNTO_SET =
      ExpressionParser.EXPR_START_SET.clone();
  static {
    TO_DOWNTO_SET.add(TO);
    TO_DOWNTO_SET.add(DOWNTO);
    TO_DOWNTO_SET.addAll(StatementParser.STMT_FOLLOW_SET);
  }

  // Synchronization set for DO.
  private final static EnumSet<PascalTokenType> DO_SET =
      StatementParser.STMT_START_SET.clone();
  static {
    DO_SET.add(DO);
    DO_SET.addAll(StatementParser.STMT_FOLLOW_SET);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public ForStatementParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses a "FOR" statement.
   *
   * @param token the initial token.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurs.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    token = nextToken(); // Consume the for.
    Token targetToken = token;

    // Create the loop "COMPOUND", "LOOP", and "TEST" nodes.
    ICodeNode compoundNode = ICodeFactory.createICodeNode(COMPOUND);
    ICodeNode loopNode = ICodeFactory.createICodeNode(LOOP);
    ICodeNode testNode = ICodeFactory.createICodeNode(TEST);

    // Parse the embedded initial assignment.
    AssignmentStatementParser assignmentParser = new AssignmentStatementParser(this);
    ICodeNode initAssignNode = assignmentParser.parse(token);
    TypeSpec controlType = initAssignNode != null ? initAssignNode.getTypeSpec() : Predefined.undefinedType;

    // Type Check: The control variable's type must integer or enumeration.
    if (!TypeChecker.isInteger(controlType) && (controlType.getForm() != ENUMERATION)) {
      errorHandler.flag(token, INCOMPATIBLE_TYPES, this);
    }

    // Set the current line number attribute
    setLineNumber(initAssignNode, targetToken);

    // The "COMPOUND" node adopts the initial "ASSIGN" and "LOOP" nodes.
    compoundNode.addChild(initAssignNode);
    compoundNode.addChild(loopNode);

    // Synchronize at the "TO" or "DOWNTO"
    token = synchronize(TO_DOWNTO_SET);
    TokenType direction = token.getType();

    // Look for the "TO" or "DOWNTO"
    if ((direction == TO) || (direction == DOWNTO)) {
      token = nextToken(); // Consume the "TO" or "DOWNTO"
    } else {
      direction = TO;
      errorHandler.flag(token, MISSING_TO_DOWNTO, this);
    }

    // Create a relational operator node: "GT" for "TO" or "LT" for "DOWNTO"
    ICodeNode relOpNode = ICodeFactory.createICodeNode((direction == TO) ? GT : LT);
    relOpNode.setTypeSpec(Predefined.booleanType);

    // Copy the control "VARIABLE" node. The relational operator node adopts the
    // copied "VARIABLE" node as its first child.
    ICodeNode controlVarNode = initAssignNode.getChildren().get(0);
    relOpNode.addChild(controlVarNode.copy());

    // Parse the termination expression. The relational operator node adopts the expression as its second child.
    ExpressionParser expressionParser = new ExpressionParser(this);
    ICodeNode exprNode = expressionParser.parse(token);
    relOpNode.addChild(exprNode);

    // Type Check: The termination expression type must be an assignment compatible with the variable's type.
    TypeSpec exprType = exprNode != null ? exprNode.getTypeSpec() : Predefined.undefinedType;
    if (!TypeChecker.areAssignmentCompatible(controlType, exprType)) {
      errorHandler.flag(token, INCOMPATIBLE_TYPES, this);
    }

    // The "TEST" node adopts the relational operator nodes as its only child.
    // The "LOOP" node adopts the "TEST" node as its first child.
    testNode.addChild(relOpNode);
    loopNode.addChild(testNode);

    // Synchronize at the "DO"
    token = synchronize(DO_SET);
    if (token.getType() == DO) {
      token = nextToken(); // Consume the "DO"
    } else {
      errorHandler.flag(token, MISSING_DO, this);
    }

    // Parse the nested statement. The "LOOP" node adopts the statement node as its second child.
    StatementParser statementParser = new StatementParser(this);
    loopNode.addChild(statementParser.parse(token));


    // Create an assignment with a copy of the control variable to advance the value of the variable.
    ICodeNode nextAssignNode = ICodeFactory.createICodeNode(ASSIGN);
    nextAssignNode.addChild(controlVarNode.copy());

    // Create the arithmetic operator node. "ADD" for "TO", or "SUBTRACT" for "DOWNTO"
    ICodeNode arithmeticOpNode = ICodeFactory.createICodeNode((direction == TO) ? ADD : SUBTRACT);

    // The operator node adopts the copy of the loop variable as its first child and the value of 1 as its second child.
    arithmeticOpNode.addChild(controlVarNode.copy());
    ICodeNode oneNode = ICodeFactory.createICodeNode(INTEGER_CONSTANT);
    oneNode.setAttribute(VALUE, 1);
    arithmeticOpNode.addChild(oneNode);

    // The next "ASSIGN" node adopts the arithmetic operator node as its second child. The loop node adopts the next
    // "ASSIGN" as its third child.
    nextAssignNode.addChild(arithmeticOpNode);
    loopNode.addChild(nextAssignNode);

    // Set the current line number attribute.
    setLineNumber(nextAssignNode, targetToken);

    return compoundNode;
  }
}
