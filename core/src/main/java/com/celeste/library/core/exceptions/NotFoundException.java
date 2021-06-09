package com.celeste.library.core.exceptions;

public class NotFoundException extends RuntimeException {

  public NotFoundException(final String error) {
    super(error);
  }

  public NotFoundException(final Throwable cause) {
    super(cause);
  }

  public NotFoundException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
