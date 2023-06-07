package com.rmompati.lang.intermediate;

import java.util.ArrayList;

/**
 * <h1>SymTableEntry</h1>
 *
 * <p>The interface for a symbol table entry.</p>
 */
public interface SymTableEntry {
  /**
   * Gets the name of the entry.
   * @return the name of the entry.
   */
  public String getName();

  /**
   * Gets the symbol table.
   * @return the symbol table that contains this entry.
   */
  public SymTable getSymTable();

  /**
   * Appends a source line number to the entry.
   * @param lineNumber the line number to append.
   */
  public void appendLineNumber(int lineNumber);

  /**
   * Gets the list of all the line numbers.
   * @return the list of all the line numbers.
   */
  public ArrayList<Integer> getLineNumbers();

  /**
   * Sets an attribute of the entry.
   * @param key the attribute key.
   * @param value the attribute value.
   */
  public void setAttribute(SymTableKey key, Object value);

  /**
   * Gets the value of an attribute of the entry.
   * @param key the attribute key.
   * @return the attribute value.
   */
  public Object getAttribute(SymTableKey key);
}
