package com.rmompati.lang.pascal.intermediate;

import com.rmompati.lang.intermediate.SymTabStack;
import com.rmompati.lang.intermediate.SymTable;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.pascal.intermediate.symtableimpl.SymTableEntryImpl;
import com.rmompati.lang.pascal.intermediate.symtableimpl.SymTableImpl;
import com.rmompati.lang.pascal.intermediate.symtableimpl.SymTableStackImpl;

/**
 * <h1>SymTableFactory</h1>
 *
 * <p>A factory for creating objects that implement the symbol table.</p>
 */
public class SymTableFactory {

  /**
   * Creates and returns a symbol table stack implementation.
   */
  public static SymTabStack createSymTabStack() {
    return new SymTableStackImpl();
  }

  /**
   * Creates and returns a symbol table implementation.
   * @param nestingLevel the nesting level.
   * @return the symbol table implementation.
   */
  public static SymTable createSymTable(int nestingLevel) {
    return new SymTableImpl(nestingLevel);
  }

  /**
   * Creates and returns a symbol table entry implementation.
   * @param name the identifier name.
   * @param symTable the symbol table implementation.
   * @return the symbol table entry implementation.
   */
  public static SymTableEntry createSymTableEntry(String name, SymTable symTable) {
    return new SymTableEntryImpl(name, symTable);
  }
}
