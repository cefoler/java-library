package com.celeste.library.spigot.exception;

public class InvalidCommandException extends RuntimeException {

  public InvalidCommandException(final String error) {
    super(error);
  }

  public InvalidCommandException(final Throwable cause) {
    super(cause);
  }

  public InvalidCommandException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
