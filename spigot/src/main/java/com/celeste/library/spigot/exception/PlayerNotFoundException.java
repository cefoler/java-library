package com.celeste.library.spigot.exception;

import java.io.IOException;

public class PlayerNotFoundException extends IOException {

  /**
   * @param error String
   */
  public PlayerNotFoundException(final String error) {
    super(error);
  }

  /**
   * @param cause Throwable
   */
  public PlayerNotFoundException(final Throwable cause) {
    super(cause);
  }

  /**
   * @param error String
   * @param cause Throwable
   */
  public PlayerNotFoundException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
