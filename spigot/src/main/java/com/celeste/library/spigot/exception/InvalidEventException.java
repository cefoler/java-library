package com.celeste.library.spigot.exception;

public class InvalidEventException extends RuntimeException {

  public InvalidEventException(final String error) {
    super(error);
  }

  public InvalidEventException(final Throwable cause) {
    super(cause);
  }

  public InvalidEventException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
