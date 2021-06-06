package com.celeste.library.bungee.exception;

public class InvalidListenerException extends RuntimeException {

  public InvalidListenerException(final String error) {
    super(error);
  }

  public InvalidListenerException(final Throwable cause) {
    super(cause);
  }

  public InvalidListenerException(final String error, Throwable cause) {
    super(error, cause);
  }

}
