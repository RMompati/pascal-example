package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.intermediate.ICodeFactory;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.pascal.frontend.PascalParserTD;

import static com.rmompati.lang.intermediate.icodeimpl.ICodeNodeTypeImpl.COMPOUND;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.END;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.MISSING_END;

/**
 * <h1>CompoundStatementParser</h1>
 *
 * <p>Parses the compound statement.</p>
 */
public class CompoundStatementParser extends StatementParser {

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public CompoundStatementParser(PascalParserTD parent) {
    super(parent);
  }

  /***/
  public ICodeNode parse(Token token) throws Exception {
    token = nextToken(); // Consume the "BEGIN" token.

    // Create the COMPOUND node.
    ICodeNode compoundNode = ICodeFactory.createICodeNode(COMPOUND);

    // Parse the statement list terminated by the END token.
    StatementParser statementParser = new StatementParser(this);
    statementParser.parseList(token, compoundNode, END, MISSING_END);

    return compoundNode;
  }
}
