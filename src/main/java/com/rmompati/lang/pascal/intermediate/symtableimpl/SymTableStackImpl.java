package com.rmompati.lang.pascal.intermediate.symtableimpl;

import com.rmompati.lang.intermediate.SymTabStack;
import com.rmompati.lang.intermediate.SymTable;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.pascal.intermediate.SymTableFactory;

import java.util.ArrayList;

/**
 * <h1>SymTableStackImpl</h1>
 *
 * <p>The default implementation of the symbol table stack.</p>
 */
public class SymTableStackImpl extends ArrayList<SymTable> implements SymTabStack {

  private int currentNestingLevel;
  private SymTableEntry programId;

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
    SymTableEntry foundEntry = null;
    // Search the current enclosing scopes.
    for (int i = currentNestingLevel; (i >= 0) && (foundEntry == null); --i) {
      foundEntry = get(i).lookup(name);
    }

    return foundEntry;
  }

  /**
   * Sets the main program's identifier.
   *
   * @param entry the symbol table entry for the main program identifier.
   */
  @Override
  public void setProgramId(SymTableEntry entry) {
    this.programId = entry;
  }

  /**
   * Gets the main program's identifier.
   *
   * @return the symbol table entry for the main program identifier.
   */
  @Override
  public SymTableEntry getProgramId() {
    return programId;
  }

  /**
   * Pushes a new symbol table onto the stack.
   *
   * @return the pushed symbol table.
   */
  @Override
  public SymTable push() {
    SymTable symTable = SymTableFactory.createSymTable(++currentNestingLevel);
    add(symTable);
    return symTable;
  }

  /**
   * Pushes a new symbol table onto the stack.
   *
   * @param symTable the symbol table to push.
   * @return the pushed symbol table.
   */
  @Override
  public SymTable push(SymTable symTable) {
    ++currentNestingLevel;
    add(symTable);
    return symTable;
  }

  /**
   * Pops a symbol table off the stack.
   *
   * @return the popped symbol table.
   */
  @Override
  public SymTable pop() {
    SymTable symTable = get(currentNestingLevel);
    remove(currentNestingLevel--);
    return symTable;
  }
}
