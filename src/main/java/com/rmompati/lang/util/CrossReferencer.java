package com.rmompati.lang.util;

import com.rmompati.lang.intermediate.SymTabStack;
import com.rmompati.lang.intermediate.SymTable;
import com.rmompati.lang.intermediate.SymTableEntry;

import java.util.ArrayList;

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

  /**
   * Prints the cross-reference table.
   * @param symTabStack the symbol table stack.
   */
  public void print(SymTabStack symTabStack) {
    System.out.println("\n===== CROSS-REFERENCE TABLE =====");
    printColumnHeadings();
    printSymTable(symTabStack.getLocalSymTab());
  }

  private void printColumnHeadings() {
    System.out.println();
    System.out.println(String.format(NAME_FORMAT, "Identifier") + NUMBERS_LABEL);
    System.out.println(String.format(NAME_FORMAT, "----------") + NUMBERS_UNDERLINE);
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
