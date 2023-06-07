package com.rmompati.lang.intermediate.symtableimpl;

import com.rmompati.lang.intermediate.SymTable;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.intermediate.SymTableFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public class SymTableImpl extends TreeMap<String, SymTableEntry> implements SymTable {
  private int nestingLevel;

  public SymTableImpl(int nestingLevel) {
    this.nestingLevel = nestingLevel;
  }

  /**
   * Gets the nesting level
   *
   * @return the scope nesting level of this entry.
   */
  @Override
  public int getNestingLevel() {
    return nestingLevel;
  }

  /**
   * Creates and enters a new entry into the symbol table.
   *
   * @param name the name of the entry.
   * @return the new entry.
   */
  @Override
  public SymTableEntry enter(String name) {
    SymTableEntry entry = SymTableFactory.createSymTableEntry(name, this);
    put(name, entry);

    return entry;
  }

  /**
   * Looks up an existing symbol table entry.
   *
   * @param name the name of the entry.
   * @return the entry, or null if it does not exist.
   */
  @Override
  public SymTableEntry lookup(String name) {
    return get(name);
  }

  /**
   * Gets a list of symbol table entries sorted by name.
   *
   * @return a list of symbol table entries sorted by name.
   */
  @Override
  public ArrayList<SymTableEntry> sortedEntries() {
    Collection<SymTableEntry> entries = values();
    Iterator<SymTableEntry> iterator = entries.iterator();
    ArrayList<SymTableEntry> list = new ArrayList<>(size());
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }

    return list;
  }
}
