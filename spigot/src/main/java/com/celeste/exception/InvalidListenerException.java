package com.celeste.exception;

public class InvalidListenerException extends RuntimeException {

  public InvalidListenerException(String message) {
    super(message);
  }

  public InvalidListenerException(String message, Throwable cause) {
    super(message, cause);
  }

}
