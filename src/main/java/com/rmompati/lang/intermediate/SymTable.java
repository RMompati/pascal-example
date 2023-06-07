package com.rmompati.lang.intermediate;

import java.util.ArrayList;

/**
 * <h1>SymTable</h1>
 *
 * <p>The framework interface that represents the symbol table.</p>
 */
public interface SymTable {

  /**
   * Gets the nesting level
   * @return the scope nesting level of this entry.
   */
  public int getNestingLevel();

  /**
   * Creates and enters a new entry into the symbol table.
   * @param name the name of the entry.
   * @return the new entry.
   */
  public SymTableEntry enter(String name);

  /**
   * Looks up an existing symbol table entry.
   * @param name the name of the entry.
   * @return the entry, or null if it does not exist.
   */
  public SymTableEntry lookup(String name);

  /**
   * Gets a list of symbol table entries sorted by name.
   * @return a list of symbol table entries sorted by name.
   */
  public ArrayList<SymTableEntry> sortedEntries();
}
