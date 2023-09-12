package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalTokenType;

import java.util.EnumSet;

public class TypeSpecificationParser extends PascalParserTD {

  // Synchronization set for starting a type specification.
  static final EnumSet<PascalTokenType> TYPE_START_SET = SimpleTypeParser.SIMPLE_START_SET.clone();
  static {
    TYPE_START_SET.add(PascalTokenType.ARRAY);
    TYPE_START_SET.add(PascalTokenType.RECORD);
    TYPE_START_SET.add(PascalTokenType.SEMICOLON);
  }
  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public TypeSpecificationParser(PascalParserTD parent) {
    super(parent);
  }

  /**
   * Parses a Pascal type specification.
   * @param token the current token.
   * @return the type specification.
   * @throws Exception if an error occurs.
   */
  public TypeSpec parse(Token token) throws Exception {
    // Synchronize at the start of a type specification.
    token = synchronize(TYPE_START_SET);
    switch ((PascalTokenType) token.getType()) {
      case ARRAY: {
        ArrayTypeParser arrayTypeParser = new ArrayTypeParser(this);
        return arrayTypeParser.parse(token);
      }
      case RECORD: {
        RecordTypeParser recordTypeParser = new RecordTypeParser(this);
        return recordTypeParser.parse(token);
      }
      default: {
        SimpleTypeParser simpleTypeParser = new SimpleTypeParser(this);
        return simpleTypeParser.parse(token);
      }
    }
  }
}
