package com.celeste.model.config.exception;

import org.jetbrains.annotations.NotNull;

public class FailedCreateException extends ConfigException {

    /**
     * @param cause Throwable
     */
    public FailedCreateException(@NotNull final Throwable cause) {
        super(cause);
    }

    /**
     * @param error String
     * @param cause Throwable
     */
    public FailedCreateException(@NotNull final String error, @NotNull final Throwable cause) {
        super(error, cause);
    }

}
