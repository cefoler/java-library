package com.celeste.library.bungee.exception;

public class InvalidCommandException extends RuntimeException {

  public InvalidCommandException(final String error) {
    super(error);
  }

  public InvalidCommandException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
