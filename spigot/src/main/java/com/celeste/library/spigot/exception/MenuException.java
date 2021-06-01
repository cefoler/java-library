package com.celeste.library.spigot.exception;

public class MenuException extends RuntimeException {

  public MenuException(final String error) {
    super(error);
  }

  public MenuException(final Throwable cause) {
    super(cause);
  }

  public MenuException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
