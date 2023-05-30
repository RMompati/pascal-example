package com.rmompati.lang.frontend;

import com.rmompati.lang.pascal.frontend.PascalParserTD;
import com.rmompati.lang.pascal.frontend.PascalScanner;

/**
 * <h1>FrontendFactory</h1>
 *
 * <p>A factory class that creates parsers for specific languages.</p>
 */
public class FrontendFactory {

  /**
   * Creates a parser.
   * @param language the name of the source language (e.g., "Pascal")
   * @param type the type of parser (e.g., "top-down")
   * @param source the source object.
   * @throws Exception if an exception occurs.
   */
  public static Parser createParser(String language, String type, Source source) throws Exception {
    if (language.equalsIgnoreCase("Pascal") && type.equalsIgnoreCase("top-down")) {
      Scanner scanner = new PascalScanner(source);
      return new PascalParserTD(scanner);
    } else if (!language.equalsIgnoreCase("Pascal")) {
      throw new Exception("Parser factory: Invalid language \"" + language + "\"");
    } else {
      throw new Exception("Parser factory: Invalid type \"" + type + "\"");
    }
  }
}
