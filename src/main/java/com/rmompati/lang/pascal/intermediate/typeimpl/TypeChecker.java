package com.rmompati.lang.pascal.intermediate.typeimpl;

import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.intermediate.symtableimpl.Predefined;

/**
 * <h1>TypeChecker</h1>
 * <p>The pascal type checker.</p>
 */
public class TypeChecker {

  /**
   * Checks if a type specification is an integer.
   * @param type the type specification to check.
   * @return true if integer, false otherwise.
   */
  public static boolean isInteger(TypeSpec type) {
    return (type != null) && (type.baseType() == Predefined.integerType);
  }

  /**
   * Checks if both type specification are integer.
   * @param type1 the first type specification to check.
   * @param type2 the second type specification to check.
   * @return true if both are integer, false otherwise.
   */
  public static boolean areBothInteger(TypeSpec type1, TypeSpec type2) {
    return isInteger(type1) && isInteger(type2);
  }

  /**
   * Checks if a type specification is real.
   * @param type the type specification to check.
   * @return true if is real, false otherwise.
   */
  public static boolean isReal(TypeSpec type) {
    return (type != null) && (type.baseType() == Predefined.realType);
  }

  /**
   * Checks if a type specification is integer or real.
   * @param type the type specification to check.
   * @return true if the type specification is integer or real, false otherwise.
   */
  public static boolean isIntegerOrReal(TypeSpec type) {
    return isInteger(type) || isReal(type);
  }



  /**
   * Checks if at leas one of two type specifications is real.
   * @param type1 the first type specification to check.
   * @param type2 the second type specification to check.
   * @return true if the type specification is integer or real, false otherwise.
   */
  public static boolean isAtLeastOneReal(TypeSpec type1, TypeSpec type2) {
    return isReal(type1) && isReal(type2) ||
        isReal(type1) && isInteger(type2) ||
        isInteger(type1) && isReal(type2);
  }

  /**
   * Checks if a type specification is boolean.
   * @param type the type specification to check.
   * @return true if boolean, false otherwise.
   */
  public static boolean isBoolean(TypeSpec type) {
    return (type != null) && (type.baseType() == Predefined.booleanType);
  }

  /**
   * Checks if both type specification are boolean.
   * @param type1 the first type specification to check.
   * @param type2 the second type specification to check.
   * @return true if both are boolean, false otherwise.
   */
  public static boolean areBothBoolean(TypeSpec type1, TypeSpec type2) {
    return isBoolean(type1) && isBoolean(type2);
  }

  /**
   * Checks if a type specification is char.
   * @param type the type specification to check.
   * @return true if boolean, false otherwise.
   */
  public static boolean isChar(TypeSpec type) {
    return (type != null) && (type.baseType() == Predefined.charType);
  }
}
