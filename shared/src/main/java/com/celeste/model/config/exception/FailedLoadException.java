package com.celeste.model.config.exception;

import org.jetbrains.annotations.NotNull;

public class FailedLoadException extends ConfigException {

    /**
     * @param cause Throwable
     */
    public FailedLoadException(@NotNull final Throwable cause) {
        super(cause);
    }

    /**
     * @param error String
     * @param cause Throwable
     */
    public FailedLoadException(@NotNull final String error, @NotNull final Throwable cause) {
        super(error, cause);
    }

}
