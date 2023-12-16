package com.rmompati.lang.pascal.intermediate.typeimpl;

import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.intermediate.TypeForm;
import com.rmompati.lang.intermediate.TypeKey;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.intermediate.symtableimpl.Predefined;

import java.util.HashMap;

import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeFormImpl.ARRAY;
import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeFormImpl.SUBRANGE;

/**
 * <h1>TypeSpecImpl</h1>
 *
 * <p>A Pascal type specification.</p>
 */
public class TypeSpecImpl extends HashMap<TypeKey, Object> implements TypeSpec {

  private TypeForm form;
  private SymTableEntry identifier;

  /**
   * Constructor.
   * @param form the type form.
   */
  public TypeSpecImpl(TypeForm form) {
    this.form = form;
    this.identifier = null;
  }

  /**
   * Constructor.
   * @param value a string value.
   */
  public TypeSpecImpl(String value) {
    this.form = ARRAY;

    TypeSpec indexType = new TypeSpecImpl(SUBRANGE);
    indexType.setAttribute(TypeKeyImpl.SUBRANGE_BASE_TYPE, Predefined.integerType);
    indexType.setAttribute(TypeKeyImpl.SUBRANGE_MIN_VALUE, 1);
    indexType.setAttribute(TypeKeyImpl.SUBRANGE_MAX_VALUE, value.length());

    setAttribute(TypeKeyImpl.ARRAY_INDEX_TYPE, indexType);
    setAttribute(TypeKeyImpl.ARRAY_ELEMENT_TYPE, Predefined.charType);
    setAttribute(TypeKeyImpl.ARRAY_ELEMENT_COUNT, value.length());
  }

  /**
   * Gets the type form.
   *
   * @return the type form.
   */
  @Override
  public TypeForm getForm() {
    return form;
  }

  /**
   * Sets the type identifier in a form of symbol table entry.
   *
   * @param identifier the type identifier.
   */
  @Override
  public void setIdentifier(SymTableEntry identifier) {
    this.identifier = identifier;
  }

  /**
   * Gets the type identifier in a form of symbol table entry.
   *
   * @return the type identifier.
   */
  @Override
  public SymTableEntry getIdentifier() {
    return identifier;
  }

  /**
   * Sets an attribute of the specification.
   *
   * @param key   the attribute key.
   * @param value the attribute value.
   */
  @Override
  public void setAttribute(TypeKey key, Object value) {
    put(key, value);
  }

  /**
   * Gets an attribute of the specification.
   *
   * @param key the attribute key.
   * @return the attribute value.
   */
  @Override
  public Object getAttribute(TypeKey key) {
    return get(key);
  }

  /**
   * Checks if the specification is a Pascal String.
   *
   * @return true if the specification is a Pascal string, false otherwise.
   */
  @Override
  public boolean isPascalString() {
    if (form == ARRAY) {
      TypeSpec elmType = (TypeSpec) getAttribute(TypeKeyImpl.ARRAY_ELEMENT_TYPE);
      TypeSpec indexType = (TypeSpec) getAttribute(TypeKeyImpl.ARRAY_INDEX_TYPE);

      return (elmType.baseType() == Predefined.charType) && (indexType.baseType() == Predefined.integerType);
    } else {
      return false;
    }
  }

  /**
   * Returns the base type of this type.
   *
   * @return base type of this type.
   */
  @Override
  public TypeSpec baseType() {
    return (((TypeFormImpl) form) == SUBRANGE) ? (TypeSpec) getAttribute(TypeKeyImpl.SUBRANGE_BASE_TYPE) : this;
  }
}
