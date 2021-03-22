package com.celeste.provider.impl;

import com.celeste.exceptions.UnableToCreateConfigException;
import com.celeste.model.Configuration;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public interface ConfigurationProvider {

  @NonNull Configuration create(final String name, final String path) throws UnableToCreateConfigException;

  @Nullable <T> T get(final String key);

}
