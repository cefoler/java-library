package com.celeste.library.spigot.exception;

public class MenuException extends Exception {

    public MenuException(final String message) {
        super(message);
    }

    public MenuException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MenuException(final Throwable cause) {
        super(cause);
    }

}
