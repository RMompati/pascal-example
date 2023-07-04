package com.rmompati.lang.intermediate;

/**
 * <h1>SymTabStack</h1>
 *
 * <p>The interface for the symbol table stack.</p>*/
public interface SymTabStack {

    /**
     * Gets the current nesting level.
     * @return the current nesting level.
     */
    public int getCurrentNestingLevel();

    /**
     * Return the local symbol table which is at the top of the stack.
     * @return the local symbol table.
     */
    public SymTable getLocalSymTab();

    /**
     * Creates and enters a new entry into the local symbol table.
     * @param name the of the entry.
     * @return the new entry.
     */
    public SymTableEntry enterLocal(String name);

  /**
   * Looks up an existing symbol table entry in the local symbol table.
   * @param name the name of the entry.
   * @return the entry, or null if it does not exist.
   */
  public SymTableEntry lookupLocal(String name);

  /**
   * Looks up an existing symbol table entry throughout the stack.
   * @param name the name of the entry.
   * @return the entry, or null if it does not exist.
   */
  public SymTableEntry lookup(String name);

  /**
   * Sets the main program's identifier.
   * @param entry the symbol table entry for the main program identifier.
   */
  public void setProgramId(SymTableEntry entry);

  /**
   * Gets the main program's identifier.
   * @return the symbol table entry for the main program identifier.
   */
  public SymTableEntry getProgramId();

  /**
   * Pushes a new symbol table onto the stack.
   * @return the pushed symbol table.
   */
  public SymTable push();

  /**
   * Pushes a new symbol table onto the stack.
   * @return the pushed symbol table.
   */
  public SymTable push(SymTable symTable);

  /**
   * Pops a symbol table off the stack.
   * @return the popped symbol table.
   */
  public SymTable pop();
}
