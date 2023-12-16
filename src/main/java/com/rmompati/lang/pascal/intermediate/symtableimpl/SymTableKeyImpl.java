package com.rmompati.lang.pascal.intermediate.symtableimpl;

import com.rmompati.lang.intermediate.SymTableKey;

/**
 * <h1>SymTableKeyImpl</h1>
 *
 * <p>Attribute keys for a symbol table entry.</p>
 */
public enum SymTableKeyImpl implements SymTableKey {

  // Constant
  CONSTANT_VALUE,

  // Procedure or Function
  ROUTINE_CODE, ROUTINE_SYMTAB, ROUTINE_ICODE,
  ROUTINE_PARAMS, ROUTINE_ROUTINES,

  // Variable or a record field value
  DATA_VALUE;
}
