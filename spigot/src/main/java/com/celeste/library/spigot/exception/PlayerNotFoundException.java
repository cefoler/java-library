package com.celeste.library.spigot.exception;

public class PlayerNotFoundException extends RuntimeException {

  public PlayerNotFoundException(final String error) {
    super(error);
  }

  public PlayerNotFoundException(final Throwable cause) {
    super(cause);
  }

  public PlayerNotFoundException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
