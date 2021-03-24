package com.celeste.exception;

public class InvalidPropertyException extends RuntimeException {

  public InvalidPropertyException(String message) {
    super(message);
  }

  public InvalidPropertyException(String message, Throwable cause) {
    super(message, cause);
  }

}
