package com.rmompati.lang.intermediate;

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
}
