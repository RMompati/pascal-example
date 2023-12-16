package com.rmompati.lang.pascal.intermediate.symtableimpl;

import com.rmompati.lang.intermediate.Definition;

public enum DefinitionImpl implements Definition {
  CONSTANT, ENUMERATION_CONSTANT("enumeration constant"),
  TYPE, VARIABLE, FIELD("record field"),
  VALUE_PARAM("value parameter"),
  PROGRAM, PROCEDURE, FUNCTION,
  UNDEFINED;

  private final String text;

  DefinitionImpl(String text) {
    this.text = text;
  }

  DefinitionImpl() {
    this.text = this.toString().toLowerCase();
  }

  /**
   * Gets the symbol table entry definition.
   *
   * @return the text of definition.
   */
  @Override
  public String getText() {
    return text;
  }
}
