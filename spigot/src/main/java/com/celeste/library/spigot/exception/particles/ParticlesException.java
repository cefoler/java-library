package com.celeste.library.spigot.exception.particles;

public final class ParticlesException extends RuntimeException {

  public ParticlesException(final String message) {
    super(message);
  }

  public ParticlesException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ParticlesException(final Throwable cause) {
    super(cause);
  }

}
