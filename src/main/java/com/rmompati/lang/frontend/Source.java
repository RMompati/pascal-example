package com.rmompati.lang.frontend;


import com.rmompati.lang.message.*;
import com.rmompati.lang.pascal.frontend.message.*;
import com.rmompati.lang.pascal.message.*;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * <h1>Source</h1>
 *
 * <p>The framework class that represents the source program.</p>
 * */
public class Source implements MessageProducer {
  /** End-of-line character. */
  public static final char EOL = '\n';
  /** end-of-file character. */
  public static final char EOF = (char) 0;

  /** reader for the source program. */
  private BufferedReader reader;
  /** source line. */
  private String line;
  /** current source line number. */
  private int lineNum;
  /** current source line position. */
  private int currentPos;

  private static MessageHandler messageHandler;

  static {
    messageHandler = new MessageHandler();
  }

  /**
   * Constructor.
   *
   * @param reader the reader for the source program.
   * @throws java.io.IOException if an I/O error occurs.
   */
  public Source(BufferedReader reader) throws IOException {
    this.lineNum = 0;
    this.currentPos = -2;
    this.reader = reader;
  }

  /**
   * Return the source character at the current position.
   * @return the source character at the current position.
   * @throws Exception if an error occurred.
   */
  public char currentChar() throws Exception {
    if (currentPos == -2) {
       readLine();
       return nextChar();
    } else if (line == null) {
      return EOF;
    } else if ((currentPos == -1) || (currentPos == line.length())) {
      return EOL;
    } else {
      return line.charAt(currentPos);
    }
  }

  /**
   * Consume the current source character and return the next character.
   *
   * @return next source character.
   * @throws Exception if an error occurred.
   */
  public char nextChar() throws Exception {
    ++currentPos;
    return currentChar();
  }

  /**
   * Return the source character following the current character without consuming the current character.
   * @return the following character.
   * @throws Exception if an error occurred.
   */
  public char peekChar() throws Exception {
    currentChar();

    if (line == null) {
      return EOF;
    }

    int nextPos = currentPos + 1;
    return nextPos < line.length() ? line.charAt(nextPos) : EOL;
  }

  /**
   * Read the next source line.
   * @throws IOException if an I/O error occurred.
   */
  public void readLine() throws IOException {
    line = reader.readLine();
    currentPos = -1;
    if (line == null) {
      ++lineNum;
    }

    if (line != null) {
      sendMessage(new Message(MessageType.SOURCE_LINE, new Object[]{lineNum, line}));
    }
  }

  /**
   * Close the source.
   * @throws Exception if an error occurred.
   */
  public void close() throws Exception {
    if (reader != null) {
      try {
        reader.close();
      } catch (IOException ex) {
        ex.printStackTrace();
        throw ex;
      }
    }
  }

  public BufferedReader getReader() {
    return reader;
  }

  public void setReader(BufferedReader reader) {
    this.reader = reader;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public int getLineNum() {
    return lineNum;
  }

  public void setLineNum(int lineNum) {
    this.lineNum = lineNum;
  }

  public int getCurrentPos() {
    return currentPos;
  }

  public void setCurrentPos(int currentPos) {
    this.currentPos = currentPos;
  }

  /**
   * Adds a lister to the listener list.
   *
   * @param listener the listener to add.
   */
  @Override
  public void addMessageListener(MessageListener listener) {
    messageHandler.addListener(listener);
  }

  /**
   * Remove a listener from the listener list.
   *
   * @param listener the listener to remove.
   */
  @Override
  public void removeMessageListener(MessageListener listener) {
    messageHandler.removeListener(listener);
  }

  /**
   * Notify listeners after setting the message.
   *
   * @param message the message to send.
   */
  @Override
  public void sendMessage(Message message) {
    messageHandler.sendMessage(message);
  }
}
