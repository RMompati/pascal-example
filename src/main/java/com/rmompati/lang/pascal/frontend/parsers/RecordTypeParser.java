package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.pascal.intermediate.TypeFactory;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.EnumSet;

import static com.rmompati.lang.pascal.intermediate.symtableimpl.DefinitionImpl.FIELD;
import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeFormImpl.RECORD;
import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeKeyImpl.RECORD_SYMTAB;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.END;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.SEMICOLON;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.MISSING_END;

public class RecordTypeParser extends TypeSpecificationParser {

  /** Synchronization set for the "END" token. */
  private static final EnumSet<PascalTokenType> END_SET =
      DeclarationsParser.VAR_START_SET.clone();
  static {
    END_SET.add(END);
    END_SET.add(SEMICOLON);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public RecordTypeParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses a Pascal type specification.
   *
   * @param token the current token.
   * @return the type specification.
   * @throws Exception if an error occurs.
   */
  @Override
  public TypeSpec parse(Token token) throws Exception {
    TypeSpec recordType = TypeFactory.createType(RECORD);
    token = nextToken(); // Consume the "RECORD".

    // Push a symbol table for the RECORD type specification.
    recordType.setAttribute(RECORD_SYMTAB, symTabStack.push());

    // Parse the field declaration.
    VariableParser variableParser = new VariableParser(this);
    variableParser.setDefinition(FIELD);
    variableParser.parse(token);

    // Pop off the record's symbol table.
    symTabStack.pop();

    token = synchronize(END_SET);

    if (token.getType() == END) {
      token = nextToken();
    } else {
      errorHandler.flag(token, MISSING_END, this);
    }

    return recordType;
  }
}
