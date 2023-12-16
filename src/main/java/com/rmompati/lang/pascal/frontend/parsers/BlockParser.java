package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.frontend.TokenType;
import com.rmompati.lang.pascal.intermediate.ICodeFactory;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.pascal.frontend.PascalParserTD;

import static com.rmompati.lang.pascal.intermediate.icodeimpl.ICodeNodeTypeImpl.COMPOUND;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.BEGIN;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.END;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.MISSING_BEGIN;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.MISSING_END;

public class BlockParser extends PascalParserTD {

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public BlockParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses a block.
   * @param token the initial token.
   * @param routineId the symbol table entry of the routine name.
   * @return the root node of the parse tree.
   * @throws Exception if an error occurs.
   */
  public ICodeNode parse(Token token, SymTableEntry routineId) throws Exception {
    DeclarationsParser declarationsParser = new DeclarationsParser(this);
    StatementParser statementParser = new StatementParser(this);

    // Parse any declarations
    declarationsParser.parse(token);

    token = synchronize(StatementParser.STMT_START_SET);
    TokenType tokenType = token.getType();
    ICodeNode rootNode = null;

    // Look for the "BEGIN" token to parse a compound statement.
    if (token.getType() == BEGIN) {
      rootNode = statementParser.parse(token);

    } else { // Missing BEGIN: Attempt to parse anyway if possible.
      errorHandler.flag(token, MISSING_BEGIN, this);

      if (StatementParser.STMT_START_SET.contains(tokenType)) {
        rootNode = ICodeFactory.createICodeNode(COMPOUND);
        statementParser.parseList(token, rootNode, END, MISSING_END);
      }
    }
    return rootNode;
  }
}
