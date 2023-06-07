package com.rmompati.lang.intermediate.symtableimpl;

import com.rmompati.lang.intermediate.SymTabStack;
import com.rmompati.lang.intermediate.SymTable;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.intermediate.SymTableFactory;

import java.util.ArrayList;

/**
 * <h1>SymTableStackImpl</h1>
 *
 * <p>The default implementation of the symbol table stack.</p>
 */
public class SymTableStackImpl extends ArrayList<SymTable> implements SymTabStack {

  private int currentNestingLevel;

  public SymTableStackImpl() {
    this.currentNestingLevel = 0;
    add(SymTableFactory.createSymTable(currentNestingLevel));
  }

  /**
   * Gets the current nesting level.
   *
   * @return the current nesting level.
   */
  @Override
  public int getCurrentNestingLevel() {
    return currentNestingLevel;
  }

  /**
   * Return the local symbol table which is at the top of the stack.
   *
   * @return the local symbol table.
   */
  @Override
  public SymTable getLocalSymTab() {
    return get(currentNestingLevel);
  }

  /**
   * Creates and enters a new entry into the local symbol table.
   *
   * @param name the of the entry.
   * @return the new entry.
   */
  @Override
  public SymTableEntry enterLocal(String name) {
    return get(currentNestingLevel).enter(name);
  }

  /**
   * Looks up an existing symbol table entry in the local symbol table.
   *
   * @param name the name of the entry.
   * @return the entry, or null if it does not exist.
   */
  @Override
  public SymTableEntry lookupLocal(String name) {
    return get(currentNestingLevel).lookup(name);
  }

  /**
   * Looks up an existing symbol table entry throughout the stack.
   *
   * @param name the name of the entry.
   * @return the entry, or null if it does not exist.
   */
  @Override
  public SymTableEntry lookup(String name) {
    return lookupLocal(name);
  }
}
