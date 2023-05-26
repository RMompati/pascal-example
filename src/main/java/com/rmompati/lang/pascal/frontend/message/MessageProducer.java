package com.rmompati.lang.pascal.frontend.message;

public interface MessageProducer {

  /**
   * Adds a lister to the listener list.
   * @param listener the listener to add.
   */
  public void addMessageListener(MessageListener listener);

  /**
   * Remove a listener from the listener list.
   * @param listener the listener to remove.
   */
  public void removeMessageListener(MessageListener listener);

  /**
   * Notify listeners after setting the message.
   * @param message the message to send.
   */
  public void sendMessage(Message message);
}
