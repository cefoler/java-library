package com.celeste.library.spigot.exception;

import java.io.IOException;

public class PlayerNotFoundException extends IOException {

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
