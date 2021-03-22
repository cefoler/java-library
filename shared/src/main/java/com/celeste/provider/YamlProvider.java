package com.celeste.provider;

import com.celeste.exceptions.UnableToCreateConfigException;
import com.celeste.model.Configuration;
import com.celeste.model.type.ConfigurationType;
import com.celeste.provider.impl.ConfigurationProvider;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;

public final class YamlProvider extends Configuration implements ConfigurationProvider {

  private final Yaml yaml;

  public YamlProvider(final String name, final String path) {
    super(name, path, ConfigurationType.YAML);
    this.yaml = new Yaml();
  }

  @Override
  public @NonNull Configuration create(String name, String path) throws UnableToCreateConfigException {
    final File file = new File(name + ".yaml");

    try {
      file.createNewFile();

      return Configuration.builder()
          .name(name)
          .type(ConfigurationType.YAML)
          .path(path)
          .build();
    } catch (IOException exception) {
      throw new UnableToCreateConfigException("Error while creating JSON configuration", exception);
    }
  }

  @Override
  public <T> @Nullable T get(final String key) {
    return yaml.load(key);
  }

}
