package com.rmompati.lang.util;

import com.rmompati.lang.intermediate.*;
import com.rmompati.lang.intermediate.icodeimpl.ICodeNodeImpl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static com.rmompati.lang.intermediate.symtableimpl.SymTableKeyImpl.ROUTINE_ICODE;
import static com.rmompati.lang.intermediate.symtableimpl.SymTableKeyImpl.ROUTINE_ROUTINES;

/**
 * <h1>ParseTreePrinter</h1>
 *
 * <p>Prints a parse tree.</p>
 */
public class ParseTreePrinter {

  private static final int INDENT_WIDTH = 4;
  private static final int LINE_WIDTH = 80;

  private PrintStream ps;
  private int length;
  private String indent;
  private String indentation;
  private StringBuilder line;

  public ParseTreePrinter(PrintStream ps) {
    this.ps = ps;
    this.length = 0;
    this.indentation = "";
    this.line = new StringBuilder();
    this.indent = " ".repeat(INDENT_WIDTH);
  }

  public void print(ICode iCode) {
    ps.println("\n===== INTERMEDIATE CODE =====\n");
    printNode((ICodeNodeImpl) iCode.getRoot());
    printLine();
  }

  /**
   * Prints rhe intermediate code as a parse tree.
   * @param symTabStack the symbol table stack.
   */
  public void print(SymTabStack symTabStack) {
    ps.println("\n===== INTERMEDIATE CODE =====\n");
    SymTableEntry programId = symTabStack.getProgramId();
    printRoutine(programId);

  }

  /**
   * Prints the parse tree of a routine.
   * @@param routineId the routine identifier's symbol table entry.
   */
  public void printRoutine(SymTableEntry routineId) {
    Definition definition = routineId.getDefinition();
    System.out.printf("\n*** %s %s ***\n\n", definition.toString(), routineId.getName());

    // Print the intermediate code in the routine's symbol table entry.
    ICode iCode = (ICode) routineId.getAttribute(ROUTINE_ICODE);
    if (iCode.getRoot() != null) {
      printNode((ICodeNodeImpl) iCode.getRoot());
    }

    // Print any procedures and functions defined in the routine.
    ArrayList<SymTableEntry> routineIds = (ArrayList<SymTableEntry>) routineId.getAttribute(ROUTINE_ROUTINES);
    if (routineIds != null) {
      routineIds.forEach(this::printRoutine);
    }
  }

  /**
   * Prints a parse tree node.
   * @param node the parse tree node.
   */
  private void printNode(ICodeNodeImpl node) {
    append(indentation); append("<" + node.toString());

    printAttributes(node);
    printTypeSpec(node);

    ArrayList<ICodeNode> childNodes = node.getChildren();

    // Print the node's children followed by the closing tag,
    if ((childNodes != null) && (childNodes.size() > 0)) {
      append(">");
      printLine();

      printChildNodes(childNodes);
      append(indentation); append("</" + node + ">");
    } else {
      append(" "); append("/>");
    }

    printLine();
  }

  /**
   * Prints a parse tree's attributes.
   * @param node the parse tree node.
   */
  private void printAttributes(ICodeNodeImpl node) {
    String saveIndentation = indentation;
    indentation += indent;

    Set<Map.Entry<ICodeKey, Object>> attributes = node.entrySet();
    for (Map.Entry<ICodeKey, Object> attribute : attributes) {
      printAttribute(attribute.getKey().toString(), attribute.getValue());
    }
    indentation = saveIndentation;
  }

  /**
   * Prints a node attribute as key="value"
   * @param key the key string
   * @param value the value.
   */
  private void printAttribute(String key, Object value) {
    // If the value is a symbol table entry, use the identifier's name, else just use the value string.
    boolean isSymTableEntry = value instanceof SymTableEntry;
    String valueString = isSymTableEntry ? ((SymTableEntry) value).getName() : value.toString();

    String text = key.toLowerCase() + "=\"" + valueString + "\"";
    append(" "); append(text);

    // Include an identifier's nest level
    if (isSymTableEntry) {
      int level = ((SymTableEntry) value).getSymTable().getNestingLevel();
      printAttribute("LEVEL", level);
    }
  }

  private void printChildNodes(ArrayList<ICodeNode> childNodes) {
    String saveIndentation = indentation;
    indentation += indent;

    for (ICodeNode child : childNodes) {
      printNode((ICodeNodeImpl) child);
    }
    indentation = saveIndentation;
  }

  /**
   * Prints a parse tree node's type specification.
   * @param node the parse tree node.
   */
  private void printTypeSpec(ICodeNodeImpl node) {}

  /**
   * Appends text to the output line.
   * @param text the text to append.
   */
  void append(String text) {
    int textLength = text.length();
    boolean lineBreak = false;

    // Wrap lines that are too long.
    if (length + textLength > LINE_WIDTH) {
      printLine();
      line.append(indentation);
      length = indentation.length();
      lineBreak = true;
    }

    // Append text
    if (!(lineBreak && text.equals(" "))) {
      line.append(text);
      length += textLength;
    }
  }

  /**
   * Prints the output line.
   */
  private void printLine() {
    if (this.length > 0) {
      ps.println(line);
      line.setLength(0);
      length = 0;
    }
  }
}
