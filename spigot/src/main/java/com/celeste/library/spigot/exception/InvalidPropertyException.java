package com.celeste.library.spigot.exception;

public class InvalidPropertyException extends RuntimeException {

  public InvalidPropertyException(final String error) {
    super(error);
  }

  public InvalidPropertyException(final Throwable cause) {
    super(cause);
  }

  public InvalidPropertyException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
