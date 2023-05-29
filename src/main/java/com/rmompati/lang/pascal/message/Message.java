package com.rmompati.lang.pascal.message;

/**
 * <h1>Message</h1>
 *
 * <p>Message format.</p>
 */
public class Message {

  private MessageType type;
  private Object body;

  /**
   * Constructor.
   * @param type the message type.
   * @param body the message body.
   */
  public Message(MessageType type, Object body) {
    this.type = type;
    this.body = body;
  }

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  public Object getBody() {
    return body;
  }

  public void setBody(Object body) {
    this.body = body;
  }
}
