package com.rmompati.lang.intermediate.symtableimpl;

import com.rmompati.lang.intermediate.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <h1>SymTableEntryImpl</h1>
 *
 * <p>An implementation of a symbol table entry.</p>
 */
public class SymTableEntryImpl extends HashMap<SymTableKey, Object> implements SymTableEntry {
  private String name;
  private SymTable symTable;
  private ArrayList<Integer> lineNumbers;
  private Definition definition;
  private TypeSpec typeSpec;

  /**
   * Constructor
   *
   * @param name the name of the entry.
   * @param symTable the symbol table that contains the entry.
   */
  public SymTableEntryImpl(String name, SymTable symTable) {
    this.name = name;
    this.symTable = symTable;
    this.lineNumbers = new ArrayList<>();
  }

  /**
   * Gets the name of the entry.
   *
   * @return the name of the entry.
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Gets the symbol table.
   *
   * @return the symbol table that contains this entry.
   */
  @Override
  public SymTable getSymTable() {
    return symTable;
  }

  /**
   * Appends a source line number to the entry.
   *
   * @param lineNumber the line number to append.
   */
  @Override
  public void appendLineNumber(int lineNumber) {
    lineNumbers.add(lineNumber);
  }

  /**
   * Gets the list of all the line numbers.
   *
   * @return the list of all the line numbers.
   */
  @Override
  public ArrayList<Integer> getLineNumbers() {
    return lineNumbers;
  }

  /**
   * Sets an attribute of the entry.
   *
   * @param key   the attribute key.
   * @param value the attribute value.
   */
  @Override
  public void setAttribute(SymTableKey key, Object value) {
    put(key, value);
  }

  /**
   * Gets the value of an attribute of the entry.
   *
   * @param key the attribute key.
   * @return the attribute value.
   */
  @Override
  public Object getAttribute(SymTableKey key) {
    return get(key);
  }

  /**
   * Sets the definition.
   *
   * @param definition the definition set.
   */
  @Override
  public void setDefinition(Definition definition) {
    this.definition = definition;
  }

  /**
   * Gets the definition.
   *
   * @return the definition.
   */
  @Override
  public Definition getDefinition() {
    return definition;
  }

  /**
   * Sets the type specification.
   *
   * @param typeSpec the type specification set.
   */
  @Override
  public void setTypeSpec(TypeSpec typeSpec) {
    this.typeSpec = typeSpec;
  }

  /**
   * Gets the type specification.
   *
   * @return the type specification.
   */
  @Override
  public TypeSpec getTypeSpec() {
    return typeSpec;
  }
}
