package com.celeste.library.core.model.entity;

import java.util.Properties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Data extends Properties {

  private Data(final Properties properties) {
    super(properties);
  }

  public synchronized Data setData(final Object key, final Object value) {
    put(String.valueOf(key), String.valueOf(value));

    return this;
  }

  public synchronized String getData(final String key) {
    final Object value = get(key);

    return value instanceof String ? String.valueOf(value) : null;
  }

  public synchronized String getData(final String key, final String orElse) {
    final String value = getData(key);

    return value != null ? value : orElse;
  }

  public static synchronized Data create() {
    return new Data();
  }

}
