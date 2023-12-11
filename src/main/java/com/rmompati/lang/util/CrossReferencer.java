package com.rmompati.lang.util;

import com.rmompati.lang.intermediate.*;
import com.rmompati.lang.intermediate.symtableimpl.DefinitionImpl;
import com.rmompati.lang.intermediate.typeimpl.TypeFormImpl;

import java.util.ArrayList;

import static com.rmompati.lang.intermediate.symtableimpl.SymTableKeyImpl.*;
import static com.rmompati.lang.intermediate.typeimpl.TypeKeyImpl.*;

/**
 * <h1>CrossReferencer</h1>
 *
 * <p>Generates a cross-reference listing.</p>
 */
public class CrossReferencer {

  private static final int NAME_WIDTH = 16;

  private static final String NAME_FORMAT = "%-" + NAME_WIDTH + "s";
  private static final String NUMBERS_LABEL = " Line numbers  ";
  private static final String NUMBERS_UNDERLINE = " ------------  ";
  private static final String NUMBER_FORMAT = " %03d";

  private static final int LABEL_WIDTH = NUMBERS_LABEL.length();
  private static final int INDENT_WIDTH = NAME_WIDTH + LABEL_WIDTH;

  private static final StringBuilder INDENT = new StringBuilder(INDENT_WIDTH);
  static {
    INDENT.append(" ".repeat(INDENT_WIDTH));
  }

  public static final String ENUM_CONST_FORMAT = "%" + NAME_WIDTH + "s = %s";

  /**
   * Prints the cross-reference table.
   * @param symTabStack the symbol table stack.
   */
  public void print(SymTabStack symTabStack) {
    System.out.println("\n===== CROSS-REFERENCE TABLE =====");

    SymTableEntry programId = symTabStack.getProgramId();
    printRoutine(programId);
    printSymTable(symTabStack.getLocalSymTab());
  }

  public void printRoutine(SymTableEntry routineId) {
    Definition definition = routineId.getDefinition();
    System.out.printf("\n\n*** %s %s ***", definition.toString(), routineId.getName());
    printColumnHeadings();

    SymTable symTable = (SymTable) routineId.getAttribute(ROUTINE_SYMTAB);
    ArrayList<TypeSpec> newRecordTypes = new ArrayList<>();
    printSymTable(symTable, newRecordTypes);

    if (!newRecordTypes.isEmpty()) {
      printRecords(newRecordTypes);
    }

    ArrayList<SymTableEntry> routineIds = (ArrayList<SymTableEntry>) routineId.getAttribute(ROUTINE_ROUTINES);
    if (routineIds != null) {
      for (SymTableEntry rtnId : routineIds) {
        printRoutine(rtnId);
      }
    }
  }

  private void printColumnHeadings() {
    System.out.println();
    System.out.println(String.format(NAME_FORMAT, "Identifier") + NUMBERS_LABEL);
    System.out.println(String.format(NAME_FORMAT, "----------") + NUMBERS_UNDERLINE);
  }

  /**
   * Prints the entries in a symbol table.
   *
   * @param symTable the symbol table.
   * @param recordTypes the list to fill with RECORD type specifications.
   */
  private void printSymTable(SymTable symTable, ArrayList<TypeSpec> recordTypes) {
    // Loop over the sorted list of symbol table entries.
    ArrayList<SymTableEntry> sorted = symTable.sortedEntries();
    for (SymTableEntry entry : sorted) {
      ArrayList<Integer> lineNumbers = entry.getLineNumbers();

      // For each entry, print the identifier followed by the line numbers.
      System.out.printf(NAME_FORMAT, entry.getName());
      if (lineNumbers != null) {
        for (Integer lineNumber : lineNumbers) {
          System.out.printf(NUMBER_FORMAT, lineNumber);
        }
      }
      System.out.println();
      printEntry(entry, recordTypes);
    }
  }

  /**
   * Prints a symbol table entry
   * @param entry the symbol table entry.
   * @param recordTypes the list to fill with RECORD type specifications.
   */
  private void printEntry(SymTableEntry entry, ArrayList<TypeSpec> recordTypes) {
    Definition definition = entry.getDefinition();
    int nestingLevel = entry.getSymTable().getNestingLevel();
    System.out.println(INDENT + "Defined as: " + definition.getText());
    System.out.println(INDENT + "Scope nesting level: " + nestingLevel);

    TypeSpec type = entry.getTypeSpec();
    printType(type);

    switch ((DefinitionImpl) definition) {
      case CONSTANT: {
        Object value = entry.getAttribute(CONSTANT_VALUE);
        System.out.println(INDENT + "Value = " + toString(value));

        if (type.getIdentifier() == null) {
          printTypeDetail(type, recordTypes);
        }
        break;
      }
      case ENUMERATION_CONSTANT: {
        Object value = entry.getAttribute(CONSTANT_VALUE);
        System.out.println(INDENT + "Value = " + toString(value));

        break;
      }
      case TYPE: {
        if (entry == type.getIdentifier()) {
          printTypeDetail(type, recordTypes);
        }

        break;
      }
      case VARIABLE: {
        if (type.getIdentifier() == null) {
          printTypeDetail(type, recordTypes);
        }
      }
    }
  }

