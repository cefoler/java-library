package com.celeste.exceptions;

public class UnableToCreateConfigException extends Exception {

  public UnableToCreateConfigException(final String message) {
    super(message);
  }

  public UnableToCreateConfigException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
