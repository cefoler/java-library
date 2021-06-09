package com.celeste.library.spigot.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(final String error) {
    super(error);
  }

  public UserNotFoundException(final Throwable cause) {
    super(cause);
  }

  public UserNotFoundException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
