package com.celeste.model.type;

import com.celeste.provider.JsonProvider;
import com.celeste.provider.YamlProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public enum ConfigurationType {

  YAML(JsonProvider.class),
  JSON(YamlProvider.class);

  @NonNull
  private final Class<?> provider;

}
