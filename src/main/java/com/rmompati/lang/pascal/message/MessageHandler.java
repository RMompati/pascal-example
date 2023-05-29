package com.rmompati.lang.pascal.message;

import java.util.ArrayList;

/**
 * <h1>MessageHandler</h1>
 *
 * A helper class to which message producer classes delegate the task of maintaining and notifying listeners.
 */
public class MessageHandler {
  private Message message;
  private ArrayList<MessageListener> listeners;

  /**
   * Constructor
   */
  public MessageHandler() {
    this.listeners = new ArrayList<>();
  }

  /**
   * Add listener to the listener list.
   * @param listener the listener to add.
   */
  public void addListener(MessageListener listener) {
    listeners.add(listener);
  }

  /**
   * Remove listener from the listener list.
   * @param listener the listener to remove.
   */
  public void removeListener(MessageListener listener) {
    listeners.remove(listener);
  }

  /**
   * Notify listeners after setting the message.
   * @param message the message to set.
   */
  public void sendMessage(Message message) {
    this.message = message;
    notifyListeners();
  }
  
  /**
   * Notify each listener in the listener list by calling the {@link MessageListener#messageReceived(Message)} method.
   */
  private void notifyListeners() {
    listeners.forEach(listener -> listener.messageReceived(message));
  }
}
