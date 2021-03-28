package com.celeste.model.config.exception;

import org.jetbrains.annotations.NotNull;

public class FailedSaveException extends ConfigException {

    /**
     * @param cause Throwable
     */
    public FailedSaveException(@NotNull final Throwable cause) {
        super(cause);
    }

    /**
     * @param error String
     * @param cause Throwable
     */
    public FailedSaveException(@NotNull final String error, @NotNull final Throwable cause) {
        super(error, cause);
    }

}
