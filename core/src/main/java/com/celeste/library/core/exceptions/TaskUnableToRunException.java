package com.celeste.library.core.exceptions;

public class TaskUnableToRunException extends RuntimeException {

  public TaskUnableToRunException() {
  }

  public TaskUnableToRunException(String message) {
    super(message);
  }

  public TaskUnableToRunException(String message, Throwable cause) {
    super(message, cause);
  }

  public TaskUnableToRunException(Throwable cause) {
    super(cause);
  }

}
