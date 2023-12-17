package com.rmompati.lang.pascal.intermediate.typeimpl;

import com.rmompati.lang.intermediate.TypeForm;
import com.rmompati.lang.intermediate.TypeSpec;
import com.rmompati.lang.pascal.intermediate.symtableimpl.Predefined;

import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeFormImpl.ENUMERATION;
import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeFormImpl.SCALAR;

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

  /**
   * Checks if two type specifications are assignment compatible.
   * @param targetType the target type specification.
   * @param valueType the value type specification.
   * @return true if the value can be assigned to the target, false otherwise.
   */
  public static boolean areAssignmentCompatible(TypeSpec targetType, TypeSpec valueType) {
    if ((targetType == null) || (valueType == null)) {
      return false;
    }

    targetType = targetType.baseType();
    valueType = valueType.baseType();

    boolean compatible = false;

    // Identical types.
    if (targetType == valueType) {
      compatible =  true;
    } else if (isReal(targetType) && isInteger(valueType)) {
      compatible = true;
    } else {
      compatible = targetType.isPascalString() && valueType.isPascalString();
    }

    return compatible;
  }

  public static boolean areComparisonCompatible(TypeSpec type1, TypeSpec type2) {
    if ((type1 == null) || (type2 == null)) {
      return false;
    }

    type1 = type1.baseType();
    type2 = type2.baseType();
    TypeForm form = type1.getForm();
    
    boolean compatible = false;
    
    // Two identical scala or enumeration types.
    if ((type1 == type2) && ((form == SCALAR) || (form == ENUMERATION))) {
      compatible = true;
    } else if (isAtLeastOneReal(type1, type2)) {
      compatible = true;
    } else {
      compatible = type1.isPascalString() && type2.isPascalString();
    }

    return compatible;
  }
}
