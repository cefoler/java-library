package com.celeste.model.config.exception;

import org.jetbrains.annotations.NotNull;

public class ConfigException extends Exception {

    /**
     * @param cause Throwable
     */
    public ConfigException(@NotNull final Throwable cause) {
        super(cause);
    }

    /**
     * @param error String
     * @param cause Throwable
     */
    public ConfigException(@NotNull final String error, @NotNull final Throwable cause) {
        super(error, cause);
    }

}
