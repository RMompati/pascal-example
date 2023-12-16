package com.rmompati.lang.pascal.intermediate.symtableimpl;

import com.rmompati.lang.intermediate.SymTabStack;
import com.rmompati.lang.intermediate.SymTableEntry;
import com.rmompati.lang.pascal.intermediate.TypeFactory;
import com.rmompati.lang.intermediate.TypeSpec;

import java.util.ArrayList;

import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeFormImpl.ENUMERATION;
import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeFormImpl.SCALAR;
import static com.rmompati.lang.pascal.intermediate.typeimpl.TypeKeyImpl.ENUMERATION_CONSTANTS;

/**
 * <h1>Predefined</h1>
 *
 * <p>Enter the predefined Pascal types, identifiers, and constants into the symbol table.</p>
 */
public class Predefined {
  // Predefined types
  public static TypeSpec integerType;
  public static TypeSpec realType;
  public static TypeSpec booleanType;
  public static TypeSpec charType;
  public static TypeSpec undefinedType;

  // Predefined identifiers.
  public static SymTableEntry integerId;
  public static SymTableEntry realId;
  public static SymTableEntry booleanId;
  public static SymTableEntry charId;
  public static SymTableEntry falseId;
  public static SymTableEntry trueId;

  /**
   * Initializes a symbol table stack with predefined identifiers.
   * @param symTabStack the symbol table stack to initialize.
   */
  public static void initialize(SymTabStack symTabStack) {
    initializeTypes(symTabStack);
    initializeConstants(symTabStack);
  }

  /**
   * Initializes a symbol table stack with predefined identifiers.
   * @param symTabStack the symbol table stack to initialize.
   */
  private static void initializeTypes(SymTabStack symTabStack) {
    // Type integer
    integerId = symTabStack.enterLocal("integer");
    integerType = TypeFactory.createType(SCALAR);
    integerType.setIdentifier(integerId);
    integerId.setDefinition(DefinitionImpl.TYPE);
    integerId.setTypeSpec(integerType);

    // Type real
    realId = symTabStack.enterLocal("real");
    realType = TypeFactory.createType(SCALAR);
    realType.setIdentifier(realId);
    realId.setDefinition(DefinitionImpl.TYPE);
    realId.setTypeSpec(realType);

    // Type boolean.
    booleanId = symTabStack.enterLocal("boolean");
    booleanType = TypeFactory.createType(ENUMERATION);
    booleanType.setIdentifier(booleanId);
    booleanId.setDefinition(DefinitionImpl.TYPE);
    booleanId.setTypeSpec(booleanType);

    // Type char
    charId = symTabStack.enterLocal("char");
    charType = TypeFactory.createType(SCALAR);
    charType.setIdentifier(charId);
    charId.setDefinition(DefinitionImpl.TYPE);
    charId.setTypeSpec(charType);

    // Undefined type
    undefinedType = TypeFactory.createType(SCALAR);
  }

  /**
   * Initializes the predefined constants.
   * @param symTabStack the symbol table stack to initialize.
   */
  private static void initializeConstants(SymTabStack symTabStack) {
    // Boolean enumeration constant false.
    falseId = symTabStack.enterLocal("false");
    falseId.setDefinition(DefinitionImpl.ENUMERATION_CONSTANT);
    falseId.setTypeSpec(booleanType);
    falseId.setAttribute(SymTableKeyImpl.CONSTANT_VALUE, 0);

    // Boolean enumeration constant true.
    trueId = symTabStack.enterLocal("true");
    trueId.setDefinition(DefinitionImpl.ENUMERATION_CONSTANT);
    trueId.setTypeSpec(booleanType);
    trueId.setAttribute(SymTableKeyImpl.CONSTANT_VALUE, 1);

    ArrayList<SymTableEntry> constants = new ArrayList<>();
    constants.add(falseId);
    constants.add(trueId);
    booleanType.setAttribute(ENUMERATION_CONSTANTS, constants);
  }

}
