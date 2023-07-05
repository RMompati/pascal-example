package com.rmompati.lang.intermediate;

/**
 * <h1>TypeSpec</h1>
 *
 * <p>The interface for a type specification.</p>
 */
public interface TypeSpec {

  /**
   * Gets the type form.
   * @return the type form.
   */
  TypeForm getForm();

  /**
   * Sets the type identifier in a form of symbol table entry.
   * @param identifier the type identifier.
   */
  void setIdentifier(SymTableEntry identifier);

  /**
   * Gets the type identifier in a form of symbol table entry.
   * @return the type identifier.
   */
  SymTableEntry getIdentifier();

  /**
   * Sets an attribute of the specification.
   * @param key the attribute key.
   * @param value the attribute value.
   */
  public void setAttribute(TypeKey key,  Object value);

  /**
   * Gets an attribute of the specification.
   * @param key the attribute key.
   * @return the attribute value.
   */
  Object getAttribute(TypeKey key);

  /**
   * Checks if the specification is a Pascal String.
   * @return true if the specification is a Pascal string, false otherwise.
   */
  boolean isPascalString();

  /**
   * Returns the base type of this type.
   * @return base type of this type.
   */
  TypeSpec baseType();
}
