package com.celeste.factory;

import com.celeste.model.Configuration;
import com.celeste.model.type.ConfigurationType;
import com.celeste.provider.JsonProvider;
import lombok.Getter;
import lombok.NonNull;

public final class ConfigurationFactory {

  @Getter
  private Configuration configuration;

  @NonNull
  public Configuration create(final String name, final ConfigurationType type) {
    final String resourcePath = "src/main/resources";
    switch(type) {
      case JSON: {
        this.configuration = new JsonProvider(name, resourcePath);
        return configuration;
      }
      case YAML: {

      }
    }

    return null;
  }

}