  /**
   * Converts the value to a string.
   * @param value the value.
   * @return the string.
   */
  private String toString(Object value) {
    return value instanceof String ? "<" + value + ">"
        : value.toString();
  }

  /**
   * Prints type specification.
   * @param type the type specification.
   */
  public void printType(TypeSpec type) {
    if (type != null) {
      TypeForm form = type.getForm();
      SymTableEntry typeId = type.getIdentifier();
      String typeName = typeId != null ? typeId.getName() : "<unnamed>";
      System.out.println(INDENT + "Type form = " + form + ", Type id = " + typeName);
    }
  }

  private void printTypeDetail(TypeSpec type, ArrayList<TypeSpec> recordTypes) {
    TypeForm form = type.getForm();

    switch ((TypeFormImpl) form) {
      case ENUMERATION: {
        ArrayList<SymTableEntry> constantIds = (ArrayList<SymTableEntry>) type.getAttribute(ENUMERATION_CONSTANTS);

        System.out.println(INDENT + "--- Enumeration constants ---");
        for (SymTableEntry constantId : constantIds) {
          String name = constantId.getName();
          Object value = constantId.getAttribute(CONSTANT_VALUE);

          System.out.println(INDENT + String.format(ENUM_CONST_FORMAT, name, value));
        }
        break;
      }
      case SUBRANGE: {
        Object minValue = type.getAttribute(SUBRANGE_MIN_VALUE);
        Object maxValue = type.getAttribute(SUBRANGE_MAX_VALUE);
        TypeSpec baseTypeSpec = (TypeSpec) type.getAttribute(SUBRANGE_BASE_TYPE);

        System.out.println(INDENT + "--- Base Type ---");
        printType(baseTypeSpec);

        if (baseTypeSpec.getIdentifier() == null) {
          printTypeDetail(baseTypeSpec, recordTypes);
        }

        System.out.print(INDENT + "Range = ");
        System.out.println(toString(minValue) + ".." + toString(maxValue));
        break;
      }
      case ARRAY: {
        TypeSpec indexType = (TypeSpec) type.getAttribute(ARRAY_INDEX_TYPE);
        TypeSpec elementType = (TypeSpec) type.getAttribute(ARRAY_ELEMENT_TYPE);
        int count = (Integer) type.getAttribute(ARRAY_ELEMENT_COUNT);

        System.out.println(INDENT + "--- INDEX TYPE ---");
        printType(indexType);
        if (indexType.getIdentifier() ==  null) {

          printTypeDetail(indexType, recordTypes);
        }

        System.out.println(INDENT + "--- ELEMENT TYPE ---");
        printType(elementType);
        System.out.println(INDENT.toString() + count + " elements");

         if (elementType.getIdentifier() == null) {
           printTypeDetail(elementType, recordTypes);
         }
         break;
      }
      case RECORD: {
        recordTypes.add(type);
        break;
      }
    }
  }

  /**
   * Prints cross-reference tables for records defined in the routine.
   * @param recordTypes the list to fill with RECORD type specifications.
   */
  private void printRecords(ArrayList<TypeSpec> recordTypes) {
    for (TypeSpec recordType : recordTypes) {
      SymTableEntry recordId = recordType.getIdentifier();
      String name = recordId != null ? recordId.getName() : "<unnamed>";

      System.out.println("\n--- RECORD " + name + " ---");
      printColumnHeadings();

      SymTable symTable = (SymTable) recordType.getAttribute(RECORD_SYMTAB);
      ArrayList<TypeSpec> newRecordTypes = new ArrayList<>();
      printSymTable(symTable, newRecordTypes);

      if (!newRecordTypes.isEmpty()) {
        printRecords(newRecordTypes);
      }
    }
  }

  private void printSymTable(SymTable symTable) {
    // Loop over the sorted list of symbol table entries.
    ArrayList<SymTableEntry> sorted = symTable.sortedEntries();
    for (SymTableEntry entry : sorted) {
      ArrayList<Integer> lineNumbers = entry.getLineNumbers();

      // For each entry, print the identifier followed by the line numbers.
      System.out.printf(NAME_FORMAT, entry.getName());
      if (lineNumbers != null) {
        for (Integer lineNumber : lineNumbers) {
          System.out.printf(NUMBER_FORMAT, lineNumber);
        }
      }
      System.out.println();
    }
  }
}
