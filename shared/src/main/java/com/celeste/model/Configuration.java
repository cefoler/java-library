package com.celeste.model;

import com.celeste.model.type.ConfigurationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.lang.reflect.Method;

@Data
@Builder
@AllArgsConstructor
public abstract class Configuration implements Serializable {

  private final String name;
  private final String path;

  private final ConfigurationType type;

  public abstract <T> T get(final String key);

}
