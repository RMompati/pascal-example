package com.rmompati.lang.pascal.frontend;

import com.rmompati.lang.frontend.*;
import com.rmompati.lang.intermediate.ICode;
import com.rmompati.lang.intermediate.ICodeFactory;
import com.rmompati.lang.intermediate.ICodeNode;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.intermediate.symtableimpl.DefinitionImpl;
import com.rmompati.lang.intermediate.symtableimpl.Predefined;
import com.rmompati.lang.message.Message;
import com.rmompati.lang.message.MessageType;
import com.rmompati.lang.pascal.frontend.error.PascalErrorHandler;
import com.rmompati.lang.pascal.frontend.parsers.BlockParser;

import java.io.IOException;
import java.util.EnumSet;

import static com.rmompati.lang.intermediate.symtableimpl.SymTableKeyImpl.ROUTINE_ICODE;
import static com.rmompati.lang.intermediate.symtableimpl.SymTableKeyImpl.ROUTINE_SYMTAB;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.DOT;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;

/**
 * <h1>PascalParserTD</h1>
 *
 * <p>The top-down Pascal parser.</p>
 */
public class PascalParserTD extends Parser {

  protected static PascalErrorHandler errorHandler = new PascalErrorHandler();

  private SymTableEntry routineId;

  /**
   * Constructor
   *
   * @param scanner the scanner to be used with this parser.
   */
  public PascalParserTD(Scanner scanner) {
    super(scanner);
  }

  /**
   * Constructor for subclasses.
   * @param parent the parent parser.
   */
  public PascalParserTD(PascalParserTD parent) {
    super(parent.getScanner());
  }


  /**
   * Synchronise the parser.
   * @param syncSet the set of token types for synchronizing the parser.
   * @return the token where the parser has synchronized.
   * @throws Exception if an error occurs.
   */
  public Token synchronize(EnumSet<? extends TokenType> syncSet) throws Exception {
    Token token = currentToken();

    // If the current is not in the synchronization set, then it is unexpected and
    // the parser must recover.
    if (!syncSet.contains(token.getType())) {
      // Flag the unexpected token.
      errorHandler.flag(token, UNEXPECTED_TOKEN, this);
      // Recover by skipping tokens not in the syncSet.
      do {
        token = nextToken();
      } while (!(token instanceof EofToken) && !syncSet.contains(token.getType()));
    }
    return token;
  }

  /**
   * Parse a source program and generate the intermediate code and the symbol table.
   * To be implemented by a language-specific parser subclass.
   *
   * @throws Exception if an error occurs.
   */
  @Override
  public void parse() throws Exception {
    long startTime = System.currentTimeMillis();
    ICode iCode = ICodeFactory.createICode();
    Predefined.initialize(symTabStack);

    // Create a dummy program identifier symbol table entry.
    routineId = symTabStack.enterLocal("PascalMain".toLowerCase());
    routineId.setDefinition(DefinitionImpl.PROGRAM);
    symTabStack.setProgramId(routineId);

    routineId.setAttribute(ROUTINE_SYMTAB, symTabStack.push());
    routineId.setAttribute(ROUTINE_ICODE, iCode);

    BlockParser blockParser = new BlockParser(this);

    try {
      Token token = nextToken();

      // Parse block
      ICodeNode rootNode = blockParser.parse(token, routineId);
      iCode.setRoot(rootNode);
      symTabStack.pop();

      // Look for the final period.
      token = currentToken();
      if (token.getType() != DOT) {
        errorHandler.flag(token, MISSING_PERIOD, this);
      }

      token = currentToken();

      // Set the parse root node.
      if (rootNode != null) {
        iCode.setRoot(rootNode);
      }


      // Send the parser summary message.
      float elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;
      sendMessage(new Message(
          MessageType.PARSER_SUMMARY, new Number[]{token.getLineNum(), getErrorCount(), elapsedTime}
      ));
    } catch (IOException exc) {
      errorHandler.abortTranslation(IO_ERROR, this);
    }
  }

  /**
   * Return the number of syntax errors found by the parser.
   * To be implemented by a language-specific parser subclass.
   *
   * @return the error count.
   */
  @Override
  public int getErrorCount() {
    return errorHandler.getErrorCount();
  }
}
