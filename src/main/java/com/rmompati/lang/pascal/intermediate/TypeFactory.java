package com.rmompati.lang.pascal.intermediate;

import com.rmompati.lang.intermediate.TypeForm;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.intermediate.typeimpl.TypeSpecImpl;

/**
 * <h1>TypeFactory</h1>
 *
 * <p>A factory for creating type specifications.</p>
 */
public class TypeFactory {

  /**
   * Creates a type specification of a given form.
   * @param form the type form.
   * @return the type specification.
   */
  public static TypeSpec createType(TypeForm form) {
    return new TypeSpecImpl(form);
  }

  /**
   * Creates a string type specification.
   * @param value the string value.
   * @return the type specification.
   */
  public static TypeSpec createStringType(String value) {
    return new TypeSpecImpl(value);
  }
}
