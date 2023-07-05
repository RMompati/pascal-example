package com.rmompati.lang.intermediate.typeimpl;

import com.rmompati.lang.intermediate.TypeForm;

/**
 * <h1>TypeFormImpl</h1>
 *
 * <p>Type forms for a Pascal type specification.</p>
 */
public enum TypeFormImpl implements TypeForm {
  SCALAR, ENUMERATION, SUBRANGE, ARRAY, RECORD;
}
