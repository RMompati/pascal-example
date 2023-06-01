package com.rmompati.lang.frontend;

/**
 * <h1>Token</h1>
 *
 * <p>The framework class that represents a token returned by the scanner.</p>
 * */
public class Token {

  protected TokenType type;
  protected String text;
  protected Object value;
  protected Source source;
  protected int lineNum;
  protected int position;

  /**
   * Constructor.
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  public Token(Source source) throws Exception {
    this.source = source;
    this.lineNum = source.getLineNum();
    this.position = source.getCurrentPos();

    extract();
  }

  /**
   * Default method to extract exactly one-character tokens from the source.
   * Subclasses can override this method to construct language-specific tokens. After extracting the token, the
   * current source line position will be one beyond the last token character.
   * @throws Exception if an error occurs.
   */
  protected void extract() throws Exception {
    text = Character.toString(currentChar());
    value = null;

    nextChar();
  }

  /**
   * Call the source's {@link Source#nextChar()} method.
   * @return the next character from the source.
   * @throws Exception if an error occurs.
   */
  protected char nextChar() throws Exception {
    return source.nextChar();
  }

  /**
   * Call the source's {@link Source#currentChar()} method.
   * @return the current character from the source.
   * @throws Exception if an error occurs.
   */
  protected char currentChar() throws Exception {
    return source.currentChar();
  }

  /**
   * Calls the source's {@link Source#peekChar()} method.
   * @return the next character from the source without moving forward.
   * @throws Exception if an error occurs.
   */
  protected char peekChar() throws Exception{
    return source.peekChar();
  }

  public TokenType getType() {
    return type;
  }

  public void setType(TokenType type) {
    this.type = type;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Source getSource() {
    return source;
  }

  public void setSource(Source source) {
    this.source = source;
  }

  public int getLineNum() {
    return lineNum;
  }

  public void setLineNum(int lineNum) {
    this.lineNum = lineNum;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }
}
