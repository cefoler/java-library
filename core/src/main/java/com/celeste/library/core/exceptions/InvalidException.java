package com.celeste.library.core.exceptions;

public class InvalidException extends RuntimeException {

  public InvalidException(final String error) {
    super(error);
  }

  public InvalidException(final Throwable cause) {
    super(cause);
  }

  public InvalidException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
