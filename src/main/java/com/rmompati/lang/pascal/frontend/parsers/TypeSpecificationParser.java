package com.rmompati.lang.pascal.frontend.parsers;

import com.rmompati.lang.frontend.Token;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.frontend.PascalParserTD;

public class TypeSpecificationParser extends PascalParserTD {

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public TypeSpecificationParser(PascalParserTD parent) {
    super(parent);
  }

  public TypeSpec parse(Token token) {
    return null;
  }
}
