package com.celeste.library.core.model.entity;

import java.util.Properties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Data extends Properties {

  private Data(final Properties properties) {
    super(properties);
  }

  public synchronized Data setData(final Object key, final Object value) {
    put(String.valueOf(key), value);
    return this;
  }

  public synchronized Data setDataIfAbsent(final Object key, final Object value) {
    putIfAbsent(String.valueOf(key), value);
    return this;
  }

  @SuppressWarnings("unchecked")
  public synchronized <T> T getData(final String key) {
    return (T) get(key);
  }

  @SuppressWarnings("unchecked")
  public synchronized <T> T getData(final String key, final Object orElse) {
    final T value = getData(key);
    return value != null ? value : (T) orElse;
  }

  public static synchronized Data create() {
    return new Data();
  }

  public static synchronized Data create(final Properties properties) {
    return new Data(properties);
  }

}
