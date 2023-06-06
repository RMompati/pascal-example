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
}
